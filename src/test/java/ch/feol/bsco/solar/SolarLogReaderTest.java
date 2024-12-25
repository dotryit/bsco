package ch.feol.bsco.solar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.feol.bsco.quantity.Power;

@ExtendWith(MockitoExtension.class)
class SolarLogReaderTest {

   SolarLog solarLog = mock(SolarLog.class);

   Stepper stepper = mock(Stepper.class);

   SolarLogReader testee = new SolarLogReader(5, solarLog, stepper);

   @Test
   void test_getAverageSurplusPower() throws SolarLogException {
      // Arrange
      doReturn( //
            new Data(LocalDateTime.now(), Power.watt(5000), Power.watt(5000), Power.watt(150)), //
            new Data(LocalDateTime.now(), Power.watt(4000), Power.watt(5000), Power.watt(1150)), //
            new Data(LocalDateTime.now(), Power.watt(3000), Power.watt(5000), Power.watt(2150)), //
            new Data(LocalDateTime.now(), Power.watt(2000), Power.watt(5000), Power.watt(3150)), //
            new Data(LocalDateTime.now(), Power.watt(1000), Power.watt(5000), Power.watt(4150))).when(solarLog).read();
      when(stepper.next()).thenReturn(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
      testee.run();

      // Act
      Power surplus = testee.getAverageSurplusPower();

      // Assert
      assertEquals(Power.kilowatt(0.85), surplus);
   }

}
