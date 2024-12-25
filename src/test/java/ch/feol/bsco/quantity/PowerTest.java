package ch.feol.bsco.quantity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

class PowerTest {

   Power oneWatt = Power.watt(1);

   Power zeroWatt = Power.none();

   Power oneKilowatt = Power.kilowatt(1);

   Power oneMegawatt = Power.megawatt(1);

   Power oneGigawatt = Power.gigawatt(1);

   @Test
   void test_minus_max() {
      assertEquals(Power.none(), Power.watt(10).minus(Power.watt(20), Power.none()));
      assertEquals(Power.watt(-20), Power.watt(10).minus(Power.watt(30), Power.watt(-30)));

   }

   @Test
   void test_minus() {
      assertEquals(Power.milliwatt(9990), Power.watt(10).minus(Power.milliwatt(10)));
      assertTrue(Power.watt(10).minus(Power.watt(20)).isNegative());
   }

   @Test
   void test_multipy() {
      assertEquals(Power.watt(10), oneWatt.multiply(10));
      assertEquals(Power.watt(10), oneWatt.multiply(10.0));
   }

   @Test
   void test_plus() {
      assertEquals(Power.watt(1001), oneKilowatt.plus(oneWatt));
   }

   @Test
   void test_isLessThan() {
      assertTrue(oneWatt.isLessThan(oneKilowatt));
      assertFalse(oneMegawatt.isLessThan(zeroWatt));
      assertFalse(oneGigawatt.isLessThan(oneGigawatt));
   }

   @Test
   void test_divide() {
      assertEquals(Power.watt(100), oneKilowatt.divide(10));
   }

   @Test
   void test_divideByZero() {
      assertThrows(IllegalArgumentException.class, () -> oneKilowatt.divide(0));
   }

   @Test
   void test_getMilliWatt() {
      assertEquals(1000, oneWatt.getQuantity());
   }

   @Test
   void test_hashCode() {
      assertEquals(oneMegawatt.hashCode(), oneMegawatt.hashCode());
      assertNotEquals(oneMegawatt.hashCode(), oneGigawatt.hashCode());
   }

   @Test
   void test_equals() {
      // Act & assert
      assertEquals(oneKilowatt, Power.watt(1000));
      assertEquals(oneWatt, Power.milliwatt(1000));
      assertEquals(oneKilowatt, Power.milliwatt(1e6));
      assertEquals(oneMegawatt, Power.milliwatt(1e9));
      assertEquals(oneGigawatt, Power.milliwatt(1e12));
      assertEquals(zeroWatt, Power.gigawatt(0));

      assertEquals(oneWatt, Power.watt(1.0001));
      assertEquals(oneKilowatt, Power.kilowatt(0.9999));
      assertEquals(oneMegawatt, Power.megawatt(1.0001));
      assertEquals(oneGigawatt, Power.gigawatt(0.9999));

      assertNotEquals(Power.kilowatt(3.086), Power.kilowatt(3.288));

      assertNotEquals(zeroWatt, Power.gigawatt(1));
      assertFalse(zeroWatt.equals(null));
      assertFalse(zeroWatt.equals(new Object()));
      assertTrue(oneMegawatt.equals(oneMegawatt));
   }

   @Test
   void test_toString() {
      // Act & assert
      assertEquals("987 mW", Power.milliwatt(987).toString());
      assertEquals("9.876 W", Power.milliwatt(9876).toString());
      assertEquals("98.765 W", Power.milliwatt(98765).toString());
      assertEquals("987.654 W", Power.milliwatt(987654).toString());
      assertEquals("9.877 kW", Power.milliwatt(9876543).toString());
      assertEquals("98.765 kW", Power.milliwatt(98765432).toString());
      assertEquals("987.654 kW", Power.milliwatt(987654321).toString());
      assertEquals("9.877 MW", Power.milliwatt(9876543210l).toString());
      assertEquals("98.765 MW", Power.milliwatt(98765432100l).toString());
      assertEquals("987.654 MW", Power.milliwatt(987654321000l).toString());
      assertEquals("9.877 GW", Power.milliwatt(9876543210000l).toString());
      assertEquals("98.765 GW", Power.milliwatt(98765432100000l).toString());
      assertEquals("987.654 GW", Power.milliwatt(987654321000000l).toString());
      assertEquals("9876.543 GW", Power.milliwatt(9876543210000000l).toString());
      assertEquals("98765.432 GW", Power.milliwatt(98765432100000000l).toString());
      assertEquals("987654.321 GW", Power.milliwatt(987654321000000000l).toString());
   }

   @Test
   void test_printAndParse_throwsException() {
      // Act & assert
      assertThrows(IllegalArgumentException.class, () -> Power.parse("gaga"));
   }

   @Test
   void test_printAndParseMilli() {
      // Arrange
      Power input = Power.milliwatt(1228);

      // Act
      Power result = Power.parse(input.toString());

      // Assert
      assertEquals(input, result);
   }

   @Test
   void test_printAndParse() {
      // Arrange
      Power input = Power.watt(127);

      // Act
      Power result = Power.parse(input.toString());

      // Assert
      assertEquals(input, result);
   }

   @Test
   void test_printAndParseKilo() {
      // Arrange
      Power input = Power.watt(12080);

      // Act
      Power result = Power.parse(input.toString());

      // Assert
      assertEquals(input, result);
   }

   @Test
   void test_printAndParseMega() {
      // Arrange
      Power input = Power.megawatt(345.6);

      // Act
      Power result = Power.parse(input.toString());

      // Assert
      assertEquals(input, result);
   }

   @Test
   void test_printAndParseGiga() {
      // Arrange
      Power input = Power.gigawatt(74088);

      // Act
      Power result = Power.parse(input.toString());

      // Assert
      assertEquals(input, result);
   }

   @Test
   void test_consume_nanos() {
      // Arrange
      Power testee = Power.kilowatt(1000);

      // Act
      Energy energy = testee.consume(Duration.of(1000, ChronoUnit.NANOS));

      // Assert
      assertEquals(Energy.joule(1), energy);
   }

   @Test
   void test_consume_seconds() {
      // Arrange
      Power testee = Power.kilowatt(10);

      // Act
      Energy energy = testee.consume(Duration.of(10, ChronoUnit.SECONDS));

      // Assert
      assertEquals(Energy.kilojoule(100), energy);
   }
}
