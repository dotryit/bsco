package ch.feol.bsco.quantity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class AbstractQuantityTest {

   private static class MyQuantity extends AbstractQuantity {
      
      static AbstractQuantity.Scale scale = new AbstractQuantity.Scale("mQ", "Q", "kQ", "MQ");

      protected AbstractQuantity.Scale getScale() {
         return scale;
      }

      protected MyQuantity(long quantity) {
         super(quantity);
      }

      public static MyQuantity parse(String input) {
         return new MyQuantity(scale.parse(input));
      }
   }

   @Test
   void test_parse() {
      assertEquals(new MyQuantity(5678), MyQuantity.parse("5.678 Q"));
      assertEquals(new MyQuantity(5678), MyQuantity.parse("5678 mQ"));
      assertEquals(new MyQuantity(5678), MyQuantity.parse("5678mQ"));
      assertEquals(new MyQuantity(5678), MyQuantity.parse("5678   mQ"));
      assertEquals(new MyQuantity(5678), MyQuantity.parse("0.005678 kQ"));
      
      assertThrows(IllegalArgumentException.class, () -> MyQuantity.parse("1.7 PQ"));
   }
   
   @Test
   void test_equals() {
      assertEquals(new MyQuantity(1), new MyQuantity(1));

      // 4 digits
      assertEquals(new MyQuantity(5678), new MyQuantity(5678));
      assertNotEquals(new MyQuantity(5678), new MyQuantity(5677));
      assertNotEquals(new MyQuantity(5678), new MyQuantity(5679));

      // 5 digits
      assertEquals(new MyQuantity(45678), new MyQuantity(45678));
      assertNotEquals(new MyQuantity(45678), new MyQuantity(45677));
      assertNotEquals(new MyQuantity(45678), new MyQuantity(45679));

      // 6 digits
      assertEquals(new MyQuantity(345678), new MyQuantity(345678));
      assertNotEquals(new MyQuantity(345678), new MyQuantity(345677));
      assertNotEquals(new MyQuantity(345678), new MyQuantity(345679));

      // 7 digits
      assertEquals(new MyQuantity(2345678), new MyQuantity(2345678));
      assertEquals(new MyQuantity(2345678), new MyQuantity(2345677));
      assertEquals(new MyQuantity(2345678), new MyQuantity(2345679));
   }

   @Test
   void test_toString() {
      assertEquals("1 mQ", new MyQuantity(1).toString());
      assertEquals("784 mQ", new MyQuantity(784).toString());
      assertEquals("1 Q", new MyQuantity(1000).toString());
      assertEquals("45.698 Q", new MyQuantity(45_698).toString());
      assertEquals("9.852 kQ", new MyQuantity(9_852_398).toString());
      assertEquals("45.875 MQ", new MyQuantity(45_874_549_714l).toString());
      assertEquals("89745.821 MQ", new MyQuantity(89_745_821_145_939l).toString());

      assertEquals("-1 mQ", new MyQuantity(-1).toString());
      assertEquals("-784 mQ", new MyQuantity(-784).toString());
      assertEquals("-1 Q", new MyQuantity(-1000).toString());
      assertEquals("-45.698 Q", new MyQuantity(-45_698).toString());
      assertEquals("-9.852 kQ", new MyQuantity(-9_852_398).toString());
      assertEquals("-45.875 MQ", new MyQuantity(-45_874_549_714l).toString());
      assertEquals("-89745.821 MQ", new MyQuantity(-89_745_821_145_939l).toString());
}

}
