package app.carsharing.dto.responce;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Getter
@Setter
public class RentalAddResponseDto {
    private LocalDate rentalDate;
    private LocalDate returnDate;
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
        if (!(o instanceof RentalAddResponseDto)) {
            return false;
        }
        RentalAddResponseDto that = (RentalAddResponseDto) o;
        EqualsBuilder eb = new EqualsBuilder()
                .append(rentalDate, that.rentalDate)
                .append(returnDate, that.returnDate)
                .append(carId, that.carId);
        return eb.isEquals();
    }
}
