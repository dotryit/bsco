package ch.feol.bsco.quantity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.Test;

class EnergyTest {

   Energy zeroJoule = Energy.none();

   Energy oneMilliJoule = Energy.millijoule(1);

   Energy twoMillijoules = Energy.millijoule(2.0);

   Energy oneJoule = Energy.joule(1);

   Energy twoJoules = Energy.joule(2.0);

   Energy oneKilojoule = Energy.kilojoule(1);

   Energy twoKilojoules = Energy.kilojoule(2.0);

   Energy oneMegajoule = Energy.megajoule(1);

   Energy twoMegajoules = Energy.megajoule(2.0);

   Energy oneGigajoule = Energy.gigajoule(1);

   Energy twoGigajoules = Energy.gigajoule(2.0);

   @Test
   void test_add() {
      assertEquals(oneMegajoule, oneMegajoule.add(Energy.none()));
      assertEquals(twoGigajoules, oneGigajoule.add(oneGigajoule));
   }

   @Test
   void test_getMillijoule() {
      assertEquals(1, Energy.millijoule(1).getQuantity());
      assertEquals(1000, Energy.joule(1).getQuantity());
   }

   @Test
   void test_equals() {
      // Act & assert
      assertEquals(oneKilojoule, Energy.joule(1000));
      assertEquals(oneJoule, Energy.millijoule(1000));
      assertEquals(oneKilojoule, Energy.millijoule(1e6));
      assertEquals(oneMegajoule, Energy.millijoule(1e9));
      assertEquals(oneGigajoule, Energy.millijoule(1e12));
      assertEquals(zeroJoule, Energy.gigajoule(0));
      assertNotEquals(zeroJoule, Energy.gigajoule(1));

      assertFalse(zeroJoule.equals(null));
      assertFalse(zeroJoule.equals(new Object()));
      assertTrue(oneMegajoule.equals(oneMegajoule));
   }

   @Test
   void test_consume_1() {
      // Arrange
      Energy testee = Energy.kilojoule(1);
      // Act
      Duration duration = testee.consume(Power.kilowatt(1));
      // Assert
      assertEquals(Duration.ofSeconds(1), duration);
   }

   @Test
   void test_consume_2() {
      // Arrange
      Energy testee = Energy.joule(1);
      // Act
      Duration duration = testee.consume(Power.kilowatt(1000));
      // Assert
      assertEquals(Duration.ofNanos(1000), duration);
   }

   @Test
   void test_consume_3() {
      // Arrange
      Energy testee = Energy.kilojoule(1000);
      // Act
      Duration duration = testee.consume(Power.watt(1));
      // Assert
      assertEquals(Duration.ofSeconds(1000000), duration);
   }

   @Test
   void test_hashCode() {
      assertEquals(oneMegajoule.hashCode(), oneMegajoule.hashCode());
      assertNotEquals(oneMegajoule.hashCode(), oneGigajoule.hashCode());
   }

   @Test
   void test_toString() {
      assertEquals("0 mJ", zeroJoule.toString());
      assertEquals("1 J", oneJoule.toString());
      assertEquals("2 J", twoJoules.toString());
      assertEquals("1 kJ", oneKilojoule.toString());
      assertEquals("2 kJ", twoKilojoules.toString());
      assertEquals("1 MJ", oneMegajoule.toString());
      assertEquals("2 MJ", twoMegajoules.toString());
      assertEquals("1 GJ", oneGigajoule.toString());
      assertEquals("2 GJ", twoGigajoules.toString());
   }
}
