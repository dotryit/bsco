package ch.feol.bsco.building;

import ch.feol.bsco.Configuration;
import ch.feol.bsco.clock.Clock;
import ch.feol.bsco.clock.StartEndSimulatedClock;
import ch.feol.bsco.quantity.Power;
import ch.feol.bsco.solar.Measurement;

public class MeasuredSimulation {

   private final Measurement measurement;

   private final Clock clock;

   public MeasuredSimulation(String filename, long maxJitterNanos) {
      measurement = new Measurement(filename);
      clock = new StartEndSimulatedClock(measurement.getStartTimestamp(), measurement.getEndTimestamp(), Configuration.CHECK_INTERVAL, maxJitterNanos);
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

   public Clock getClock() {
      return clock;
   }

   public Measurement getMeasurement() {
      return measurement;
   }
}
