package ch.feol.bsco;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Locale.Category;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.feol.bsco.boiler.TimeManagedBoilers;
import ch.feol.bsco.clock.Clock;
import ch.feol.bsco.clock.StartEndSimulatedClock;

public class SingleTariffDaytimeManagerSimulator {

   private static final Logger LOG;

   static {
      System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "info");
      LOG = LoggerFactory.getLogger(SingleTariffDaytimeManagerSimulator.class);
   }

   public static void main(String[] args) {
      Locale.setDefault(Category.FORMAT, Locale.ENGLISH);
      LocalDateTime now = LocalDateTime.now();
      Clock clock = new StartEndSimulatedClock(now, now.plusYears(10), Configuration.CHECK_INTERVAL, 1000);
      TimeManagedBoilers boilers = new TimeManagedBoilers(clock, false, false);
      LOG.info("Starting single tariff daytime manager simulation");
      try {
         while (!clock.isExpired()) {
            boilers.manage();
            clock.next();
         }
      } catch (RuntimeException e) {
         LOG.error("Single tariff daytime manager simulation failed", e);
      }
      LOG.info("Single tariff daytime manager simulation ended");
   }
}
