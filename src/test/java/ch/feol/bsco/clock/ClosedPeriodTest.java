package ch.feol.bsco.clock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

class ClosedPeriodTest {

   private static final int YEAR = 2020;

   private static final LocalDateTime APRIL = LocalDateTime.of(YEAR, Month.APRIL, 1, 0, 0);

   private static final LocalDateTime MAY = LocalDateTime.of(YEAR, Month.MAY, 1, 0, 0);

   private static final LocalDateTime JUNE = LocalDateTime.of(YEAR, Month.JUNE, 1, 0, 0);

   private static final LocalDateTime JULY = LocalDateTime.of(YEAR, Month.JULY, 1, 0, 0);

   @Test
   void test_getDuration() {
      // Assert
      assertEquals(Duration.of(61, ChronoUnit.DAYS), new ClosedPeriod(APRIL, JUNE).getDuration());
   }

   @Test
   void test_getDays() {

      // Assert
      assertEquals(30, new ClosedPeriod(APRIL, MAY).getDays());
      assertEquals(91, new ClosedPeriod(APRIL, JULY).getDays());
   }

   @Test
   void test_endBeforeStart() {
      // Act
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
         new ClosedPeriod(JULY, MAY);
      });
      // Assert
      assertNotNull(exception.getMessage());
   }

   @Test
   void test_equals() {
      // Arrange
      ClosedPeriod testee = new ClosedPeriod(MAY, JULY);
      ClosedPeriod mayToJuly = new ClosedPeriod(MAY, JULY);
      ClosedPeriod aprilToJuly = new ClosedPeriod(APRIL, JULY);
      ClosedPeriod mayToJune = new ClosedPeriod(MAY, JUNE);
      // Assert
      assertEquals(testee, testee);
      assertEquals(mayToJuly, testee);
      assertNotEquals(aprilToJuly, testee);
      assertNotEquals(mayToJune, testee);
   }

   @Test
   void test_hashCode() {
      // Arrange
      ClosedPeriod testee = new ClosedPeriod(MAY, JULY);
      ClosedPeriod mayToJuly = new ClosedPeriod(MAY, JULY);
      ClosedPeriod aprilToJuly = new ClosedPeriod(APRIL, JULY);
      ClosedPeriod mayToJune = new ClosedPeriod(MAY, JUNE);
      // Act & assert
      assertEquals(testee.hashCode(), testee.hashCode());
      assertEquals(testee.hashCode(), mayToJuly.hashCode());
      assertNotEquals(testee.hashCode(), aprilToJuly.hashCode());
      assertNotEquals(testee.hashCode(), mayToJune.hashCode());
   }

}
