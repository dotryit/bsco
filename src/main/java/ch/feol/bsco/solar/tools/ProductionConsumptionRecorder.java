package ch.feol.bsco.solar.tools;

import java.time.LocalDateTime;

import ch.feol.bsco.Configuration;
import ch.feol.bsco.quantity.Power;
import ch.feol.bsco.solar.SolarLog;
import ch.feol.bsco.solar.SolarLogReader;
import ch.feol.bsco.solar.SolarLogStepper;

/**
 * The {@link ProductionConsumptionRecorder} records the production values to standard out.
 */
public class ProductionConsumptionRecorder {

   public static void main(String[] args) throws InterruptedException {
      SolarLog solarLog = new SolarLog(Configuration.getSoloarLogAddress());
      SolarLogStepper stepper = new SolarLogStepper(15);
      SolarLogReader reader = new SolarLogReader(5, solarLog, stepper);
      reader.start();
      Power lastProduction = null;
      Power actualProduction = null;
      Power lastConsumption = null;
      Power actualConsumption = null;
      while (true) {
         actualProduction = reader.getAverageProductionPower();
         actualConsumption = reader.getAverageConsumptionPower();
         if (!actualProduction.equals(lastProduction) || !actualConsumption.equals(lastConsumption)) {
            System.out.println(LocalDateTime.now() + ": " + actualProduction + ", " + actualConsumption);
         }
         lastProduction = actualProduction;
         lastConsumption = actualConsumption;
         Thread.sleep(10000);
      }
   }
}
