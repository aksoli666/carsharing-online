package app.carsharing.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Getter
@Setter
public class CarDto {
    private String model;
    private String brand;
    private String type;
    private int inventory;
    private BigDecimal daileFee;

    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder()
                .append(model)
                .append(brand)
                .append(type)
                .append(inventory)
                .append(daileFee);
        return hcb.toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarDto)) {
            return false;
        }
        CarDto that = (CarDto) o;
        EqualsBuilder eb = new EqualsBuilder()
                .append(model, that.model)
                .append(brand, that.brand)
                .append(type, that.type)
                .append(inventory, that.inventory)
                .append(daileFee, that.daileFee);
        return eb.isEquals();
    }
}
