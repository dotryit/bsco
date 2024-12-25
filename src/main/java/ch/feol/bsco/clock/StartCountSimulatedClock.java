package ch.feol.bsco.clock;

import java.time.Duration;
import java.time.LocalDateTime;

public class StartCountSimulatedClock implements Clock {

   private LocalDateTime now;

   private Duration interval;

   private long count;

   private long calls = 0;

   /**
    * @param start
    *           The start time
    * @param count
    *           The number of intervals to go until the clock expires.
    * @param interval
    *           The duration of the simulation interval
    */
   public StartCountSimulatedClock(LocalDateTime start, long count, Duration interval) {
      this.now = start;
      this.interval = interval;
      this.count = count;
   }

   @Override
   public LocalDateTime now() {
      return now;
   }

   @Override
   public void next() {
      now = now.plus(interval);
      calls++;
   }

   @Override
   public boolean isExpired() {
      return calls >= count;
   }
}
