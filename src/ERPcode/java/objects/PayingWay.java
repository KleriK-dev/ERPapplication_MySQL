package objects;

public class PayingWay {

    private Integer payingway_id;
    private String payingway_description;

    public PayingWay(){} //empty constructor

    public PayingWay(Integer payingway_id, String payingway_description){

        this.payingway_id = payingway_id;
        this.payingway_description = payingway_description;

    }

    public Integer getPayingway_id() {
        return payingway_id;
    }

    public void setPayingway_id(Integer payingway_id) {
        this.payingway_id = payingway_id;
    }

    public String getPayingway_description() {
        return payingway_description;
    }

    public void setPayingway_description(String payingway_description) {
        this.payingway_description = payingway_description;
    }
}
