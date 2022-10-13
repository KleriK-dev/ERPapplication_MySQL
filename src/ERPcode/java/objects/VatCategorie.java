package objects;

public class VatCategorie {

    private Integer id;
    private String description;
    private double vatPercentage;

    public VatCategorie(){}

    public VatCategorie(Integer id, String description, double vatPercentage){

        this.id = id;
        this.description = description;
        this.vatPercentage = vatPercentage;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getVatPercentage() {
        return vatPercentage;
    }

    public void setVatPercentage(double vatPercentage) {
        this.vatPercentage = vatPercentage;
    }
}
