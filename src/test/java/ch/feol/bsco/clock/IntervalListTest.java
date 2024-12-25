package ch.feol.bsco.clock;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class IntervalListTest {

   @Test
   void test() {

      Clock clock = new StartCountSimulatedClock(LocalDateTime.now(), 24 * 12 + 10, Duration.ofMinutes(5));
      boolean on = false;
      LocalDateTime lastSwitchedOn = clock.now();
      LocalDateTime lastSwitchedOff = clock.now();

      IntervalList testee = new IntervalList(clock, Duration.ofDays(2));

      int ons = 0;
      while (!clock.isExpired()) {
         if (on) {
            on = false;
            lastSwitchedOff = clock.now();
            Interval onInterval = new Interval(lastSwitchedOn, lastSwitchedOff);
            testee.add(onInterval);
            ons++;
         } else {
            on = true;
            lastSwitchedOn = clock.now();
         }
         clock.next();
      }
      assertEquals(12 * 12 + 5, ons);
      assertEquals(Duration.ofMinutes(745), testee.getTotalOnDuration());
      assertEquals(Duration.ofMinutes(20), testee.getTotalOnDuration(Duration.ofMinutes(43)));
      assertEquals(Duration.ofMinutes(20), testee.getTotalOnDuration(Duration.ofMinutes(44)));
      assertEquals(Duration.ofMinutes(20), testee.getTotalOnDuration(Duration.ofMinutes(45)));
      assertEquals(Duration.ofMinutes(21), testee.getTotalOnDuration(Duration.ofMinutes(46)));
      assertEquals(Duration.ofMinutes(22), testee.getTotalOnDuration(Duration.ofMinutes(47)));
      assertEquals(Duration.ofMinutes(23), testee.getTotalOnDuration(Duration.ofMinutes(48)));
      assertEquals(Duration.ofMinutes(24), testee.getTotalOnDuration(Duration.ofMinutes(49)));
      assertEquals(Duration.ofMinutes(25), testee.getTotalOnDuration(Duration.ofMinutes(50)));
      assertEquals(Duration.ofMinutes(25), testee.getTotalOnDuration(Duration.ofMinutes(51)));
      assertEquals(Duration.ofMinutes(25), testee.getTotalOnDuration(Duration.ofMinutes(52)));
   }

}
