package ch.feol.bsco.solar;

public interface Stepper {

   /**
    * @return true if the stepper is to continue and false otherwise.
    */
   boolean next();
}
