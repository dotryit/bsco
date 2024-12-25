package ch.feol.bsco.clock;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class IntervalList {

   private final List<Interval> intervalList;

   private Clock clock;

   private Duration maxHistory;

   public IntervalList(Clock clock, Duration maxHistory) {
      this.clock = clock;
      this.maxHistory = maxHistory;
      this.intervalList = new ArrayList<>();
   }

   public void add(Interval onInterval) {
      intervalList.add(onInterval);
      purge();
   }

   public Duration getTotalOnDuration() {
      return purge();
   }

   private Duration purge() {
      Duration total = Duration.ZERO;
      LocalDateTime limit = clock.now().minus(maxHistory);
      List<Interval> toRemove = new ArrayList<>();
      for (Interval interval : intervalList) {
         if (interval.getEndAt().isBefore(limit)) {
            toRemove.add(interval);
         } else {
            total = total.plus(interval.getDuration().abs());
         }
      }
      intervalList.removeAll(toRemove);
      return total;
   }

   public Duration getTotalOnDuration(Duration since) {
      LocalDateTime limit = clock.now().minus(since);
      Duration total = Duration.ZERO;
      for (Interval interval : intervalList) {
         if (interval.getEndAt().isAfter(limit)) {
            if (interval.getStartAt().isAfter(limit)) {
               total = total.plus(interval.getDuration());
            } else {
               total = total.plus(Duration.between(limit, interval.getEndAt()));
            }
         }
      }
      return total;
   }
}
