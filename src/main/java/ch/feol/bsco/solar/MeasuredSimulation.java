package ch.feol.bsco.solar;

import ch.feol.bsco.Configuration;
import ch.feol.bsco.clock.Clock;
import ch.feol.bsco.clock.StartEndSimulatedClock;
import ch.feol.bsco.quantity.Power;

public class MeasuredSimulation {

   final Measurement measurement;

   final Clock clock;

   public MeasuredSimulation(String filename, long maxJitterNanos) {
      measurement = new Measurement(filename);
      clock = new StartEndSimulatedClock(measurement.startTimestamp, measurement.endTimestamp, Configuration.CHECK_INTERVAL, maxJitterNanos);
   }

   public Clock getClock() {
      return clock;
   }

   public EnergySystem getEnergySystem() {
      return new EnergySystem() {
         @Override
         public Power getSurplusPower() {
            return measurement.getSurplusPower(clock);
         }
         
         @Override
         public Power getNettoPower() {
            return measurement.getNettoPower(clock);
         }
      };
   }
}
