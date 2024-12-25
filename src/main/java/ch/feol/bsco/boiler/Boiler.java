package ch.feol.bsco.boiler;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.feol.bsco.Configuration;
import ch.feol.bsco.clock.Clock;
import ch.feol.bsco.clock.Interval;
import ch.feol.bsco.clock.IntervalList;
import ch.feol.bsco.pi.board.DummyRelay;
import ch.feol.bsco.pi.board.RealRelay;
import ch.feol.bsco.pi.board.Relay;
import ch.feol.bsco.quantity.Power;

/**
 * The {@link Boiler} implements a real or dummy boiler.
 * <p>
 * If the relay is a {@link RealRelay} the boiler is on when B-C is connected and the LED is NOT burning.
 * <p>
 * Specific heat capacity of water: 4.2kJ/kg*K
 * <p>
 * 50 degrees K from 10 to 60 degrees C and 120 litres = 25,200kJ
 * <p>
 * With heating power of 2.4kW -> 10'500 sec -> 175 min -> ca. 3h
 */
public class Boiler {

   private static final Logger LOG = LoggerFactory.getLogger(Boiler.class);

   private static final Duration MAX_HISTORY = Duration.ofDays(2);

   boolean on = true;

   private final String name;

   private final Power nominalPower;

   private final Duration totalHeatUpDuration;

   private final Clock clock;

   private LocalDateTime switchedOnAt;

   private LocalDateTime switchedOffAt;

   private final IntervalList onIntervals;

   private final Duration minimalOnDuration;

   private final Relay relay;

   public Boiler(Relay relay, Power nominalPower, Duration totalHeatUpDuration, Clock clock, Duration minimalOnDuration) {
      this.relay = relay;
      this.nominalPower = nominalPower;
      this.totalHeatUpDuration = totalHeatUpDuration;
      this.clock = clock;
      name = "boiler " + relay.getRelayNumber();
      switchedOnAt = clock.now();
      switchedOffAt = switchedOnAt;
      onIntervals = new IntervalList(clock, MAX_HISTORY);
      this.minimalOnDuration = minimalOnDuration;
      LOG.info("Turning {} off during start", name);
      relay.connectorA(); // Unconditionally turning boiler OFF during start.
      on = false;
   }

   /**
    * Turn the boiler ON.
    * 
    * @return true if the boiler was effectively turned ON or false if it was ON already.
    */
   public boolean on() {
      if (!on) {
         on = true;
         switchedOnAt = clock.now();
         relay.connectorB();
         LOG.info("{} switched on at {}", name, switchedOnAt);
         return true;
      } else {
         return false;
      }
   }

   /**
    * Turn the boiler OFF.
    * 
    * @return true if the boiler was effectively turned OFF or false if it was OFF already.
    */
   public boolean off() {
      LocalDateTime now = clock.now();
      if (on) {
         if (switchedOnAt.plus(minimalOnDuration).isBefore(now)) {
            on = false;
            this.switchedOffAt = now;
            Interval onInterval = new Interval(switchedOnAt, switchedOffAt);
            onIntervals.add(onInterval);
            relay.connectorA();
            LOG.info("{} switched off at {}", name, switchedOffAt);
            return true;
         } else {
            LOG.info("{} not switched off at {} since it only was switched on at {}", name, now, switchedOnAt);
            return false;
         }
      } else {
         return false;
      }
   }

   /**
    * Unconditionally turning boiler ON during shutdown.
    */
   public void shutdown() {
      LOG.info("Turning {} on during shutdown", name);
      relay.connectorB();
   }

   public boolean isInactive() {
      return !isActive();
   }

   public boolean isActive() {
      return on;
   }

   public String toString() {
      return name;
   }

