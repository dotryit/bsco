package ch.feol.bsco.clock;

import java.time.LocalDateTime;

/**
 * The {@link TimeService} provides time related services.
 * <p>
 * In simulations the actual timestamp is the simulated timestamp. And sleep only pushes the time forward the specified duration.
 */
public interface TimeService {

   /**
    * @return the actual timestamp.
    */
   public LocalDateTime getActualTimestamp();

   /**
    * Sleep for a set of minutes.
    * 
    * @param minutes
    *           the minutes to sleep.
    */
   public void sleepMinutes(int minutes);

   /**
    * Sleep for a set of hours.
    * 
    * @param hours
    *           the number of hours to sleep.
    */
   public void sleepHours(int hours);

}
