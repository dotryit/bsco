package ch.feol.bsco.clock;

import java.time.LocalDateTime;

/**
 * The {@link SystemTimeService} is based on the the current date-time from the system clock in the default time-zone.
 * <p>
 * The {@link SystemTimeService} is used in normal management mode.
 */
public class SystemTimeService implements TimeService {

   @Override
   public LocalDateTime getActualTimestamp() {
      return LocalDateTime.now();
   }

   @Override
   public void sleepMinutes(int minutes) {
      try {
         Thread.sleep(1000 * 60);
      } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
      }
   }

   @Override
   public void sleepHours(int hours) {
      sleepMinutes(60);
   }
}
