package ch.feol.bsco;

import java.util.Locale;
import java.util.Locale.Category;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.feol.bsco.boiler.SurplusManagedBoilers;
import ch.feol.bsco.clock.Clock;
import ch.feol.bsco.quantity.Power;
import ch.feol.bsco.solar.EnergySystem;
import ch.feol.bsco.solar.MeasuredSimulation;

public class SingleTariffSurplusManagerSimulator {

   private static final Logger LOG;

   static {
      System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "info");
      LOG = LoggerFactory.getLogger(SingleTariffSurplusManagerSimulator.class);
   }

   public static void main(String[] args) {
      Locale.setDefault(Category.FORMAT, Locale.ENGLISH);
      MeasuredSimulation simulation = new MeasuredSimulation("20241218-20250101.txt", 1000);
      Clock clock = simulation.getClock();
      EnergySystem energySystem = simulation.getEnergySystem();
      SurplusManagedBoilers boilers = new SurplusManagedBoilers(clock, false, true);
      LOG.info("Starting single tariff surplus manager simulation at {}", clock.now());
      try {
         while (!clock.isExpired()) {
            Power netto = energySystem.getNettoPower();
            LOG.debug("The netto power of the energy system is {}", netto);
            boilers.manage(netto);
            clock.next();
         }
      } catch (RuntimeException e) {
         LOG.error("Single tariff surplus manager simulation failed", e);
      }
      LOG.info("Single tariff surplus manager simulation ended at {}", clock.now());
   }
}
