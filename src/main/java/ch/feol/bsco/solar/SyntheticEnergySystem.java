package ch.feol.bsco.solar;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;

import ch.feol.bsco.building.EnergyProducer;
import ch.feol.bsco.clock.Clock;
import ch.feol.bsco.quantity.Power;

/**
 * Simulates a synthetic PV system on the basis of exp(-x^2).
 * <p>
 * Power generation starts at 05:57 and ends at 18:03 with an outage beween 13:30 and 15:00.
 * <p>
 * The peak power is reached at midday.
 */
public class SyntheticEnergySystem implements EnergyProducer {

   private final Clock clock;

   private final Power peakPower;

   public SyntheticEnergySystem(Clock clock, Power peakPower) {
      this.clock = clock;
      this.peakPower = peakPower;
   }

   @Override
   public Power getActualProduction() {

      LocalDateTime now = clock.now();
      
      LocalTime actualTime = LocalTime.from(clock.now());

      // Simulate PV power outage between 13:30 and 15:00
      if (actualTime.isAfter(LocalTime.of(13, 30)) && actualTime.isBefore(LocalTime.of(15, 0))) {
         return Power.none();
      }
      return getPower(now, peakPower.getKilowatt());
   }

   static Power getPower(LocalDateTime time, double peakPowerKiloWatt) {
      int minuteOfDay = time.get(ChronoField.MINUTE_OF_DAY);
      int midDayMinute = 12 * 60;
      double power = (1 + 0.3) * peakPowerKiloWatt * Math.exp(-1 * Math.pow((minuteOfDay - midDayMinute) / 300.0, 2)) - (0.3 * peakPowerKiloWatt);
      if (power > 0) {
         return Power.kilowatt(power);
      } else {
         return Power.none();
      }
   }
}
