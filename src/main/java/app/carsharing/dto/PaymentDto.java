package app.carsharing.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Getter
@Setter
public class PaymentDto {
    private String status;
    private String type;
    private Long rentalId;
    private BigDecimal amount;

    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder()
                .append(status)
                .append(type)
                .append(rentalId)
                .append(amount);
        return hcb.toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentDto)) {
            return false;
        }
        PaymentDto that = (PaymentDto) o;
        EqualsBuilder eb = new EqualsBuilder()
                .append(status, that.status)
                .append(type, that.type)
                .append(rentalId, that.rentalId)
                .append(amount, that.amount);
        return eb.isEquals();
    }
}
