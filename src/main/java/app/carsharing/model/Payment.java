package app.carsharing.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.URL;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;
    @OneToOne
    @MapsId
    @JoinColumn(name = "id", nullable = false)
    private Rental rental;
    @Column(nullable = false, unique = true)
    @URL
    private String sessionUrl;
    @Column(nullable = false, unique = true)
    private String sessionId;
    @Column(nullable = false)
    private BigDecimal amount;

    public enum Status {
        PENDING,
        PAID
    }

    public enum Type {
        PAYMENT,
        FINE
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder()
                .append(status)
                .append(type)
                .append(rental)
                .append(sessionUrl)
                .append(sessionId)
                .append(amount);
        return hcb.toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        Payment that = (Payment) o;
        EqualsBuilder eb = new EqualsBuilder()
                .append(status, that.status)
                .append(type, that.type)
                .append(rental, that.rental)
                .append(sessionUrl, that.sessionUrl)
                .append(sessionId, that.sessionId)
                .append(amount, that.amount);
        return eb.isEquals();
    }
}
