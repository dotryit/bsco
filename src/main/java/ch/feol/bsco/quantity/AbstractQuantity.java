package ch.feol.bsco.quantity;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.stream.IntStream;

public abstract class AbstractQuantity {

   public static class Scale {

      private NumberFormat df = new DecimalFormat("###############.###", new DecimalFormatSymbols(Locale.ENGLISH));

      private static final long[] POWERS_OF_THOUSAND = IntStream.range(0, 7).mapToLong(i -> (long) Math.pow(10, i * 3)).toArray();

      private String[] units;

      public Scale(String... units) {
         this.units = units;
      }

      String format(long quantity) {
         final long absoluteQuantity;
         if (quantity < 0) {
            absoluteQuantity = -quantity;
         } else {
            absoluteQuantity = quantity;
         }
         for (int i = units.length - 1; i > 0; i--) {
            double scale = POWERS_OF_THOUSAND[i];
            if (absoluteQuantity >= scale) {
               return df.format(quantity / scale) + " " + units[i];
            }
         }
         return df.format(quantity) + " " + units[0];
      }

      public boolean equals(long quantity1, long quantity2) {
         for (int i = POWERS_OF_THOUSAND.length - 2; i > 0; i--) {
            double scale = POWERS_OF_THOUSAND[i + 1];
            if (quantity1 >= scale) {
               double divisor = POWERS_OF_THOUSAND[i];
               long q1 = Math.round(quantity1 / divisor);
               long q2 = Math.round(quantity2 / divisor);
               return q1 == q2;
            }
         }
         return quantity1 == quantity2;
      }

      public long parse(String input) {
         int maxLength = getMaxLength();
         for (int l = maxLength; l > 0; l--) {
            for (int i = units.length - 1; i >= 0; i--) {
               if (units[i].length() == l && input.endsWith(units[i])) {
                  try {
                     double scale = POWERS_OF_THOUSAND[i];
                     double value = Double.parseDouble(input.substring(0, input.length() - units[i].length()));
                     return Math.round(value * scale);
                  } catch (Exception e) {
                     // Continue and try other lengths
                  }
               }
            }
         }
         throw new IllegalArgumentException("Illegal power value " + input);
      }

      protected int getMaxLength() {
         int maxLength = 0;
         for (int i = 0; i < units.length; i++) {
            if (units[i].length() > maxLength) {
               maxLength = units[i].length();
            }
         }
         return maxLength;
      }
   }

   final long quantity;

   protected abstract AbstractQuantity.Scale getScale();

   protected AbstractQuantity(long quantity) {
      this.quantity = quantity;
   }

   @Override
   public String toString() {
      return getScale().format(quantity);
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + (int) (quantity ^ (quantity >>> 32));
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
      AbstractQuantity other = (AbstractQuantity) obj;
      return getScale().equals(quantity, other.quantity);
   }

   public long getQuantity() {
      return quantity;
   }
}
