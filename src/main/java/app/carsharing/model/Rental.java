package app.carsharing.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = "rentals")
@Getter
@Setter
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDate rentalDate;
    @Column(nullable = false)
    private LocalDate returnDate;
    private LocalDate actualReturnDate;
    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    private void validate() {
        if (actualReturnDate != null && actualReturnDate.isBefore(rentalDate)) {
            throw new IllegalArgumentException("Actual return date cannot be before rental date");
        }
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder()
                .append(rentalDate)
                .append(returnDate)
                .append(actualReturnDate)
                .append(car)
                .append(user);
        return hcb.toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rental)) {
            return false;
        }
        Rental that = (Rental) o;
        EqualsBuilder eb = new EqualsBuilder()
                .append(rentalDate, that.rentalDate)
                .append(returnDate, that.returnDate)
                .append(actualReturnDate, that.actualReturnDate)
                .append(car, that.rentalDate)
                .append(user, that.user);
        return eb.isEquals();
    }
}
