package ch.feol.bsco.boiler;

import java.time.Duration;
import java.time.LocalTime;

import ch.feol.bsco.clock.Clock;
import ch.feol.bsco.clock.SlotManager;

public class TimeManagedBoilers extends AbstractManagedBoilers {

   private final SlotManager slotManager;
   
   /**
    * Manage a set of time managed boilers.
    * 
    * @param clock
    *           The clock to use.
    * @param realBoilers
    *           True if relays and associated real boilers are to be used.
    * @param estimatePower
    *           True if boiler power is to be estimated and taken into account with surplus power.
    */
   public TimeManagedBoilers(Clock clock, boolean realBoilers, boolean estimatePower) {
      super(clock, realBoilers, estimatePower);
      slotManager = new SlotManager(getBoilers(), clock, 2, LocalTime.of(2, 17), Duration.ofHours(24));
   }

   public void manage() {
      slotManager.manage();
   }
}
