package app.carsharing.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Getter
@Setter
public class RentalAddRequestDto {
    @NotNull
    private LocalDate rentalDate;
    @NotNull
    private LocalDate returnDate;
    @NotNull
    private Long carId;

    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder()
                .append(rentalDate)
                .append(returnDate)
                .append(carId);
        return hcb.toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RentalAddRequestDto)) {
            return false;
        }
        RentalAddRequestDto that = (RentalAddRequestDto) o;
        EqualsBuilder eb = new EqualsBuilder()
                .append(rentalDate, that.rentalDate)
                .append(returnDate, that.returnDate)
                .append(carId, that.carId);
        return eb.isEquals();
    }
}