   /**
    * Get a list of {@link Boiler}s initialized with {@link DummyRelay}s.
    * 
    * @param number
    *           The number of {@link Boiler}s to create. Must be less than the number of relays.
    * @param nominalPower
    *           The nominal power of the boilers.
    * @param clock
    *           The clock.
    * @param minimalDuration
    *           The minimal duration a boiler must be left switched on.
    * @return the list of {@link Boiler}s.
    */
   private static List<Boiler> getDummyBoilers(int number, Power nominalPower, Duration totalHeatUpDuration, Clock clock, Duration minimalOnDuration) {
      List<Boiler> boilers = new ArrayList<>();
      for (int i = 0; i < number; i++) {
         Relay relay = new DummyRelay(i + 1);
         boilers.add(new Boiler(relay, nominalPower, totalHeatUpDuration, clock, minimalOnDuration));
      }
      return boilers;
   }

   /**
    * Get the list of {@link Boiler}s initialized with {@link RealRelay}s.
    * 
    * @param number
    *           The number of {@link Boiler}s to create. Must be less than the number of relays.
    * @param nominalPower
    *           The nominal power of the boilers.
    * @param clock
    *           The clock.
    * @param minimalDuration
    *           The minimal duration a boiler must be left switched on.
    * @return the list of {@link Boiler}s.
    */
   private static List<Boiler> getRealBoilers(int number, Power nominalPower, Duration totalHeatUpDuration, Clock clock, Duration minimalOnDuration) {
      List<Boiler> boilers = new ArrayList<>();
      RealRelay[] relays = RealRelay.values();
      if (number > relays.length) {
         throw new IllegalStateException("Number of boilers to create (" + number + ") exceeds number of available relays (" + relays.length + ")");
      }
      for (int i = 0; i < number; i++) {
         boilers.add(new Boiler(relays[i], nominalPower, totalHeatUpDuration, clock, minimalOnDuration));
      }
      return boilers;
   }

   /**
    * Try to estimate the probable heating power or {@link Power#none()} if the boiler is not active.
    * <p>
    * Remark: Use this method in simulation mode only!
    * 
    * @return the estimated active power.
    */
   public Power getEstimatedActivePower() {
      if (isActive()) {
         return getEstimatedPower(Duration.ofHours(16), totalHeatUpDuration.dividedBy(2));
      } else {
         return Power.none();
      }
   }

   public Duration getInactiveDuration() {
      if (on) {
         return Duration.ZERO;
      } else {
         Duration difference = Duration.between(switchedOffAt, clock.now());
         if (difference.isNegative()) {
            LOG.error("Switched off at {} is after {}", switchedOffAt, clock.now());
            return Duration.ZERO;
         }
         return difference;
      }
   }

   public Duration getActiveDuration() {
      if (!on) {
         return Duration.ZERO;
      } else {
         Duration difference = Duration.between(switchedOnAt, clock.now());
         if (difference.isNegative()) {
            LOG.error("Switched on at {} is after {}", switchedOnAt, clock.now());
            return Duration.ZERO;
         }
         return difference;
      }
   }

   public Duration getTotalOnDuration(Duration since) {
      return onIntervals.getTotalOnDuration(since);
   }

   /**
    * Try to estimate the probable heating power if the boiler were switched on now.
    * <p>
    * Return the nominal power if the boilers total ON duration is less than the limit during the specified history duration.
    * 
    * @param history
    *           The history.
    * @param limit
    *           The limit.
    * @return the estimated power (nominal) or {@link Power#none()} the total ON duration was longer than the limit.
    */
   Power getEstimatedPower(Duration history, Duration limit) {
      if (getTotalOnDuration(history).compareTo(limit) < 0) {
         return nominalPower;
      } else {
         return Power.none();
      }
   }

   public static List<Boiler> getBoilers(Clock clock, boolean realBoilers) {
      if (realBoilers) {
         return getRealBoilers(Configuration.NUMBER_OF_BOILERS, Configuration.BOILER_NOMINAL_POWER, //
               Configuration.TOTAL_HEAT_UP_DURATION, clock, Configuration.MINIMAL_ON_DURATION);
      } else {
         return getDummyBoilers(Configuration.NUMBER_OF_BOILERS, Configuration.BOILER_NOMINAL_POWER, //
               Configuration.TOTAL_HEAT_UP_DURATION, clock, Configuration.MINIMAL_ON_DURATION);
      }
   }
}
