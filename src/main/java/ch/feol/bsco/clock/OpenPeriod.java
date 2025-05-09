package ch.feol.bsco.clock;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A <b>OpenPeriod</b> defines a section on the timeline with a start and no end timestamp.
 * <p>
 * For periods that have a end timestamp see {@link ClosedPeriod}.
 */
public class OpenPeriod {

   private LocalDateTime startAt;

   /**
    * Constructor.
    * 
    * @param startAt
    *           The start timestamp. Must not be null.
    */
   public OpenPeriod(LocalDateTime startAt) {
      this.startAt = Objects.requireNonNull(startAt);
   }

   public LocalDateTime getStartAt() {
      return this.startAt;
   }

   /**
    * End this period and return the corresponding {@link ClosedPeriod}.
    * 
    * @param localDateTime
    *           The end timestamp. Not null.
    * @return the closed period which corresponds to the ended period.
    */
   public ClosedPeriod end(LocalDateTime endAt) {
      return new ClosedPeriod(startAt, endAt);
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
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
      OpenPeriod other = (OpenPeriod) obj;
      if (startAt == null) {
         if (other.startAt != null)
            return false;
      } else if (!startAt.equals(other.startAt))
         return false;
      return true;
   }
}
