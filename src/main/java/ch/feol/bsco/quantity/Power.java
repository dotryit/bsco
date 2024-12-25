package ch.feol.bsco.quantity;

import java.time.Duration;

/**
 * Power to the precision of a milliwatt.
 */
public class Power extends AbstractQuantity {

   static AbstractQuantity.Scale scale = new AbstractQuantity.Scale("mW", "W", "kW", "MW", "GW");

   protected AbstractQuantity.Scale getScale() {
      return scale;
   }

   private Power(long milliwatt) {
      super(milliwatt);
   }

   private Power(double milliwatt) {
      this(Math.round(milliwatt));
   }

   public Power plus(Power power) {
      return new Power(this.quantity + power.quantity);
   }

   /**
    * Consume this power for a specific duration.
    * 
    * @param duration
    *           The duration.
    * @return the energy consumed.
    */
   public Energy consume(Duration duration) {
      double seconds = duration.getSeconds() + (duration.getNano() / 1e9d);
      return Energy.joule(quantity * seconds / 1000d);
   }

   public static Power milliwatt(double milliwatt) {
      return new Power(Math.round(milliwatt));
   }

   public static Power milliwatt(long milliwatt) {
      return new Power(milliwatt);
   }

   public static Power watt(double watt) {
      return new Power(watt * 1000);
   }

   public static Power watt(long watt) {
      return new Power(watt * 1000);
   }

   public static Power kilowatt(double kilowatt) {
      return new Power(kilowatt * 1e6);
   }

   public static Power kilowatt(long kilowatt) {
      return new Power(kilowatt * 1e6);
   }

   public static Power megawatt(double megawatt) {
      return new Power(megawatt * 1e9);
   }

   public static Power megawatt(long megawatt) {
      return new Power(megawatt * 1e9);
   }

   public static Power gigawatt(double gigawatt) {
      return new Power(gigawatt * 1e12);
   }

   public static Power gigawatt(long gigawatt) {
      return new Power(gigawatt * 1e12);
   }

   public Power multiply(double factor) {
      return new Power(quantity * factor);
   }

   public Power multiply(long factor) {
      return new Power(quantity * factor);
   }

   public boolean isLessThan(Power other) {
      return quantity < other.quantity;
   }

   public boolean isMoreThan(Power other) {
      return quantity > other.quantity;
   }

   public static Power none() {
      return new Power(0);
   }

   public Power divide(int size) {
      if (size != 0) {
         return new Power(quantity / size);
      } else {
         throw new IllegalArgumentException("Divsion by zero");
      }
   }

   public static Power parse(String input) {
      return Power.milliwatt(scale.parse(input));
   }

   /**
    * Subtract a power value.
    * 
    * @param other
    *           The power value to subtract.
    * @return the resulting power value.
    */
   public Power minus(Power other) {
      return new Power(quantity - other.quantity);
   }

   /**
    * Subtract a power value with minimum limitation.
    * 
    * @param other
    *           The power value to subtract.
    * @param min
    *           Limit the resulting power.
    * @return the resulting power value.
    */
   public Power minus(Power other, Power min) {
      long resulting = this.quantity - other.quantity;
      if (resulting < min.quantity) {
         return min;
      } else {
         return Power.milliwatt(resulting);
      }
   }

   public boolean isNegative() {
      return quantity < 0;
   }
}
