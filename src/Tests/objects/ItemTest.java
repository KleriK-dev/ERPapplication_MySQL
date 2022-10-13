package objects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
    
    Item item = new Item();

    // Run some tests for the generateNextItemCode method from Item class
    // check all the possible outcomes
    // this helps to make the code better and check if there are some bugs

    @Test
    void testItemCodeReturnWhenPreviousItemIsNull(){

        // given
        String previousItem = null;

        // when
        String returningItemCode = item.generateNextItemCode(previousItem);

        // then
        assertEquals("0001", returningItemCode);

    }

    @Test
    void testItemCodeReturnWhenPreviousItemIsSmallerThanTen(){

        // given
        String previousItem = "9";

        // when
        String returningItemCode = item.generateNextItemCode(previousItem);

        // then
        assertEquals("0010", returningItemCode);

    }

    @Test
    void testItemCodeReturnWhenPreviousItemIsEqualToTen(){

        // given
        String previousItem = "10";

        // when
        String returningItemCode = item.generateNextItemCode(previousItem);

        // then
        assertEquals("0011", returningItemCode);

    }

    @Test
    void testItemCodeReturnWhenPreviousItemIsBiggerThanTen(){

        // given
        String previousItem = "12";

        // when
        String returningItemCode = item.generateNextItemCode(previousItem);

        // then
        assertEquals("0013", returningItemCode);

    }

    @Test
    void testItemCodeReturnWhenPreviousItemIsEqualToOneHundred(){

        // given
        String previousItem = "100";

        // when
        String returningItemCode = item.generateNextItemCode(previousItem);

        // then
        assertEquals("0101", returningItemCode);

    }

    @Test
    void testItemCodeReturnWhenPreviousItemIsBiggerThanOneHundred(){

        // given
        String previousItem = "148";

        // when
        String returningItemCode = item.generateNextItemCode(previousItem);

        // then
        assertEquals("0149", returningItemCode);

    }

    @Test
    void testItemCodeReturnWhenPreviousItemIsEqualToOneThousand(){

        // given
        String previousItem = "1000";

        // when
        String returningItemCode = item.generateNextItemCode(previousItem);

        // then
        assertEquals("1001", returningItemCode);

    }

    @Test
    void testItemCodeReturnWhenPreviousItemIsBiggerThanOneThousand(){

        // given
        String previousItem = "1999";

        // when
        String returningItemCode = item.generateNextItemCode(previousItem);

        // then
        assertEquals("2000", returningItemCode);

    }

    @Test
    void testItemCodeReturnWhenPreviousItemIsEqualTo9999(){

        // given
        String previousItem = "9999";

        // when
        String returningItemCode = item.generateNextItemCode(previousItem);

        // then
        assertEquals("10000", returningItemCode);

    }

    // test checkIfItemCodeExists method
    // test 2 possible outcomes

    @Test
    void testCheckItemCodeExistanceReturnsTrue(){

        // given
        String itemCode = "0001";

        // when
        boolean itemCodeExistance = item.checkIfItemCodeExists(itemCode);

        // then
        assertEquals(true, itemCodeExistance);

    }

    @Test
    void testCheckItemCodeExistanceReturnsFalse(){

        // given
        String itemCode = "9999";

        // when
        boolean itemCodeExistance = item.checkIfItemCodeExists(itemCode);

        // then
        assertEquals(false, itemCodeExistance);

    }

}