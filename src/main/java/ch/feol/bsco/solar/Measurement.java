package ch.feol.bsco.solar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.feol.bsco.clock.Clock;
import ch.feol.bsco.quantity.Power;

public class Measurement {

   private static final Logger LOG = LoggerFactory.getLogger(Measurement.class);

   class Point {
      LocalDateTime timestamp;

      Power productionPower;

      Power consumptionPower;

      Point(String input) {
         String[] splitInput = input.split(": ");
         if (splitInput.length != 2) {
            throw new IllegalArgumentException("SplitInput is " + splitInput);
         }
         timestamp = LocalDateTime.parse(splitInput[0]);
         String[] power = splitInput[1].split(", ");
         productionPower = Power.parse(power[0]);
         consumptionPower = Power.parse(power[1]);
      }

      @Override
      public String toString() {
         return productionPower + ", " + consumptionPower + " at " + timestamp;
      }
   }

   private List<Point> points = new ArrayList<>();

   private LocalDateTime startTimestamp = null;

   private LocalDateTime endTimestamp = null;

   public Measurement(String filename) {
      initValues(filename);
   }

   private void initValues(String filename) {
      try {
         InputStream inputStream = this.getClass().getResourceAsStream(filename);
         BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
         LocalDateTime lastTimestamp = LocalDateTime.MIN;
         Power lastProductionPower = Power.milliwatt(Long.MIN_VALUE);
         Power lastConsumptionPower = Power.milliwatt(Long.MIN_VALUE);
         while (reader.ready()) {
            String line = reader.readLine().strip();
            if (!line.isEmpty()) {
               Point newest = new Point(line);
               if (startTimestamp == null) {
                  startTimestamp = newest.timestamp;
               }
               if (!lastProductionPower.equals(newest.productionPower) || (!lastConsumptionPower.equals(newest.consumptionPower))) {
                  if (newest.timestamp.isAfter(lastTimestamp)) {
                     LOG.debug("Adding {}", newest);
                     points.add(newest);
                  } else {
                     throw new IllegalStateException("Last measurement timestamp " + lastTimestamp + " is after timestamp of actual measurement " + newest.timestamp);
                  }
               } else {
                  LOG.debug("Same {}", newest);
               }
               lastTimestamp = newest.timestamp;
               lastProductionPower = newest.productionPower;
               lastConsumptionPower = newest.consumptionPower;
               endTimestamp = lastTimestamp;
            }
         }
      } catch (IOException e) {
         throw new IllegalStateException(e);
      }
   }

   public Power getSurplusPower(Clock clock) {
      Point point = getPoint(clock);
      if (point != null) {
         return point.productionPower.minus(point.consumptionPower, Power.none());
      } else {
         return Power.none();
      }
   }

   public Power getNettoPower(Clock clock) {
      Point point = getPoint(clock);
      if (point != null) {
         return point.productionPower.minus(point.consumptionPower);
      } else {
         return Power.none();
      }
   }

   private Point getPoint(Clock clock) {
      LocalDateTime now = clock.now();
      Point lastPoint = null;
      for (Point point : points) {
         if (now.isBefore(point.timestamp)) {
            return lastPoint;
         }
         lastPoint = point;
      }
      return null;
   }

   public LocalDateTime getStartTimestamp() {
      return startTimestamp;
   }

   public LocalDateTime getEndTimestamp() {
      return endTimestamp;
   }

   public List<Point> getPoints() {
      return points;
   }
}
