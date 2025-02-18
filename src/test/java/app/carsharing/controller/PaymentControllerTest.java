package app.carsharing.controller;

import static app.carsharing.util.ConstantUtil.ADD_CAR_FOR_PAYMENT_SQL;
import static app.carsharing.util.ConstantUtil.ADD_PAYMENT_SQL;
import static app.carsharing.util.ConstantUtil.ADD_RENTAL_FOR_PAYMENT_SQL;
import static app.carsharing.util.ConstantUtil.ADD_USER_FOR_PAYMENT_SQL;
import static app.carsharing.util.ConstantUtil.COUNT_CONTENT_1;
import static app.carsharing.util.ConstantUtil.DELETE_CAR_FOR_PAYMENT_SQL;
import static app.carsharing.util.ConstantUtil.DELETE_PAYMENT_SQL;
import static app.carsharing.util.ConstantUtil.DELETE_RENTAL_FOR_PAYMENT_SQL;
import static app.carsharing.util.ConstantUtil.DELETE_USER_FOR_PAYMENT_SQL;
import static app.carsharing.util.ConstantUtil.ID_40L_CORRECT;
import static app.carsharing.util.ConstantUtil.URL_PAYMENTS_WITHOUT_ID;
import static app.carsharing.util.ConstantUtil.URL_PAYMENTS_WITH_ID;
import static app.carsharing.util.ConstantUtil.pageable;
import static app.carsharing.util.EntityAndDtoMaker.createPaymentDto40L;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;

import app.carsharing.config.CustomPageImpl;
import app.carsharing.dto.PaymentDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Sql(
       scripts = {ADD_CAR_FOR_PAYMENT_SQL,
               ADD_USER_FOR_PAYMENT_SQL,
               ADD_RENTAL_FOR_PAYMENT_SQL,
               ADD_PAYMENT_SQL},
       executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
@Sql(
        scripts = {DELETE_CAR_FOR_PAYMENT_SQL,
                DELETE_USER_FOR_PAYMENT_SQL,
                DELETE_RENTAL_FOR_PAYMENT_SQL,
                DELETE_PAYMENT_SQL},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentControllerTest {
    private static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext webApplicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
        }
    }

    @WithMockUser(username = "email26@gmail.com", roles = "MANAGER")
    @Test
    @DisplayName("Get payment by id")
    public void getById_validId_returnPaymentDto() throws Exception {
        PaymentDto expected = createPaymentDto40L();

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.get(URL_PAYMENTS_WITH_ID, ID_40L_CORRECT)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        PaymentDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), PaymentDto.class);
        assertTrue(reflectionEquals(expected, actual));
    }

    @WithMockUser(username = "email26@gmail.com", roles = "CUSTOMER")
    @Test
    @DisplayName("Get payments")
    public void getPayments_validAuthentication_success() throws Exception {
        PaymentDto dto = createPaymentDto40L();

        PageImpl<PaymentDto> expected = new PageImpl<>(List.of(dto), pageable, COUNT_CONTENT_1);

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.get(URL_PAYMENTS_WITHOUT_ID)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Object actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructParametricType(CustomPageImpl.class, PaymentDto.class));
        assertTrue(reflectionEquals(expected, actual));
    }
}
