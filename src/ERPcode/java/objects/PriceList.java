package objects;

public class PriceList {

    private Integer pricelist_id;
    private String pricelist_description;

    public PriceList(){} //empty constructor

    public PriceList(Integer pricelist_id, String pricelist_description){

        this.pricelist_id = pricelist_id;
        this.pricelist_description = pricelist_description;

    }

    public Integer getPricelist_id() {
        return pricelist_id;
    }

    public void setPricelist_id(Integer pricelist_id) {
        this.pricelist_id = pricelist_id;
    }

    public String getPricelist_description() {
        return pricelist_description;
    }

    public void setPricelist_description(String pricelist_description) {
        this.pricelist_description = pricelist_description;
    }
}
