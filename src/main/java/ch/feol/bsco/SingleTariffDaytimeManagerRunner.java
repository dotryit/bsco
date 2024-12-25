package ch.feol.bsco;

import java.util.Locale;
import java.util.Locale.Category;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.feol.bsco.boiler.TimeManagedBoilers;
import ch.feol.bsco.clock.Clock;
import ch.feol.bsco.clock.RealClock;

public class SingleTariffDaytimeManagerRunner {

   private static final Logger LOG = LoggerFactory.getLogger(SingleTariffDaytimeManagerRunner.class);

   public static void main(String[] args) {
      Locale.setDefault(Category.FORMAT, Locale.ENGLISH);
      Clock clock = new RealClock(Configuration.CHECK_INTERVAL);
      TimeManagedBoilers boilers = new TimeManagedBoilers(clock, true, false);
      LOG.info("Starting single tariff daytime manager");
      try {
         while (!clock.isExpired()) {
            LOG.debug("Single tariff daytime manager at {}", clock.now());
            boilers.manage();
            clock.next();
         }
      } catch (RuntimeException e) {
         LOG.error("Single tariff daytime manager failed", e);
      }
      LOG.info("Single tariff daytime manager ended");
   }
}
