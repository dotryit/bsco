package ch.feol.bsco.pi.board;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DummyRelay implements Relay {

   private static final Logger LOG = LoggerFactory.getLogger(DummyRelay.class);

   private boolean isA;

   private int relayNumber;

   public DummyRelay(int relayNumber) {
      this.relayNumber = relayNumber;
   }

   @Override
   public boolean connectorA() {
      if (!isA) {
         isA = true;
         LOG.debug("Turning dummy relay to connector A");
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean connectorB() {
      if (isA) {
         isA = false;
         LOG.debug("Turning dummy relay to connector B");
         return true;
      } else {
         return false;
      }
   }

   @Override
   public int getRelayNumber() {
      return relayNumber;
   }
}
