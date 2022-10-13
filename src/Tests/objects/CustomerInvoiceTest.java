package objects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerInvoiceTest {
    
    CustomerInvoice customerInvoice = new CustomerInvoice();

    // Run some tests for the generateInvoiceNumber method from CustomerInvoice class
    // check all the possible outcomes
    // this helps to make the code better and check if there are some bugs

    @Test
    void testGenerateInvoiceNumberWhenPreviousRowIsNull(){

        // given
        String previousInvoiceNumber = null;

        // when
        String returningInvoiceNumber = customerInvoice.generateInvoiceNumber(previousInvoiceNumber);

        // then
        assertEquals("00000001", returningInvoiceNumber);

    }

    @Test
    void testGenerateInvoiceNumberWhenPreviousNumberIsSmallerThanTens(){

        // given
        String previousInvoiceNumber = "8";

        // when
        String returningInvoiceNumber = customerInvoice.generateInvoiceNumber(previousInvoiceNumber);

        // then
        assertEquals("00000009", returningInvoiceNumber);

    }

    @Test
    void testGenerateInvoiceNumberWhenPreviousNumberIsEqualToTen(){

        // given
        String previousInvoiceNumber = "10";

        // when
        String returningInvoiceNumber = customerInvoice.generateInvoiceNumber(previousInvoiceNumber);

        // then
        assertEquals("00000011", returningInvoiceNumber);

    }

    @Test
    void testGenerateInvoiceNumberWhenPreviousNumberIsSmallerThanHundreds(){

        // given
        String previousInvoiceNumber = "99";

        // when
        String returningInvoiceNumber = customerInvoice.generateInvoiceNumber(previousInvoiceNumber);

        // then
        assertEquals("00000100", returningInvoiceNumber);

    }

    @Test
    void testGenerateInvoiceNumberWhenPreviousNumberIsEqualToHundreds(){

        // given
        String previousInvoiceNumber = "100";

        // when
        String returningInvoiceNumber = customerInvoice.generateInvoiceNumber(previousInvoiceNumber);

        // then
        assertEquals("00000101", returningInvoiceNumber);

    }

    @Test
    void testGenerateInvoiceNumberWhenPreviousNumberIsSmallerThanThousands(){

        // given
        String previousInvoiceNumber = "999";

        // when
        String returningInvoiceNumber = customerInvoice.generateInvoiceNumber(previousInvoiceNumber);

        // then
        assertEquals("00001000", returningInvoiceNumber);

    }

    @Test
    void testGenerateInvoiceNumberWhenPreviousNumberIsEqualToThousands(){

        // given
        String previousInvoiceNumber = "1000";

        // when
        String returningInvoiceNumber = customerInvoice.generateInvoiceNumber(previousInvoiceNumber);

        // then
        assertEquals("00001001", returningInvoiceNumber);

    }

    @Test
    void testGenerateInvoiceNumberWhenPreviousNumberIsEqualToTenThousands(){

        // given
        String previousInvoiceNumber = "10000";

        // when
        String returningInvoiceNumber = customerInvoice.generateInvoiceNumber(previousInvoiceNumber);

        // then
        assertEquals("00010001", returningInvoiceNumber);

    }

    @Test
    void testGenerateInvoiceNumberWhenPreviousNumberIsSmallerThanHundredThousand(){

        // given
        String previousInvoiceNumber = "99999";

        // when
        String returningInvoiceNumber = customerInvoice.generateInvoiceNumber(previousInvoiceNumber);

        // then
        assertEquals("00100000", returningInvoiceNumber);

    }

    @Test
    void testGenerateInvoiceNumberWhenPreviousNumberIsEqualToHundredThousand(){

        // given
        String previousInvoiceNumber = "100000";

        // when
        String returningInvoiceNumber = customerInvoice.generateInvoiceNumber(previousInvoiceNumber);

        // then
        assertEquals("00100001", returningInvoiceNumber);

    }

    @Test
    void testGenerateInvoiceNumberWhenPreviousNumberIsSmallerThanMillions(){

        // given
        String previousInvoiceNumber = "999999";

        // when
        String returningInvoiceNumber = customerInvoice.generateInvoiceNumber(previousInvoiceNumber);

        // then
        assertEquals("01000000", returningInvoiceNumber);

    }

    @Test
    void testGenerateInvoiceNumberWhenPreviousNumberIsEqualToMillions(){

        // given
        String previousInvoiceNumber = "1000000";

        // when
        String returningInvoiceNumber = customerInvoice.generateInvoiceNumber(previousInvoiceNumber);

        // then
        assertEquals("01000001", returningInvoiceNumber);

    }

    @Test
    void testGenerateInvoiceNumberWhenPreviousNumberIsEqualToMoreThanMillions(){

        // given
        String previousInvoiceNumber = "10000000";

        // when
        String returningInvoiceNumber = customerInvoice.generateInvoiceNumber(previousInvoiceNumber);

        // then
        assertEquals("10000001", returningInvoiceNumber);

    }

    // Run some tests for the checkBeforeInsertion method from CustomerInvoiceclass
    // check all the possible outcomes

    @Test
    void testCheckBeforeInsertionWhenDocumentIsInvoiceWithDeliveryAndLicensePlateReturnsTrue(){

        // given
        String invoiceTypeDescription = "SALES INVOICE - DELIVERY NOTE";
        Integer invoiceTypeID = 1;
        String licensePlate = "aaaaa";
        Integer customerID = 1;

        // when
        boolean methodReturn = customerInvoice.checkBeforeInsertion(invoiceTypeDescription, invoiceTypeID, licensePlate, customerID);

        // then
        assertEquals(true, methodReturn);

    }

    @Test
    void testCheckBeforeInsertionWhenDocumentIsInvoiceWithDeliveryAndNoLicensePlateReturnsFalse(){

        // given
        String invoiceTypeDescription = "SALES INVOICE - DELIVERY NOTE";
        Integer invoiceTypeID = 1;
        String licensePlate = "";
        Integer customerID = 1;

        // when
        boolean methodReturn;
        try{ // we try this method, if it returns false it will also throw an ExceptionInInitializerError as unit test can not read the alert that javafx want's to display
           methodReturn = customerInvoice.checkBeforeInsertion(invoiceTypeDescription, invoiceTypeID, licensePlate, customerID);
        } catch(ExceptionInInitializerError e){ //thats why we catch that exception and set false to methodReturn
            // we set it false because we know that from the moment that exception occurs the method returns false
            // check at CustomerInvoice class at line 354, we can see there the alert that we want to display
            methodReturn = false;
        }

        // then
        assertEquals(false, methodReturn);

    }

    @Test
    void testCheckBeforeInsertionWhenDocumentIsInvoiceAndCustomerHasNotTaxCodeReturnsFalse(){

        // given
        String invoiceTypeDescription = "SALES INVOICE - DELIVERY NOTE";
        Integer invoiceTypeID = 1;
        String licensePlate = "aaaa";
        Integer customerID = 6; // in my database this customer with id 6 does not have a taxcode

        // when
        boolean methodReturn;
        try{ // we try this method, if it returns false it will also throw an ExceptionInInitializerError as unit test can not read the alert that javafx want's to display
            methodReturn = customerInvoice.checkBeforeInsertion(invoiceTypeDescription, invoiceTypeID, licensePlate, customerID);
        } catch(ExceptionInInitializerError e){ //thats why we catch that exception and set false to methodReturn
            // we set it false because we know that from the moment that exception occurs the method returns false
            // check at CustomerInvoice class at line 354, we can see there the alert that we want to display
            methodReturn = false;
        }

        // then
        assertEquals(false, methodReturn);

    }

    @Test
    void testCheckBeforeInsertionWhenDocumentIsInvoiceWithoutDeliveryReturnsTrue(){

        // given
        String invoiceTypeDescription = "SERVICE INVOICE";
        Integer invoiceTypeID = 2; // service invoice has id 2 and does not have delivery
        String licensePlate = "";
        Integer customerID = 1;

        // when
        boolean methodReturn = customerInvoice.checkBeforeInsertion(invoiceTypeDescription, invoiceTypeID, licensePlate, customerID);

        // then
        assertEquals(true, methodReturn);

    }

    @Test
    void testCheckBeforeInsertionWhenDocumentIsReceiptWithDeliveryAndLicensePlateReturnsTrue(){

        // given
        String invoiceTypeDescription = "RECEIPT OF RETAIL TRANSACTIONS";
        Integer invoiceTypeID = 3;
        String licensePlate = "aaaaa";
        Integer customerID = 1;

        // when
        boolean methodReturn = customerInvoice.checkBeforeInsertion(invoiceTypeDescription, invoiceTypeID, licensePlate, customerID);

        // then
        assertEquals(true, methodReturn);

    }

    @Test
    void testCheckBeforeInsertionWhenDocumentIsReceiptWithDeliveryAndNoLicensePlateReturnsFalse(){

        // given
        String invoiceTypeDescription = "RECEIPT OF RETAIL TRANSACTIONS";
        Integer invoiceTypeID = 3;
        String licensePlate = "";
        Integer customerID = 1;

        // when
        boolean methodReturn;
        try{ // we try this method, if it returns false it will also throw an ExceptionInInitializerError as unit test can not read the alert that javafx want's to display
            methodReturn = customerInvoice.checkBeforeInsertion(invoiceTypeDescription, invoiceTypeID, licensePlate, customerID);
        } catch(ExceptionInInitializerError e){ //thats why we catch that exception and set false to methodReturn
            // we set it false because we know that from the moment that exception occurs the method returns false
            // check at CustomerInvoice class at line 354, we can see there the alert that we want to display
            methodReturn = false;
        }

        // then
        assertEquals(false, methodReturn);;

    }

    @Test
    void testCheckBeforeInsertionWhenDocumentIsReceiptWithNoDeliveryReturnsTrue(){

        // given
        String invoiceTypeDescription = "RECEIPT OF SERVICE";
        Integer invoiceTypeID = 4;
        String licensePlate = "";
        Integer customerID = 1;

        // when
        boolean methodReturn = customerInvoice.checkBeforeInsertion(invoiceTypeDescription, invoiceTypeID, licensePlate, customerID);

        // then
        assertEquals(true, methodReturn);

    }


}