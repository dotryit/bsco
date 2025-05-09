package ch.feol.bsco.clock;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * A <b>ClosedPeriod</b> defines a section on the timeline with a start and an end timestamp.
 * <p>
 * For periods with no end timestamp see {@link OpenPeriod}.
 */
public class ClosedPeriod {

   private final LocalDateTime startAt;

   private final LocalDateTime endAt;

   /**
    * Constructor.
    * 
    * @param startAt
    *           The start timestamp. Must not be null.
    * @param endAt
    *           The end timestamp. Must be later or equal to the start timestamp.
    */
   public ClosedPeriod(LocalDateTime startAt, LocalDateTime endAt) {
      this.startAt = Objects.requireNonNull(startAt);
      this.endAt = Objects.requireNonNull(endAt);
      if (startAt.isAfter(endAt)) {
         throw new IllegalArgumentException("Start at must be before end at");
      }
   }

   public LocalDateTime getStartAt() {
      return this.startAt;
   }

   public LocalDateTime getEndAt() {
      return endAt;
   }

   public long getDays() {
      return startAt.until(endAt, ChronoUnit.DAYS);
   }

   public Duration getDuration() {
      return Duration.between(startAt, endAt);
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((endAt == null) ? 0 : endAt.hashCode());
      result = prime * result + ((startAt == null) ? 0 : startAt.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      ClosedPeriod other = (ClosedPeriod) obj;
      if (endAt == null) {
         if (other.endAt != null)
            return false;
      } else if (!endAt.equals(other.endAt))
         return false;
      if (startAt == null) {
         if (other.startAt != null)
            return false;
      } else if (!startAt.equals(other.startAt))
         return false;
      return true;
   }
}
