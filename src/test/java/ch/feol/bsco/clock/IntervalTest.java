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

class IntervalTest {

   private static final int YEAR = 2020;

   private static final LocalDateTime APRIL = LocalDateTime.of(YEAR, Month.APRIL, 1, 0, 0);

   private static final LocalDateTime MAY = LocalDateTime.of(YEAR, Month.MAY, 1, 0, 0);

   private static final LocalDateTime JUNE = LocalDateTime.of(YEAR, Month.JUNE, 1, 0, 0);

   private static final LocalDateTime JULY = LocalDateTime.of(YEAR, Month.JULY, 1, 0, 0);

   @Test
   void test_getDuration() {
      // Assert
      assertEquals(Duration.of(61, ChronoUnit.DAYS), new Interval(APRIL, JUNE).getDuration());
   }

   @Test
   void test_getDays() {

      // Assert
      assertEquals(30, new Interval(APRIL, MAY).getDays());
      assertEquals(91, new Interval(APRIL, JULY).getDays());
   }

   @Test
   void test_endBeforeStart() {
      // Act
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
         new Interval(JULY, MAY);
      });
      // Assert
      assertNotNull(exception.getMessage());
   }

   @Test
   void test_equals() {
      // Arrange
      Interval testee = new Interval(MAY, JULY);
      Interval mayToJuly = new Interval(MAY, JULY);
      Interval aprilToJuly = new Interval(APRIL, JULY);
      Interval mayToJune = new Interval(MAY, JUNE);
      // Assert
      assertEquals(testee, testee);
      assertEquals(mayToJuly, testee);
      assertNotEquals(aprilToJuly, testee);
      assertNotEquals(mayToJune, testee);
   }

   @Test
   void test_hashCode() {
      // Arrange
      Interval testee = new Interval(MAY, JULY);
      Interval mayToJuly = new Interval(MAY, JULY);
      Interval aprilToJuly = new Interval(APRIL, JULY);
      Interval mayToJune = new Interval(MAY, JUNE);
      // Act & assert
      assertEquals(testee.hashCode(), testee.hashCode());
      assertEquals(testee.hashCode(), mayToJuly.hashCode());
      assertNotEquals(testee.hashCode(), aprilToJuly.hashCode());
      assertNotEquals(testee.hashCode(), mayToJune.hashCode());
   }

}
