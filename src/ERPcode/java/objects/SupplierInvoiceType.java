package objects;

public class SupplierInvoiceType {

    private Integer invoiceTypeID;
    private String abbreviationOfInvoiceType;
    private String description;

    public SupplierInvoiceType(){}

    public SupplierInvoiceType(Integer invoiceTypeID, String abbreviationOfInvoiceType, String description) {
        this.invoiceTypeID = invoiceTypeID;
        this.abbreviationOfInvoiceType = abbreviationOfInvoiceType;
        this.description = description;
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
}
