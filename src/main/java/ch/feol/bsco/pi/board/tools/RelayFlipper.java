package ch.feol.bsco.pi.board.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.feol.bsco.pi.Pi4jProvider;
import ch.feol.bsco.pi.board.RealRelay;

public class RelayFlipper {

   private static final Logger LOG = LoggerFactory.getLogger(RelayFlipper.class);

   public static void main(String[] args) throws InterruptedException {
      LOG.info("Hello, this is RelayFlipper");

      RealRelay[] relays = RealRelay.values();

      for (int i = 0; i < relays.length; i++) {
         RealRelay relay = relays[i];
         for (int l = 0; l < 3; l++) {
            relay.connectorB(); // LED off
            Thread.sleep(200);
            relay.connectorA(); // LED on
            Thread.sleep(200);
         }
      }
      // All LED's are on now

      Thread.sleep(500);

      for (int i = 0; i < relays.length; i++) {
         RealRelay relay = relays[i];
         relay.connectorB(); // LED off
      }
      // All LED's are off now

      Thread.sleep(500);

      for (int i = relays.length - 1; i >= 0; i--) {
         RealRelay relay = relays[i];
         relay.connectorA();
         Thread.sleep(200);
      }
      // All LED's are on now

      for (int i = 0; i < relays.length; i++) {
         RealRelay relay = relays[i];
         relay.connectorB();
      }
      // All LED's are off now

      Thread.sleep(500);

      Pi4jProvider.shutdown();
   }
}
