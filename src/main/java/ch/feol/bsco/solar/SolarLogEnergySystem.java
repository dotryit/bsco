package ch.feol.bsco.solar;

import ch.feol.bsco.building.EnergySystem;
import ch.feol.bsco.quantity.Power;

public class SolarLogEnergySystem implements EnergySystem {

   private final SolarLogReader reader;

   public SolarLogEnergySystem(String address) {
      SolarLog solarLog = new SolarLog(address);
      SolarLogStepper stepper = new SolarLogStepper(15);
      reader = new SolarLogReader(5, solarLog, stepper);
      reader.start();
   }

   @Override
   public Power getSurplusPower() {
      return reader.getAverageSurplusPower();
   }

   @Override
   public Power getNettoPower() {
      return reader.getAverageNettoPower();
   }

}
