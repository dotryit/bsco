package ch.feol.bsco.quantity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.feol.bsco.substance.Liquid;

class VolumeTest {

   Volume emptyVolume = Volume.liter(0);

   Volume oneLiter = Volume.liter(1);

   Volume twoLiters = Volume.liter(2.0);

   Volume tenLiters = Volume.liter(10);

   Volume oneSquareMeter = Volume.squareMeter(1);

   Volume twoSquareMeters = Volume.squareMeter(2);

   @Test
   void test_divide() {
      assertEquals(Volume.milliliter(13), Volume.liter(13).divide(1000));
      assertEquals(Volume.empty(), Volume.liter(0.1).divide(1000));
      assertEquals(Volume.empty(), Volume.empty().divide(100_000));
   }

   @Test
   void test_isEmpty() {
      assertTrue(Volume.milliliter(0).isEmpty());
      assertFalse(Volume.milliliter(1).isEmpty());
   }

   @Test
   void test_mix() {
      assertEquals(Temperature.celsius(10), Volume.mix(oneLiter, Temperature.celsius(10), tenLiters, Temperature.celsius(10)));
      assertEquals(Temperature.celsius(15), Volume.mix(tenLiters, Temperature.celsius(10), tenLiters, Temperature.celsius(20)));
      assertEquals(Temperature.celsius(19.09), Volume.mix(oneLiter, Temperature.celsius(10), tenLiters, Temperature.celsius(20)));
   }

   @Test
   void test_subtract() {
      assertEquals(Volume.milliliter(250), oneLiter.subtract(Volume.milliliter(750)));
      assertEquals(Volume.none(), tenLiters.subtract(Volume.liter(11)));
   }

   @Test
   void test_toString() {
      assertEquals("10 l", tenLiters.toString());
      assertEquals("0 ml", emptyVolume.toString());
      assertEquals("1 ml", Volume.milliliter(1).toString());
      assertEquals("2.222 m2", Volume.liter(2222).toString());
   }

   @Test
   void test_multiply() {
      assertEquals(tenLiters, twoLiters.multiply(5));
      assertEquals(Volume.liter(0), twoLiters.multiply(0));
      assertEquals(Volume.liter(0), emptyVolume.multiply(1000));
      assertEquals(Volume.liter(75), tenLiters.multiply(7.5));
   }

   @Test
   void test_heat() {
      // Arrange
      Volume testee = Volume.liter(800);

      // Act
      Energy result = testee.heat(Liquid.WATER, 50);

      // Assert
      assertEquals(Energy.kilojoule(167280), result);
   }

   @Test
   void test_add() {
      assertEquals(Volume.liter(100), tenLiters.add(Volume.liter(90)));
      assertEquals(Volume.liter(50.0), Volume.liter(61).add(Volume.liter(-11.0)));
   }

   @Test
   void test_getMilliliter() {
      assertEquals(2620, Volume.liter(2.62).getQuantity());
   }

   @Test
   void test_hashCode() {
      assertEquals(oneLiter.hashCode(), oneLiter.hashCode());
      assertNotEquals(oneLiter.hashCode(), emptyVolume.hashCode());
   }

   @Test
   void test_equals() {
      Volume testee = oneLiter;
      // Act & assert
      assertEquals(Volume.liter(1.0001), testee);
      assertNotEquals(Volume.liter(1.001), testee);
      assertEquals(Volume.liter(0), Volume.liter(0.0));
      assertEquals(Volume.liter(-1), Volume.liter(-1.0));
      assertEquals(testee, testee);
      assertNotEquals(null, testee);
      assertNotEquals(new Object(), testee);
      assertFalse(oneLiter.equals(null));
      assertFalse(emptyVolume.equals(new Object()));
      assertTrue(oneSquareMeter.equals(oneSquareMeter));

   }
}
