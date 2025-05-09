package ch.feol.bsco.building;

import java.time.LocalDateTime;

import ch.feol.bsco.Configuration;
import ch.feol.bsco.clock.Clock;
import ch.feol.bsco.clock.StartEndSimulatedClock;
import ch.feol.bsco.quantity.Power;
import ch.feol.bsco.solar.SyntheticEnergySystem;

public class SyntheticSimulation {

   private final EnergyProducer producer;

   private final EnergyConsumer consumer;

   private final Clock clock;

   public SyntheticSimulation(LocalDateTime start, LocalDateTime end, long maxJitterNanos) {
      clock = new StartEndSimulatedClock(start, end, Configuration.CHECK_INTERVAL, maxJitterNanos);
      this.producer = new SyntheticEnergySystem(clock, Power.kilowatt(14));
      this.consumer = new ConstantConsumer(Power.kilowatt(2.1));
   }

   public Clock getClock() {
      return clock;
   }

   public EnergySystem getEnergySystem() {
      return new EnergySystem() {

         @Override
         public Power getSurplusPower() {
            return producer.getActualProduction().minus(consumer.getActualConsumption(), Power.none());
         }

         @Override
         public Power getNettoPower() {
            return producer.getActualProduction().minus(consumer.getActualConsumption());
         }
      };
   }
}
