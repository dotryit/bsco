package ch.feol.bsco.solar;

import java.time.LocalDateTime;

import ch.feol.bsco.quantity.Power;

public class Data {

   private LocalDateTime lastUpdate;

   private Power productionPowerAc;

   private Power productionPowerDc;

   private Power consumptionPowerAc;

   public Data(LocalDateTime lastUpdate, Power productionPowerAc, Power productionPowerDc, Power consumptionPowerAc) {
      this.lastUpdate = lastUpdate;
      this.productionPowerAc = productionPowerAc;
      this.productionPowerDc = productionPowerDc;
      this.consumptionPowerAc = consumptionPowerAc;
   }

   public LocalDateTime getLastUpdate() {
      return lastUpdate;
   }

   public Power getProductionPower() {
      return productionPowerAc;
   }

   public Power getConsumptionPower() {
      return consumptionPowerAc;
   }

   @Override
   public String toString() {
      return this.getClass().getSimpleName() + //
            "(lastUpdate=" + lastUpdate + //
            ", productionPowerDc=" + productionPowerDc + //
            ", productionPowerAc=" + productionPowerAc + //
            ", consumptionPowerAc=" + consumptionPowerAc + ")";
   }

   public Power getProductionPowerAc() {
      return productionPowerAc;
   }

   public Power getProductionPowerDc() {
      return productionPowerDc;
   }

   public Power getConsumptionPowerAc() {
      return consumptionPowerAc;
   }

}
