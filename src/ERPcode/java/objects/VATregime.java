package objects;

public class VATregime {

    private Integer vatregime_id;
    private String vatregime;

    public VATregime(){} //empty constructor

    public VATregime(Integer vatregime_id, String vatregime){

        this.vatregime_id = vatregime_id;
        this.vatregime = vatregime;

    }

    public Integer getVatregime_id() {
        return vatregime_id;
    }

    public void setVatregime_id(Integer vatregime_id) {
        this.vatregime_id = vatregime_id;
    }

    public String getVatregime() {
        return vatregime;
    }

    public void setVatregime(String vatregime) {
        this.vatregime = vatregime;
    }
}



