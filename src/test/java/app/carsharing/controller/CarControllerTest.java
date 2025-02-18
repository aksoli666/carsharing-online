package app.carsharing.controller;

import static app.carsharing.util.ConstantUtil.ADD_CARS_SQL;
import static app.carsharing.util.ConstantUtil.ADD_CAR_FOR_UPDATE_SQL;
import static app.carsharing.util.ConstantUtil.ADD_CAR_SQL;
import static app.carsharing.util.ConstantUtil.COUNT_CONTENT_2;
import static app.carsharing.util.ConstantUtil.DELETE_CARS_SQL;
import static app.carsharing.util.ConstantUtil.DELETE_CAR_SQL;
import static app.carsharing.util.ConstantUtil.DELETE_UPD_CAR_SQL;
import static app.carsharing.util.ConstantUtil.ID_10L_CORRECT;
import static app.carsharing.util.ConstantUtil.ID_15L_CORRECT;
import static app.carsharing.util.ConstantUtil.ID_16L_CORRECT;
import static app.carsharing.util.ConstantUtil.UPDATE_CAR_SQL;
import static app.carsharing.util.ConstantUtil.URL_CARS_WITHOUT_ID;
import static app.carsharing.util.ConstantUtil.URL_CARS_WITH_ID;
import static app.carsharing.util.ConstantUtil.pageable;
import static app.carsharing.util.EntityAndDtoMaker.createCarDto10L;
import static app.carsharing.util.EntityAndDtoMaker.createCarDto16L;
import static app.carsharing.util.EntityAndDtoMaker.createCarDto17L;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;

import app.carsharing.config.CustomPageImpl;
import app.carsharing.dto.CarDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.sql.DataSource;
import java.math.BigDecimal;
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
        scripts = ADD_CARS_SQL,
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
@Sql(
        scripts = DELETE_CARS_SQL,
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarControllerTest {
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

    @Sql(
            scripts = ADD_CAR_SQL,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_CAR_SQL,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @WithMockUser(username = "manager", roles = "MANAGER")
    @Test
    @DisplayName("Create a new car")
    public void createCar_validDto_success() throws Exception {
        CarDto expected = createCarDto10L();
        String json = objectMapper.writeValueAsString(expected);

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post(URL_CARS_WITHOUT_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();


        CarDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CarDto.class);
        assertTrue(reflectionEquals(expected, actual));
    }

    @WithMockUser(username = "customer", roles = "CUSTOMER")
    @Test
    @DisplayName("Get car by id")
    public void getCarById_validId_success() throws Exception {
        CarDto expected = createCarDto16L();

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.get(URL_CARS_WITH_ID, ID_16L_CORRECT)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        CarDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CarDto.class);
        assertTrue(reflectionEquals(expected, actual));
    }

    @WithMockUser(username = "customer", roles = "CUSTOMER")
    @Test
    @DisplayName("Get all cars")
    public void getAll_givenCarsInCatalog_returnPageDtos() throws Exception {
        CarDto first = createCarDto16L();
        CarDto second = createCarDto17L();
        PageImpl<CarDto> expected = new PageImpl<>(List.of(first, second), pageable, COUNT_CONTENT_2);

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.get(URL_CARS_WITHOUT_ID)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        CustomPageImpl<CarDto> actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructParametricType(CustomPageImpl.class, CarDto.class));
        assertTrue(reflectionEquals(expected.getContent().get(0).getModel(),
                actual.getContent().get(0).getModel()));
        assertTrue(reflectionEquals(expected.getContent().get(1).getModel(),
                actual.getContent().get(1).getModel()));
    }

    @WithMockUser(username = "manager", roles = "MANAGER")
    @Test
    @Sql(
            scripts = ADD_CAR_FOR_UPDATE_SQL,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = UPDATE_CAR_SQL,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_UPD_CAR_SQL,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Update car by id")
    public void update_validRequestDto_success() throws Exception {
        CarDto expected = new CarDto();
        expected.setModel("updated");
        expected.setBrand("brand10");
        expected.setType("UNIVERSAL");
        expected.setInventory(5);
        expected.setDaileFee(BigDecimal.valueOf(50.99));
        String json = objectMapper.writeValueAsString(expected);

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.put(URL_CARS_WITH_ID, ID_15L_CORRECT)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        CarDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CarDto.class);
        assertTrue(reflectionEquals(expected, actual));
    }


    @WithMockUser(username = "manager", roles = "MANAGER")
    @Test
    @DisplayName("Delete car by id")
    public void delete_validId_noContent() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete(URL_CARS_WITH_ID, ID_10L_CORRECT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
