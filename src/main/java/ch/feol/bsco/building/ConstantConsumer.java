package ch.feol.bsco.building;

import ch.feol.bsco.quantity.Power;

public class ConstantConsumer implements EnergyConsumer {

   private final Power consumption;

   public ConstantConsumer(Power consumption) {
      this.consumption = consumption;
   }

   @Override
   public Power getActualConsumption() {
      return consumption;
   }

}
