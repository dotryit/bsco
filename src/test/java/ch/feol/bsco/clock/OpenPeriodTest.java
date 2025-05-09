package ch.feol.bsco.clock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.Month;

import org.junit.jupiter.api.Test;

class OpenPeriodTest {

   private static final int YEAR = 2020;

   private static final LocalDateTime APRIL = LocalDateTime.of(YEAR, Month.APRIL, 1, 0, 0);

   private static final LocalDateTime MAY = LocalDateTime.of(YEAR, Month.MAY, 1, 0, 0);

   private static final LocalDateTime JUNE = LocalDateTime.of(YEAR, Month.JUNE, 1, 0, 0);

   private static final LocalDateTime JULY = LocalDateTime.of(YEAR, Month.JULY, 1, 0, 0);

   @Test
   void test_equals_true() {
      // Assert
      assertTrue(new OpenPeriod(APRIL).equals(new OpenPeriod(APRIL)));
   }

   @Test
   void test_equals_false() {
      // Assert
      assertFalse(new OpenPeriod(APRIL).equals(new OpenPeriod(JULY)));
   }

   @Test
   void test_end() {
      // Arrange
      OpenPeriod testee = new OpenPeriod(APRIL);

      // Act
      ClosedPeriod closed = testee.end(MAY);

      // Assert
      assertEquals(MAY, closed.getEndAt());
      assertEquals(30, closed.getDays());
   }

   @Test
   void test_end_fail() {
      // Arrange
      OpenPeriod testee = new OpenPeriod(JULY);
      // Act
      IllegalArgumentException cause = assertThrows(IllegalArgumentException.class, () -> testee.end(JUNE));
      // Assert
      assertNotNull(cause.getMessage());
   }

   @Test
   void test_startAt() {
      // Arrange
      OpenPeriod testee = new OpenPeriod(JULY);
      // Act & assert
      assertEquals(JULY, testee.getStartAt());
   }

   @Test
   void test_hashCode_true() {
      // Act & assert
      assertEquals(new OpenPeriod(JULY).hashCode(), new OpenPeriod(JULY).hashCode());
   }

   @Test
   void test_hashCode_false() {
      // Act & assert
      assertNotEquals(new OpenPeriod(JUNE).hashCode(), new OpenPeriod(JULY).hashCode());
   }

}
