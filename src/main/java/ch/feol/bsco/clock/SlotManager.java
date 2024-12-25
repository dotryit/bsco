package ch.feol.bsco.clock;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.feol.bsco.boiler.Boiler;

public class SlotManager {

   private static final Logger LOG = LoggerFactory.getLogger(SlotManager.class);

   private final List<Boiler> boilers;

   private final Clock clock;

   private final int boilersPerSlot;

   private final Duration slotInterval;

   private final int slots;

   private LocalTime startAt;

   public SlotManager(List<Boiler> boilers, Clock clock, int boilersPerSlot, LocalTime startAt, Duration totalDuration) {
      this.boilers = boilers;
      this.clock = clock;
      this.boilersPerSlot = boilersPerSlot;
      slots = boilers.size() / boilersPerSlot;
      this.slotInterval = totalDuration.dividedBy(slots);
      this.startAt = startAt;
   }

   public void manage() {
      LocalTime now = clock.now().toLocalTime();
      int onSlot = -1;
      for (int interval = 0; interval < slots; interval++) {
         LocalTime boilerStartTime = startAt.plus(slotInterval.multipliedBy(interval));
         LocalTime boilerEndTime = boilerStartTime.plus(slotInterval);
         for (int slot = 0; slot < slots; slot++) {
            if (boilerStartTime.isBefore(boilerEndTime)) {
               if (now.isAfter(boilerStartTime) && now.isBefore(boilerEndTime) && slot == interval) {
                  onSlot = slot;
               }
            } else {
               if (now.isBefore(LocalTime.NOON)) {
                  if (now.isBefore(boilerEndTime) && slot == interval) {
                     onSlot = slot;
                  }
               } else {
                  if (now.isAfter(boilerStartTime) && slot == interval) {
                     onSlot = slot;
                  }
               }
            }
         }
      }
      if (onSlot == -1) { // No slot found
         // Outside managed interval -> turn boilers off
         for (int boiler = 0; boiler < boilers.size(); boiler++) {
            boilers.get(boiler).off();
         }
         return;
      }
      LOG.debug("ON slot is {}", onSlot);
      for (int slot = 0; slot < slots; slot++) {
         if (slot == onSlot) {
            for (int slotBoiler = 0; slotBoiler < boilersPerSlot; slotBoiler++) {
               boilers.get(slot * boilersPerSlot + slotBoiler).on();
            }
         } else {
            for (int slotBoiler = 0; slotBoiler < boilersPerSlot; slotBoiler++) {
               boilers.get(slot * boilersPerSlot + slotBoiler).off();
            }
         }
      }
   }

}
