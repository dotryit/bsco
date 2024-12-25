package ch.feol.bsco;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ConfigurationTest {

   @Test
   void test_getSoloarLogAddress() {
      assertEquals("pi.feol.ch:9878", Configuration.getSoloarLogAddress());
   }

}
