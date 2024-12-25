package ch.feol.bsco.solar;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.feol.bsco.quantity.Power;

/**
 * In a separate thread and in intervals defined by the {@link Stepper} the {@link SolarLogReader} reads surplus data from the SolarLog device and provides the mean value averaged
 * over the last {@value SolarLogReader#NUMBER_OF_AVERAGED_VALUES} values.
 */
public class SolarLogReader extends Thread {

   private static final Logger LOG = LoggerFactory.getLogger(SolarLogReader.class);

   private final List<Power> productionValues = new ArrayList<>();

   private final List<Power> consumptionValues = new ArrayList<>();

   private final SolarLog solarLog;

   private final Stepper stepper;

   private final int averagedValues;

   public SolarLogReader(int averagedValues, SolarLog solarLog, Stepper stepper) {
      this.averagedValues = averagedValues;
      this.solarLog = solarLog;
      this.stepper = stepper;
   }

   public Power getAverageSurplusPower() {
      return getAverageProductionPower().minus(getAverageConsumptionPower(), Power.none());
   }

   public Power getAverageNettoPower() {
      return getAverageProductionPower().minus(getAverageConsumptionPower());
   }

   public Power getAverageProductionPower() {
      return getAverageValues(productionValues);
   }

   public Power getAverageConsumptionPower() {
      return getAverageValues(consumptionValues);
   }

   private static Power getAverageValues(List<Power> powerValues) {
      Power surplus = Power.none();
      synchronized (powerValues) {
         for (Power value : powerValues) {
            surplus = surplus.plus(value);
         }
         if (powerValues.isEmpty()) {
            return Power.none();
         } else {
            surplus = surplus.divide(powerValues.size());
         }
      }
      return surplus;
   }

   @Override
   public void run() {
      boolean next = true;
      while (next) {
         Power newProduction;
         Power newConsumption;
         try {
            Data data = solarLog.read();
            newConsumption = data.getConsumptionPowerAc();
            newProduction = data.getProductionPowerAc();
         } catch (SolarLogException e) {
            newConsumption = Power.none();
            newProduction = Power.none();
            LOG.error("Reading SolarLog failed", e);
         }
         synchronized (productionValues) {
            productionValues.add(newProduction);
            while (productionValues.size() > averagedValues) {
               productionValues.remove(0);
            }
         }
         synchronized (consumptionValues) {
            consumptionValues.add(newConsumption);
            while (consumptionValues.size() > averagedValues) {
               consumptionValues.remove(0);
            }
         }
         next = stepper.next();
      }
   }
}
