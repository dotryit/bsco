package ch.feol.bsco.solar;

public class SolarLogException extends Exception {

   public SolarLogException(String message) {
      super(message);
   }

   public SolarLogException(String message, Exception cause) {
      super(message, cause);
   }
}
