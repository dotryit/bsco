package ch.feol.bsco.solar;

import ch.feol.bsco.quantity.Power;

public interface EnergySystem {

   /**
    * @return the surplus power. The value will never be negative.
    */
   public Power getSurplusPower();

   /**
    * @return the netto power. The value will be positive if production exceeds consumption.
    */
   public Power getNettoPower();
}
