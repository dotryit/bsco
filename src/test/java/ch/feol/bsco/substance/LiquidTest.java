package ch.feol.bsco.substance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.feol.bsco.quantity.Energy;
import ch.feol.bsco.quantity.Temperature;
import ch.feol.bsco.quantity.Volume;;

class LiquidTest {

   @Test
   void test_getThermalCapacity() {
      assertEquals(4.182, Liquid.WATER.getThermalCapacity());
   }
   
   @Test
   void test_Heat_Volume_Energy() {
      // Arrange
      Volume empty = Volume.none();
      Energy megajoule = Energy.megajoule(1);
      Liquid testee = Liquid.WATER;

      // Act & assert
      assertEquals(Temperature.kelvin(1), testee.heat(Volume.liter(1), Energy.joule(4182)));
      assertEquals(Temperature.kelvin(0), testee.heat(Volume.liter(1), Energy.joule(0)));
      assertEquals(Temperature.kelvin(239.12), testee.heat(Volume.liter(1), megajoule));
      assertThrows(IllegalStateException.class, () -> testee.heat(empty, megajoule));
   }

   @Test
   void test_Heat_Volume_Temperatur() {
      // Arrange
      Liquid testee = Liquid.WATER;
      // Act & assert
      assertEquals(Energy.joule(4182), testee.heat(Volume.liter(1), 1));
      assertEquals(Energy.joule(0), testee.heat(Volume.liter(0), 1000));
      assertEquals(Energy.joule(0), testee.heat(Volume.liter(98765.4321), 0));
   }
}
