package app.carsharing.controller;

import static app.carsharing.util.ConstantUtil.ADD_CAR_FOR_RENTAL_SQL;
import static app.carsharing.util.ConstantUtil.ADD_RENTAL_FOR_SET_RETURN_SQL;
import static app.carsharing.util.ConstantUtil.ADD_USER_FOR_RENTAL_SQL;
import static app.carsharing.util.ConstantUtil.DELETE_RENTAL_CAR_SQL;
import static app.carsharing.util.ConstantUtil.DELETE_UPD_RENTAL_SQL;
import static app.carsharing.util.ConstantUtil.DELETE_USER_FOR_RENTAL_SQL;
import static app.carsharing.util.ConstantUtil.ID_30L_CORRECT;
import static app.carsharing.util.ConstantUtil.URL_RENTALS_WITH_ID;
import static app.carsharing.util.EntityAndDtoMaker.createRentalDto30L;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;

import app.carsharing.dto.RentalDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
        scripts = {ADD_CAR_FOR_RENTAL_SQL,
                ADD_USER_FOR_RENTAL_SQL,
                ADD_RENTAL_FOR_SET_RETURN_SQL},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
@Sql(
        scripts = {DELETE_UPD_RENTAL_SQL,
                DELETE_RENTAL_CAR_SQL,
                DELETE_USER_FOR_RENTAL_SQL},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RentalControllerTest {
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

    @WithMockUser(username = "email25@gmail.com", roles = "MANAGER")
    @Test
    @DisplayName("Set actual return date")
    public void setActualReturnDate_validParams_success() throws Exception {
        RentalDto expected = createRentalDto30L();

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.put(URL_RENTALS_WITH_ID, ID_30L_CORRECT)
                                .param("actual_return_date", "2024-11-30")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        RentalDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), RentalDto.class);
        assertTrue(reflectionEquals(expected.getActualReturnDate().toString(),
                actual.getActualReturnDate().toString()));
    }
}
