package ch.feol.bsco.clock;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

public class StartEndSimulatedClock implements Clock {

   private final LocalDateTime end;

   private final Duration interval;

   private LocalDateTime now;

   private final Random random = new Random();

   private final long maxJitterNanos;

   public StartEndSimulatedClock(LocalDateTime start, LocalDateTime end, Duration interval, long maxJitterNanos) {
      now = start;
      this.end = end;
      this.interval = interval;
      this.maxJitterNanos = maxJitterNanos;
   }

   @Override
   public LocalDateTime now() {
      return now;
   }

   @Override
   public void next() {
      if (maxJitterNanos > 0) {
         now = now.plus(interval).plusNanos(random.nextLong(maxJitterNanos));
      } else {
         now = now.plus(interval);
      }
   }

   @Override
   public boolean isExpired() {
      return now.isAfter(end);
   }
}
