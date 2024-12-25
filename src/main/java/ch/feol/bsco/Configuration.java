package ch.feol.bsco;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;

import ch.feol.bsco.quantity.Power;

public class Configuration {
   
   public static final String REAL_SOLARLOG = "192.168.1.102:80";
   
   public static final String REAL_SOLARLOG_NET = "192.168.1.";
   
   public static final String REMOTE_SOLARLOG = "pi.feol.ch:9878"; // Use soltun01 from pi.feol.ch to start tunnel to SolarLog

   public static final int NUMBER_OF_BOILERS = 6;

   public static final Power BOILER_NOMINAL_POWER = Power.kilowatt(2.4);

   public static final Duration TOTAL_HEAT_UP_DURATION = Duration.ofHours(4);

   public static final Duration CHECK_INTERVAL = Duration.ofMinutes(5);

   public static final Duration MINIMAL_ON_DURATION = Duration.ofMinutes(20);

   public Configuration() {
      // Singleton
   }

   public static String getSoloarLogAddress() {
      try {
         String address = InetAddress.getLocalHost().getHostAddress();
         if (address.startsWith(REAL_SOLARLOG_NET)) {
            return REAL_SOLARLOG;
         } else {
            return REMOTE_SOLARLOG; 
         }
      } catch (UnknownHostException e) {
         throw new IllegalStateException(e);
      }
   }
}
