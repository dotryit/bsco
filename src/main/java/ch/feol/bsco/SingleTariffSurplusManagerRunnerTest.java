package ch.feol.bsco;

import java.util.Locale;
import java.util.Locale.Category;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.feol.bsco.boiler.SurplusManagedBoilers;
import ch.feol.bsco.clock.Clock;
import ch.feol.bsco.clock.RealClock;
import ch.feol.bsco.quantity.Power;
import ch.feol.bsco.solar.EnergySystem;
import ch.feol.bsco.solar.SolarLogEnergySystem;

public class SingleTariffSurplusManagerRunnerTest {

   private static final Logger LOG;

   static {
      System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
      LOG = LoggerFactory.getLogger(SingleTariffSurplusManagerRunnerTest.class);
   }

   public static void main(String[] args) {
      Locale.setDefault(Category.FORMAT, Locale.ENGLISH);
      Clock clock = new RealClock(Configuration.CHECK_INTERVAL);
      EnergySystem energySystem = new SolarLogEnergySystem(Configuration.REMOTE_SOLARLOG);
      SurplusManagedBoilers boilers = new SurplusManagedBoilers(clock, true, false);
      LOG.info("Starting single tariff surplus manager test");
      try {
         while (!clock.isExpired()) {
            Power netto = energySystem.getNettoPower();
            LOG.debug("The netto power of the energy system is {}", netto);
            boilers.manage(netto);
            clock.next();
         }
      } catch (RuntimeException e) {
         LOG.error("Single tariff surplus manager test failed", e);
      }
      LOG.info("Single tariff surplus manager test ended");
   }
}
