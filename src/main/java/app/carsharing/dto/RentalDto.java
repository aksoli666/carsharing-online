package app.carsharing.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Getter
@Setter
public class RentalDto {
    private LocalDate rentalDate;
    private LocalDate returnDate;
    private LocalDate actualReturnDate;
    private Long carId;
    private Long userId;

    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder()
                .append(rentalDate)
                .append(returnDate)
                .append(actualReturnDate)
                .append(carId)
                .append(userId);
        return hcb.toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RentalDto)) {
            return false;
        }
        RentalDto that = (RentalDto) o;
        EqualsBuilder eb = new EqualsBuilder()
                .append(rentalDate, that.rentalDate)
                .append(returnDate, that.returnDate)
                .append(actualReturnDate, that.actualReturnDate)
                .append(carId, that.carId)
                .append(userId, that.userId);
        return eb.isEquals();
    }
}
