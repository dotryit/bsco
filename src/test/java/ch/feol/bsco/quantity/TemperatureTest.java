package ch.feol.bsco.quantity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TemperatureTest {

   @Test
   void test_above() {
      assertTrue(Temperature.celsius(22.5).above(Temperature.kelvin(100.0)));
      assertFalse(Temperature.celsius(22.5).above(Temperature.celsius(22.6)));
   }

   @Test
   void test_below() {
      assertTrue(Temperature.celsius(22.5).below(Temperature.celsius(22.6)));
      assertFalse(Temperature.celsius(22.6).below(Temperature.celsius(22.6)));
   }

   @Test
   void test_add() {
      assertEquals(Temperature.celsius(100), Temperature.celsius(10).add(Temperature.kelvin(90)));
   }

   @Test
   void test_toString() {
      assertEquals("0°C", Temperature.celsius(0).toString());
      assertEquals("-273.15°C", Temperature.kelvin(0).toString());
   }
}
