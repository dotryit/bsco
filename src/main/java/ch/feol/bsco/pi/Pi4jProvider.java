package ch.feol.bsco.pi;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;

public class Pi4jProvider {

   private static Context pi4j;

   private Pi4jProvider() {
      // Singleton
   }

   public static Context getContext() {
      if (pi4j == null) {
         pi4j = Pi4J.newAutoContext();
      }
      return pi4j;
   }

   public static void shutdown() {
      if (pi4j != null) {
         pi4j.shutdown();
      }
   }
}
