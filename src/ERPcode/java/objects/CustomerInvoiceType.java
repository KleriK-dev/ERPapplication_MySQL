package objects;

public class CustomerInvoiceType {

    private Integer invoiceTypeID;
    private String abbreviationOfInvoiceType;
    private String description;
    private Integer delivery_check;

    public CustomerInvoiceType(){} //empty constructor

    public CustomerInvoiceType(Integer invoiceTypeID, String abbreviationOfInvoiceType, String description, Integer delivery_check){

        this.invoiceTypeID = invoiceTypeID;
        this.abbreviationOfInvoiceType = abbreviationOfInvoiceType;
        this.description = description;
        this.delivery_check = delivery_check;

    }

    public Integer getInvoiceTypeID() {
        return invoiceTypeID;
    }

    public void setInvoiceTypeID(Integer invoiceTypeID) {
        this.invoiceTypeID = invoiceTypeID;
    }

    public String getAbbreviationOfInvoiceType() {
        return abbreviationOfInvoiceType;
    }

    public void setAbbreviationOfInvoiceType(String abbreviationOfInvoiceType) {
        this.abbreviationOfInvoiceType = abbreviationOfInvoiceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDelivery_check() {
        return delivery_check;
    }

    public void setDelivery_check(Integer delivery_check) {
        this.delivery_check = delivery_check;
    }
}
