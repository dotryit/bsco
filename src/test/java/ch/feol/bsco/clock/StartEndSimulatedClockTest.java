package ch.feol.bsco.clock;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

class StartEndSimulatedClockTest {

   @Test
   void test() {
      // Arrange
      StartEndSimulatedClock testee = new StartEndSimulatedClock(LocalDateTime.of(2024, 12, 15, 10, 23), LocalDateTime.of(2024, 12, 15, 10, 35), Duration.of(5, ChronoUnit.MINUTES), 0);
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
