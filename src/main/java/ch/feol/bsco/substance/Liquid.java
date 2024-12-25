package ch.feol.bsco.substance;

import ch.feol.bsco.quantity.Energy;
import ch.feol.bsco.quantity.Temperature;
import ch.feol.bsco.quantity.Volume;

public enum Liquid {

   WATER(4.182); // 4.182 Joule per gramm kelvin and 1ml = 1g

   private double thermalCapacity;

   /**
    * @param thermalCapacity
    *           The thermal capacity in joule per gram/milliliter kelvin.
    */
   private Liquid(double thermalCapacity) {
      this.thermalCapacity = thermalCapacity;
   }

   /**
    * @return the thermal capacity in joule per gram/milliliter kelvin.
    */
   public double getThermalCapacity() {
      return thermalCapacity;
   }

   public Energy heat(Volume volume, long temperatureDifference) {
      return heat(volume, (double) temperatureDifference);
   }

   public Energy heat(Volume volume, double temperatureDifference) {
      return Energy.joule(volume.getQuantity() * thermalCapacity * temperatureDifference);
   }

   public Temperature heat(Volume volume, Energy energy) {
      if (volume.isEmpty()) {
         throw new IllegalStateException("Volume is empty");
      }
      return Temperature.millikelvin(energy.getQuantity() / (volume.getQuantity() * thermalCapacity));
   }
}
