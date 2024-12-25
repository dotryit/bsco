package ch.feol.bsco.quantity;

import ch.feol.bsco.substance.Liquid;

/**
 * Volume to the precision of milliliter.
 */
public class Volume extends AbstractQuantity {

   static AbstractQuantity.Scale scale = new AbstractQuantity.Scale("ml", "l", "m2");

   protected AbstractQuantity.Scale getScale() {
      return scale;
   }

   private Volume(double milliliter) {
      this(Math.round(milliliter));
   }

   private Volume(long milliliter) {
      super(milliliter);
   }

   public Volume add(Volume volume) {
      return new Volume(this.quantity + volume.quantity);
   }

   public static Volume empty() {
      return Volume.milliliter(0);
   }

   public static Volume liter(int liter) {
      return new Volume(liter * 1000l);
   }

   public static Volume liter(double liter) {
      return new Volume(liter * 1000);
   }

   public static Volume squareMeter(int quantity) {
      return new Volume(quantity + 1e6);
   }

   /**
    * Calculate the energy it takes to heat a liquid.
    * 
    * @param liquid
    *           The liquid to heat.
    * @param temperatureDifference
    *           The temperature difference in degrees kelvin.
    * @return the energy it takes to heat the liquid.
    */
   public Energy heat(Liquid liquid, double temperatureDifference) {
      return liquid.heat(this, temperatureDifference);
   }

   public Volume multiply(int factor) {
      return new Volume(quantity * factor);
   }

   public Volume multiply(double factor) {
      return new Volume(quantity * factor);
   }

   public Volume divide(long divisor) {
      return new Volume(quantity / (double) divisor);
   }

   public static Volume milliliter(int milliliter) {
      return new Volume(milliliter);
   }

   public static Volume none() {
      return Volume.milliliter(0);
   }

   public Volume subtract(Volume volume) {
      long newVolume = quantity - volume.quantity;
      if (newVolume > 0) {
         return new Volume(newVolume);
      } else {
         return Volume.none();
      }
   }

   /**
    * Mix two volumes of the same liquid: t = (v1 * t1 + v2 * t2) / (v1 + v2)
    * 
    * @param v1
    *           Volume 1
    * @param t1
    *           Temperature 1
    * @param v2
    *           Volume 2
    * @param t2
    *           Temperature 2
    * @return the resulting temperature.
    */
   public static Temperature mix(Volume v1, Temperature t1, Volume v2, Temperature t2) {
      return Temperature.millikelvin((v1.quantity * t1.quantity + v2.quantity * t2.quantity) / (v1.quantity + v2.quantity));
   }

   public boolean isEmpty() {
      return quantity == 0;
   }
}
