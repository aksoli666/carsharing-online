package app.carsharing.repository;

import app.carsharing.model.Payment;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT DISTINCT p FROM Payment p "
            + "JOIN FETCH p.rental r "
            + "WHERE r.user.id = :userId")
    Page<Payment> findByRentalUserIdFetchRental(Long userId, Pageable pageable);

    Optional<Payment> findBySessionId(String sessionId);
}
