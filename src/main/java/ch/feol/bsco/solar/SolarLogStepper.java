package ch.feol.bsco.solar;

public class SolarLogStepper implements Stepper {

   private int intervalSeconds;

   public SolarLogStepper(int intervalSeconds) {
      this.intervalSeconds = intervalSeconds;
   }

   @Override
   public boolean next() {
      try {
         Thread.sleep(intervalSeconds * 1000l);
      } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
         return false; // Stop stepping
      }
      return true; // Run at least one more time.
   }
}
