package ch.feol.bsco.clock;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * The {@link SimulationTimeService} is used in simulation mode.
 * <p>
 * Sleeping is performed by forwarding the local date time with the specified duration and returning immediately, so that simulation does complete fast.
 *
 */
public class SimulationTimeService implements TimeService {

   private LocalDateTime localDateTime;

   public SimulationTimeService() {
      // Start at midnight
      this.localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
   }

   public SimulationTimeService(LocalDateTime start) {
      this.localDateTime = start;
   }

   @Override
   public LocalDateTime getActualTimestamp() {
      return localDateTime;
   }

   @Override
   public void sleepMinutes(int minutes) {
      localDateTime = localDateTime.plusMinutes(minutes);
   }

   @Override
   public void sleepHours(int hours) {
      localDateTime = localDateTime.plusHours(hours);
   }
}
