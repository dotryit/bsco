package ch.feol.bsco.quantity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

import org.junit.jupiter.api.Test;

class PowerTest {

   @Test
   void testPlus() {
      assertEquals(Power.milliwatt(111), Power.milliwatt(29).plus(Power.milliwatt(82)));
   }

   @Test
   void testConsume() {
      assertEquals(Energy.kilojoule(5), Power.megawatt(5).consume(Duration.ofMillis(1)));
   }

   @Test
   void testMilliwattDouble() {
      assertEquals(Power.kilowatt(5), Power.kilowatt(5.0));
   }

   @Test
   void testMilliwattLong() {
      assertEquals(Power.watt(1.2), Power.milliwatt(1200));
   }

   @Test
   void testWattDouble() {
      assertEquals(Power.milliwatt(625), Power.watt(0.625));
   }

   @Test
   void testWattLong() {
      assertEquals(Power.kilowatt(1.33), Power.watt(1330));
   }

   @Test
   void testKilowattDouble() {
      assertEquals(Power.watt(1234.5), Power.kilowatt(1.2345));
   }
   @Test
   void testMultiplyDouble() {
      assertEquals(Power.watt(13), Power.watt(10).multiply(1.3));
   }

   @Test
   void testMultiplyLong() {
      assertEquals(Power.watt(33), Power.watt(11).multiply(3));
   }

   @Test
   void testIsLessThan() {
      assertTrue(Power.watt(9).isLessThan(Power.watt(10)));
   }

   @Test
   void testIsMoreThan() {
      assertTrue(Power.watt(19).isMoreThan(Power.milliwatt(18999)));
   }

   @Test
   void testNone() {
      assertEquals(0.0d, Power.none().getKilowatt());
   }

   @Test
   void testDivide() {
      assertEquals(Power.watt(1.5), Power.watt(3).divide(2));
   }

   @Test
   void testParse() {
      assertEquals(Power.gigawatt(3), Power.parse(Power.gigawatt(3).toString()));
   }

   @Test
   void testMinusPower() {
      assertEquals(Power.none(), Power.watt(100).minus(Power.watt(100)));
   }

   @Test
   void testMinusPowerPower() {
      assertEquals(Power.none(), Power.watt(10).minus(Power.watt(100), Power.none()));
   }

   @Test
   void testIsNegative() {
      assertTrue(Power.milliwatt(-1).isNegative());
   }

   @Test
   void testGetKilowatt() {
      assertEquals(3.141593, Power.watt(3141.5926).getKilowatt());
   }
}
