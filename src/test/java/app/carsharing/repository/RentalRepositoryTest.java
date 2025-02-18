package app.carsharing.repository;

import static app.carsharing.util.ConstantUtil.ADD_CAR_REPO;
import static app.carsharing.util.ConstantUtil.ADD_RENTAL_ID42_REPO;
import static app.carsharing.util.ConstantUtil.ADD_RENTAL_ID43_REPO;
import static app.carsharing.util.ConstantUtil.ADD_RENTAL_ID44_REPO;
import static app.carsharing.util.ConstantUtil.ADD_USER_REPO;
import static app.carsharing.util.ConstantUtil.COUNT_CONTENT_1;
import static app.carsharing.util.ConstantUtil.ID_1L_CORRECT;
import static app.carsharing.util.ConstantUtil.ID_42L_CORRECT;
import static app.carsharing.util.ConstantUtil.ID_43L_CORRECT;
import static app.carsharing.util.ConstantUtil.ID_44L_CORRECT;
import static app.carsharing.util.ConstantUtil.pageable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import app.carsharing.model.Rental;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RentalRepositoryTest {
    @Autowired
    private RentalRepository rentalRepository;

    @Test
    @DisplayName("""
            Verify #findByUserIdAndActualReturnDateIsNull, return Page<Rental>
            """)
    @Sql(statements = {ADD_USER_REPO,
            ADD_CAR_REPO,
            ADD_RENTAL_ID42_REPO})
    void findByUserIdAndActualReturnDateIsNull_correctId_returnPageRental() {
        Page<Rental> rentals = rentalRepository
                .findByUserIdAndActualReturnDateIsNull(ID_1L_CORRECT, pageable);

        assertNotNull(rentals);
        assertEquals(COUNT_CONTENT_1, rentals.getTotalElements());
        assertEquals(ID_42L_CORRECT, rentals.getContent().get(0).getId());
    }

    @Test
    @DisplayName("""
            Verify #findByUserIdAndActualReturnDateIsNotNull, return Page<Rental>
            """)
    @Sql(statements = {ADD_USER_REPO,
            ADD_CAR_REPO,
            ADD_RENTAL_ID43_REPO})
    void findByUserIdAndActualReturnDateIsNotNull_correctId_returnPageRental() {
        Page<Rental> rentals = rentalRepository
                .findByUserIdAndActualReturnDateIsNotNull(ID_1L_CORRECT, pageable);

        assertNotNull(rentals);
        assertEquals(COUNT_CONTENT_1, rentals.getTotalElements());
        assertEquals(ID_43L_CORRECT, rentals.getContent().get(0).getId());
    }

    @Test
    @DisplayName("""
            Verify #findByIdAndUserId, return Rental
            """)
    @Sql(statements = {ADD_USER_REPO,
            ADD_CAR_REPO,
            ADD_RENTAL_ID44_REPO})
    void findByIdAndUserId_correctId_returnRental() {
        Optional<Rental> rental = rentalRepository
                .findByIdAndUserId(ID_44L_CORRECT, ID_1L_CORRECT);

        assertNotNull(rental.isPresent());
        assertEquals(ID_44L_CORRECT, rental.get().getId());
        assertEquals(ID_1L_CORRECT, rental.get().getUser().getId());
    }
}
