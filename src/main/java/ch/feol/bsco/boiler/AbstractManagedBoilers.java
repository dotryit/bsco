package ch.feol.bsco.boiler;

import java.time.Duration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.feol.bsco.clock.Clock;
import ch.feol.bsco.pi.Pi4jProvider;
import ch.feol.bsco.quantity.Power;

public abstract class AbstractManagedBoilers {

   private static final Logger LOG = LoggerFactory.getLogger(AbstractManagedBoilers.class);

   private final List<Boiler> boilers;

   private final boolean estimatePower;

   private final Clock clock;

   /**
    * Manage a set of managed boilers.
    * 
    * @param clock
    *           The clock to use.
    * @param realBoilers
    *           True if relays and associated real boilers are to be used.
    * @param estimatePower
    *           True if boiler power is to be estimated and taken into account with suplus power.
    */
   protected AbstractManagedBoilers(Clock clock, boolean realBoilers, boolean estimatePower) {
      this.clock = clock;
      this.boilers = Boiler.getBoilers(clock, realBoilers);
      this.estimatePower = estimatePower;
      addShutdownHook();
   }

   private void addShutdownHook() {
      Runtime.getRuntime().addShutdownHook(new Thread() {
         @Override
         public void run() {
            LOG.info("Shutting boilers down");
            if (boilers != null) {
               boilers.stream().forEach(Boiler::shutdown);
            }
            Pi4jProvider.shutdown();
         }
      });
   }

   public List<Boiler> getBoilers() {
      return boilers;
   }

   Power getTotalActivePower() {
      if (estimatePower) {
         Power active = Power.none();
         for (Boiler boiler : boilers) {
            active = active.plus(boiler.getEstimatedActivePower());
         }
         if (active.isMoreThan(Power.none())) {
            LOG.debug("Total active power is {} at {}", active, clock.now());
         }
         return active;
      } else {
         return Power.none();
      }
   }

   Boiler getLongestInactive() {
      Duration longestInactiveDuration = Duration.ZERO;
      Boiler longestInactiveBoiler = null;
      for (Boiler boiler : boilers) {
         if (boiler.isInactive()) {
            Duration inactiveDuration = boiler.getInactiveDuration();
            if (longestInactiveDuration.minus(inactiveDuration).isNegative()) {
               longestInactiveBoiler = boiler;
               longestInactiveDuration = inactiveDuration;
            }
         }
      }
      if (longestInactiveBoiler != null) {
         LOG.debug("{} was inactive for {}", longestInactiveBoiler, longestInactiveDuration);
      } else {
         LOG.debug("No inactive boiler found");
      }
      return longestInactiveBoiler;
   }

   Boiler getLongestActive() {
      Duration longestActiveDuration = Duration.ZERO;
      Boiler longestActiveBoiler = null;
      for (Boiler boiler : boilers) {
         if (boiler.isActive()) {
            Duration activeDuration = boiler.getActiveDuration();
            if (longestActiveDuration.minus(activeDuration).isNegative()) {
               longestActiveBoiler = boiler;
               longestActiveDuration = activeDuration;
            }
         }
      }
      if (longestActiveBoiler != null) {
         LOG.debug("{} was active for {}", longestActiveBoiler, longestActiveDuration);
      } else {
         LOG.debug("No inactive boiler found");
      }
      return longestActiveBoiler;
   }
}
