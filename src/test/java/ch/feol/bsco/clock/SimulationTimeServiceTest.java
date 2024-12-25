package ch.feol.bsco.clock;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

public class SimulationTimeServiceTest {

   @Test
   void test_sleepMinutes() {

      // Arrange
      LocalDateTime now = LocalDateTime.now();
      SimulationTimeService testee = new SimulationTimeService(now);

      // Act
      testee.sleepMinutes(10);

      // Assert
      assertEquals(now.plus(10, ChronoUnit.MINUTES), testee.getActualTimestamp());
   }
}
