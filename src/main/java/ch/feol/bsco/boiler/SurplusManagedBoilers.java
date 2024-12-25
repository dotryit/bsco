package ch.feol.bsco.boiler;

import java.time.Duration;
import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.feol.bsco.Configuration;
import ch.feol.bsco.clock.Clock;
import ch.feol.bsco.clock.SlotManager;
import ch.feol.bsco.quantity.Power;

/**
 * The {@link SurplusManagedBoilers} is heating the boilers when there is surplus solar power and in regular intervals at night.
 */
public class SurplusManagedBoilers extends AbstractManagedBoilers {

   private static final Logger LOG = LoggerFactory.getLogger(SurplusManagedBoilers.class);

   private static final int BOILERS_PER_SLOT = 2;

   private static final LocalTime START_DAY = LocalTime.of(6, 0);

   private static final LocalTime END_DAY = LocalTime.of(20, 0);

   private static final Duration DAY_DURATION = Duration.between(START_DAY, END_DAY);

   private static final Duration NIGHT_DURATION = Duration.ofDays(1).minus(DAY_DURATION);

   private static final Power OFFSET = Power.none();

   private static final Power HYSTERESIS = Power.kilowatt(0.5);

   private static final Power ON_THRESHOLD = OFFSET.plus(HYSTERESIS.plus(Configuration.BOILER_NOMINAL_POWER));

   private static final Power OFF_THRESHOLD = OFFSET;

   private final Clock clock;

   private final SlotManager slotManager;

   /**
    * Manage a set of surplus managed boilers.
    * 
    * @param clock
    *           The clock to use.
    * @param realBoilers
    *           True if relays and associated real boilers are to be used.
    * @param estimatePower
    *           True if boiler power is to be estimated and taken into account with surplus power.
    */
   public SurplusManagedBoilers(Clock clock, boolean realBoilers, boolean estimatePower) {
      super(clock, realBoilers, estimatePower);
      this.clock = clock;
      slotManager = new SlotManager(getBoilers(), clock, BOILERS_PER_SLOT, END_DAY, NIGHT_DURATION);
   }

   public void manage(Power netto) {
      if (clock.now().toLocalTime().isAfter(START_DAY.plus(Configuration.CHECK_INTERVAL)) && clock.now().toLocalTime().isBefore(END_DAY)) {
         manageDay(netto);
      } else {
         manageNight();
      }
   }

   private void manageNight() {
      slotManager.manage();
   }

   private void manageDay(Power netto) {
      Power effectiveNetto = netto.minus(getTotalActivePower());
      if (ON_THRESHOLD.isLessThan(effectiveNetto)) {
         LOG.debug("Effectiv netto power of {} is higher than ON threshold of {}", effectiveNetto, ON_THRESHOLD);
         Boiler longestInactive = getLongestInactive();
         if (longestInactive != null) {
            if (longestInactive.on()) {
               LOG.info("Netto power at {} is {}", clock.now(), effectiveNetto);
            }
         }
      } else if (OFF_THRESHOLD.isMoreThan(effectiveNetto)) {
         LOG.debug("Effective netto power of {} is lower than OFF threshold of {}", effectiveNetto, OFF_THRESHOLD);
         Boiler longestActive = getLongestActive();
         if (longestActive != null) {
            if (longestActive.off()) {
               LOG.info("Netto power at {} is {}", clock.now(), effectiveNetto);
            }
         }
      } else {
         LOG.debug("Effective netto power is {}", effectiveNetto);
      }
   }
}
