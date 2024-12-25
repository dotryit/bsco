package ch.feol.bsco.pi.board;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DummyRelayTest {

   @Test
   void test_connect() {
      DummyRelay testee = new DummyRelay(7);
      assertTrue(testee.connectorA());
      assertFalse(testee.connectorA());
      assertFalse(testee.connectorA());
      assertTrue(testee.connectorB());
      assertFalse(testee.connectorB());
      assertFalse(testee.connectorB());
   }

   @Test
   void test_getRelayNumber() {
      DummyRelay testee = new DummyRelay(7);
      assertEquals(7, testee.getRelayNumber());
   }
}
