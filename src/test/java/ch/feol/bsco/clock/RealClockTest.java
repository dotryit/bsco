package ch.feol.bsco.clock;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

public class RealClockTest {

   @Test
   public void test() {

      // Prepare
      Clock testee = new RealClock(Duration.of(2, ChronoUnit.SECONDS));

      // Act
      LocalDateTime now = testee.now();
      testee.next();
      testee.next();

      // Assert
      long durationSeconds = Duration.between(now, testee.now()).get(ChronoUnit.SECONDS);
      assertEquals(4, durationSeconds);

   }

}
