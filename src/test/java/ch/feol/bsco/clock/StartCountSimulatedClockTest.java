package ch.feol.bsco.clock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

class StartCountSimulatedClockTest {

   @Test
   void test_midnight() {
      // Arrange
      StartCountSimulatedClock testee = new StartCountSimulatedClock(LocalDateTime.of(2024, 12, 15, 14, 10), 24 * 60, Duration.of(1, ChronoUnit.MINUTES));
      // Act & Assert
      for (int count = 0; count < 10 * 60; count++) {
         testee.next();
      }
      assertEquals(LocalDateTime.of(2024, 12, 16, 0, 10), testee.now());
      assertFalse(testee.isExpired());

      int count = 0;
      while (!testee.isExpired()) {
         testee.next();
         count++;
      }
      assertEquals(14 * 60, count);
   }

   @Test
   void test() {
      // Arrange
      StartCountSimulatedClock testee = new StartCountSimulatedClock(LocalDateTime.of(2024, 12, 15, 10, 23), 3, Duration.of(5, ChronoUnit.MINUTES));
      // Act & Assert
      assertEquals(LocalDateTime.of(2024, 12, 15, 10, 23), testee.now());
      testee.next();
      assertEquals(LocalDateTime.of(2024, 12, 15, 10, 28), testee.now());
      testee.next();
      assertEquals(LocalDateTime.of(2024, 12, 15, 10, 33), testee.now());
      testee.next();
      assertTrue(testee.isExpired());
   }
}
