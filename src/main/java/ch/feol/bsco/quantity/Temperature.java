package ch.feol.bsco.quantity;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Temperature to the precision of millikelvin.
 */
public class Temperature extends AbstractQuantity {

   private static final double ABSOLUTE_ZERO = 273.15;

   static AbstractQuantity.Scale scale = new AbstractQuantity.Scale("mK", "K");

   protected AbstractQuantity.Scale getScale() {
      return scale;
   }

   private Temperature(long millikelvin) {
      super(millikelvin);
   }

   private Temperature(double millikelvin) {
      this(Math.round(millikelvin));
   }

   @Override
   public String toString() {
      return NumberFormat.getInstance(Locale.ENGLISH).format((quantity / 1000d) - ABSOLUTE_ZERO) + "°C";
   }

   public Temperature add(Temperature offset) {
      return new Temperature(quantity + offset.quantity);
   }

   public static Temperature celsius(double celsius) {
      return new Temperature((celsius + ABSOLUTE_ZERO) * 1000);
   }

   public static Temperature kelvin(double kelvin) {
      return new Temperature(kelvin * 1000);
   }

   public static Temperature millikelvin(double millikelvin) {
      return new Temperature(millikelvin);
   }

   public boolean above(Temperature other) {
      return quantity > other.quantity;
   }

   public boolean below(Temperature other) {
      return quantity < other.quantity;
   }
}
