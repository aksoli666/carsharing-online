package app.carsharing.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "cars")
@Getter
@Setter
@SQLDelete(sql = "UPDATE cars SET is_deleted=true WHERE id=?")
@SQLRestriction(value = "is_deleted=false")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String model;
    @Column(nullable = false)
    private String brand;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;
    @Column(nullable = false)
    private int inventory;
    @Column(nullable = false)
    private BigDecimal daileFee;
    @Column(nullable = false)
    private boolean isDeleted = false;

    public enum Type {
        SEDAN,
        SUV,
        HATCHBACK,
        UNIVERSAL
    }

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
        if (!(o instanceof Car)) {
            return false;
        }
        Car that = (Car) o;
        EqualsBuilder eb = new EqualsBuilder()
                .append(model, that.model)
                .append(brand, that.brand)
                .append(type, that.type)
                .append(inventory, that.inventory)
                .append(daileFee, that.daileFee);
        return eb.isEquals();
    }
}
