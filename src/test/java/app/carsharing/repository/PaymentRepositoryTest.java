package app.carsharing.repository;

import static app.carsharing.util.ConstantUtil.ADD_CAR_REPO;
import static app.carsharing.util.ConstantUtil.ADD_PAYMENT_REPO;
import static app.carsharing.util.ConstantUtil.ADD_RENTAL_PAYMENT_REPO;
import static app.carsharing.util.ConstantUtil.ADD_USER_REPO;
import static app.carsharing.util.ConstantUtil.ID_1L_CORRECT;
import static app.carsharing.util.ConstantUtil.pageable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import app.carsharing.model.Payment;
import app.carsharing.util.ConstantUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PaymentRepositoryTest {
    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    @DisplayName("""
            Verify #findByRentalUserIdFetchRental(), return Page<Book>
            """)
    @Sql(statements = {ADD_USER_REPO,
            ADD_CAR_REPO,
            ADD_RENTAL_PAYMENT_REPO,
            ADD_PAYMENT_REPO})
    void findByRentalUserIdFetchRental_correctId_returnPageBook() {
        Page<Payment> payments = paymentRepository.findByRentalUserIdFetchRental(
                ID_1L_CORRECT, pageable);

        assertNotNull(payments);
        assertEquals(ConstantUtil.COUNT_CONTENT_1, payments.getTotalElements());
        assertEquals(ConstantUtil.ID_41L_CORRECT, payments.getContent().get(0).getId());
    }
}
