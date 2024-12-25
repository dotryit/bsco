package ch.feol.bsco.solar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import ch.feol.bsco.clock.Clock;
import ch.feol.bsco.quantity.Power;

class MeasuredSimulationTest {

   @Test
   void test_december2024() {
      MeasuredSimulation testee = new MeasuredSimulation("20241218-20250101.txt", 0);
      assertEquals(82132, testee.measurement.points.size());
      Clock clock = testee.clock;
      assertEquals(LocalDateTime.of(2024, 12, 18, 0, 0, 34, 485268300), clock.now());

      for (int i = 0; i < 116; i++) {
         clock.next();
      }
      assertEquals(LocalDateTime.of(2024, 12, 18, 9, 40, 34, 485268300), clock.now());
      assertEquals(Power.watt(3856), testee.getEnergySystem().getNettoPower());
      assertEquals(Power.watt(3856), testee.getEnergySystem().getSurplusPower());

      for (int i = 0; i < 1116; i++) {
         clock.next();
      }
      assertEquals(LocalDateTime.of(2024, 12, 22, 6, 40, 34, 485268300), clock.now());
      assertEquals(Power.watt(-3963), testee.getEnergySystem().getNettoPower());
      assertEquals(Power.none(), testee.getEnergySystem().getSurplusPower());

      for (int i = 0; i < 864; i++) {
         clock.next();
      }
      assertEquals(LocalDateTime.of(2024, 12, 25, 6, 40, 34, 485268300), clock.now());
      assertEquals(Power.watt(-6261), testee.getEnergySystem().getNettoPower());
      assertEquals(Power.none(), testee.getEnergySystem().getSurplusPower());

      int ticks = 0;
      while (!clock.isExpired()) {
         clock.next();
         ticks++;
      }
      assertEquals(2165, ticks);
      assertEquals(LocalDateTime.of(2025, 1, 1, 19, 5, 34, 485268300), clock.now());
      assertEquals(Power.none(), testee.getEnergySystem().getNettoPower());
      assertEquals(Power.none(), testee.getEnergySystem().getSurplusPower());
   }
}
