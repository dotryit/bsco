package ch.feol.bsco.pi.board;

public interface Relay {

   /**
    * Turn relay to connector A and turn board LED of channel on.
    * 
    * @return true if relay was turned to A and false if it was already on connector A.
    */
   public boolean connectorA();

   /**
    * Turn relay to connector B and turn board LED of channel off.
    * 
    * @return true if relay was turned to B and false if it was already on connector B.
    */
   public boolean connectorB();

   /**
    * @return the logical number of the relay.
    */
   public int getRelayNumber();
}
