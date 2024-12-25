package ch.feol.bsco.pi.board;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalOutputConfigBuilder;
import com.pi4j.io.gpio.digital.DigitalState;

import ch.feol.bsco.pi.Pi4jProvider;

/**
 * The {@link RealRelay} class represents the relays on the Waveshare board.
 * <p>
 * Each relay has three power connectors A, B, C.
 * <p>
 * If the corresponding digital output pin of the raspberry pi is high or the board has no power then the circuit B-C is closed and the LED on the board is not burning.
 * <p>
 * If the corresponding digital output pin of the raspberry pi is low then the circuit A-C is closed and the LED on the board is burning.
 * <p>
 * For the Raspberry Pi 4B rev 1.2 the pin layout of the J8 connector is as follows:
 * 
 * <pre>
 *              3V3  (1) (2)  5V            
 *            GPIO2  (3) (4)  5V            
 *            GPIO3  (5) (6)  GND           
 *            GPIO4  (7) (8)  GPIO14        
 *              GND  (9) (10) GPIO15        
 *           GPIO17 (11) (12) GPIO18        
 *           GPIO27 (13) (14) GND           
 *           GPIO22 (15) (16) GPIO23        
 *              3V3 (17) (18) GPIO24        
 *           GPIO10 (19) (20) GND           
 *            GPIO9 (21) (22) GPIO25        
 *           GPIO11 (23) (24) GPIO8         
 *              GND (25) (26) GPIO7         
 *            GPIO0 (27) (28) GPIO1         
 * Relay 1 /  GPIO5 (29) (30) GND           
 * Relay 2 /  GPIO6 (31) (32) GPIO12        
 * Relay 3 / GPIO13 (33) (34) GND           
 * Relay 5 / GPIO19 (35) (36) GPIO16 / Relay 4
 * Relay 8 / GPIO26 (37) (38) GPIO20 / Relay 6
 *              GND (39) (40) GPIO21 / Relay 7
 * </pre>
 */
public enum RealRelay implements Relay {

   C1(5, 1),
   C2(6, 2),
   C3(13, 3),
   C4(16, 4),
   C5(19, 5),
   C6(20, 6),
   C7(21, 7),
   C8(26, 8);

   private static final Logger LOG = LoggerFactory.getLogger(RealRelay.class);

   private final int pinNumber;

   private final DigitalOutput output;

   private final int relayNumber;

   private final String name;

   private RealRelay(int pinNumber, int relayNumber) {
      this.pinNumber = pinNumber;
      this.relayNumber = relayNumber;
      this.name = "relay " + relayNumber;
      Context context = Pi4jProvider.getContext();
      DigitalOutputConfigBuilder configBuilder = DigitalOutput.newConfigBuilder(context) //
            .address(pinNumber) //
            .name(name) //
            .initial(DigitalState.HIGH) //
            .shutdown(DigitalState.HIGH);
      output = context.digitalOutput().create(configBuilder);
   }

   public String getName() {
      return name;
   }

   public int getPinNumber() {
      return pinNumber;
   }

   /**
    * Turn relay to connector B and turn board LED of channel off.
    * 
    * @return true if relay was turned to B and false if it was already on connector B.
    */
   public boolean connectorB() {
      if (output.isLow()) {
         LOG.debug("Switching {} to B and turing LED off", name);
         output.high();
         return true;
      } else {
         return false;
      }
   }

   /**
    * Turn relay to connector A and turn board LED of channel on.
    * 
    * @return true if relay was turned to A and false if it was already on connector A.
    */
   public boolean connectorA() {
      if (output.isHigh()) {
         LOG.debug("Switching {} to A and turning LED on", name);
         output.low();
         return true;
      } else
         return false;
   }

   public int getRelayNumber() {
      return relayNumber;
   }
}
