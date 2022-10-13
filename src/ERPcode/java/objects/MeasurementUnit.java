package objects;

public class MeasurementUnit {

    private Integer measurement_id;
    private String measurement_code;
    private String measurement_description;

    public MeasurementUnit(){} //empty constructor

    public MeasurementUnit(Integer measurement_id, String measurement_code, String measurement_description){

        this.measurement_id = measurement_id;
        this.measurement_code = measurement_code;
        this.measurement_description = measurement_description;

    }

    public Integer getMeasurement_id() {
        return measurement_id;
    }

    public void setMeasurement_id(Integer measurement_id) {
        this.measurement_id = measurement_id;
    }

    public String getMeasurement_code() {
        return measurement_code;
    }

    public void setMeasurement_code(String measurement_code) {
        this.measurement_code = measurement_code;
    }

    public String getMeasurement_description() {
        return measurement_description;
    }

    public void setMeasurement_description(String measurement_description) {
        this.measurement_description = measurement_description;
    }
}
