package ch.feol.bsco.boiler;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import ch.feol.bsco.clock.Clock;
import ch.feol.bsco.clock.StartCountSimulatedClock;
import ch.feol.bsco.pi.board.DummyRelay;
import ch.feol.bsco.pi.board.Relay;
import ch.feol.bsco.quantity.Power;

class BoilerTest {

   @Test
   void test_getPower() {

      Clock clock = new StartCountSimulatedClock(LocalDateTime.now(), 10, Duration.ofMinutes(10));

      Relay relay = new DummyRelay(1);
      Boiler testee = new Boiler(relay, Power.kilowatt(5), Duration.ofHours(4), clock, Duration.ZERO);

      testee.on();
      clock.next(); // 20 minutes ON with history 30 minutes
      clock.next(); // 10 minutes ON with history 20 minutes
      testee.off();
      clock.next(); // 10 minutes ON with history 10 minutes
      testee.on();
      clock.next(); // 00 minutes ON with history 00 minutes
      testee.off();

      assertEquals(Duration.ofMinutes(30), testee.getTotalOnDuration(Duration.ofMinutes(50)));
      assertEquals(Duration.ofMinutes(30), testee.getTotalOnDuration(Duration.ofMinutes(40)));
      assertEquals(Duration.ofMinutes(20), testee.getTotalOnDuration(Duration.ofMinutes(30)));
      assertEquals(Duration.ofMinutes(10), testee.getTotalOnDuration(Duration.ofMinutes(20)));
      assertEquals(Duration.ofMinutes(10), testee.getTotalOnDuration(Duration.ofMinutes(10)));
      assertEquals(Duration.ofMinutes(0), testee.getTotalOnDuration(Duration.ofMinutes(0)));

      assertEquals(Power.none(), testee.getEstimatedPower(Duration.ofMinutes(50), Duration.ofMinutes(28)));
      assertEquals(Power.none(), testee.getEstimatedPower(Duration.ofMinutes(50), Duration.ofMinutes(29)));
      assertEquals(Power.none(), testee.getEstimatedPower(Duration.ofMinutes(50), Duration.ofMinutes(30)));
      assertEquals(Power.kilowatt(5), testee.getEstimatedPower(Duration.ofMinutes(50), Duration.ofMinutes(31)));

      assertEquals(Power.none(), testee.getEstimatedPower(Duration.ofMinutes(20), Duration.ofMinutes(8)));
      assertEquals(Power.none(), testee.getEstimatedPower(Duration.ofMinutes(20), Duration.ofMinutes(9)));
      assertEquals(Power.none(), testee.getEstimatedPower(Duration.ofMinutes(20), Duration.ofMinutes(10)));
      assertEquals(Power.kilowatt(5), testee.getEstimatedPower(Duration.ofMinutes(20), Duration.ofMinutes(11)));

   }

}
