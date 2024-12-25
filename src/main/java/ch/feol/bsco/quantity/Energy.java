package ch.feol.bsco.quantity;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * Energy to the precision of millijoule.
 */
public class Energy extends AbstractQuantity {

   static AbstractQuantity.Scale scale = new AbstractQuantity.Scale("mJ", "J", "kJ", "MJ", "GJ");

   protected AbstractQuantity.Scale getScale() {
      return scale;
   }

   private Energy(long millijoule) {
      super(millijoule);
   }

   private Energy(double millijoule) {
      this(Math.round(millijoule));
   }

   public static Energy millijoule(long millijoule) {
      return new Energy(millijoule);
   }

   public static Energy millijoule(double millijoule) {
      return new Energy(millijoule);
   }

   public static Energy joule(long joule) {
      return new Energy(joule * 1e3);
   }

   public static Energy joule(double joule) {
      return new Energy(joule * 1e3);
   }

   public static Energy kilojoule(long kiloJoule) {
      return new Energy(kiloJoule * 1e6);
   }

   public static Energy kilojoule(double kiloJoule) {
      return new Energy(kiloJoule * 1e6);
   }

   public static Energy megajoule(long megajoule) {
      return new Energy(megajoule * 1e9);
   }

   public static Energy megajoule(double megajoule) {
      return new Energy(megajoule * 1e9);
   }

   public static Energy gigajoule(long gigajoule) {
      return new Energy(gigajoule * 1e12);
   }

   public static Energy gigajoule(double gigajoule) {
      return new Energy(gigajoule * 1e12);
   }

   /**
    * Calculate how long it takes to consume this energy with a given power.
    * 
    * @param power
    *           The power.
    * @return the duration it takes to consume this energy with the power.
    */
   public Duration consume(Power power) {
      double nanos = 1e9d * quantity / power.quantity;
      return Duration.of(Math.round(nanos), ChronoUnit.NANOS);
   }

   public static Energy none() {
      return new Energy(0);
   }

   public Energy add(Energy energy) {
      return new Energy(quantity + energy.quantity);
   }
}
