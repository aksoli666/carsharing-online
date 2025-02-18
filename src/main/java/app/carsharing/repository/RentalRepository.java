package app.carsharing.repository;

import app.carsharing.model.Rental;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    @Query("SELECT DISTINCT r FROM Rental r "
            + "JOIN FETCH r.user u "
            + "JOIN FETCH  r.car c "
            + "WHERE r.user.id = :userId AND r.actualReturnDate IS NULL")
    Page<Rental> findByUserIdAndActualReturnDateIsNull(Long userId,
                                                          Pageable pageable);

    @Query("SELECT DISTINCT r FROM Rental r "
            + "JOIN FETCH r.user u "
            + "JOIN FETCH  r.car c "
            + "WHERE r.user.id = :userId AND r.actualReturnDate IS NOT NULL")
    Page<Rental> findByUserIdAndActualReturnDateIsNotNull(Long userId,
                                                          Pageable pageable);

    @Query("SELECT r FROM Rental r "
            + "JOIN FETCH r.user u "
            + "JOIN FETCH  r.car c "
            + "WHERE r.id = :id AND r.user.id = :userId")
    Optional<Rental> findByIdAndUserId(Long id, Long userId);

    List<Rental> findByReturnDateBeforeAndActualReturnDateIsNull(LocalDate date);

    List<Rental> findByReturnDateAfterOrActualReturnDateIsNotNull(LocalDate date);
}
