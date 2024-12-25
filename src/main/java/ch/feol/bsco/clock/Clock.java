package ch.feol.bsco.clock;

import java.time.LocalDateTime;

public interface Clock {

   /**
    * @return the actual local date time.
    */
   LocalDateTime now();

   /**
    * Wait for the next interval to expire.
    */
   void next();

   /**
    * @return true if this clock is expired and the simulation should be stopped.
    */
   boolean isExpired();
}
