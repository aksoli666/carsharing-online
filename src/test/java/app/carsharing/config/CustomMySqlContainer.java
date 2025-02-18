package app.carsharing.config;

import org.testcontainers.containers.MySQLContainer;

public class CustomMySqlContainer extends MySQLContainer<CustomMySqlContainer> {
    private static final String DB_IMAGE = "mysql:8";
    private static CustomMySqlContainer container;

    private CustomMySqlContainer() {
        super(DB_IMAGE);
    }

    public static synchronized CustomMySqlContainer getInstance() {
        if (container == null) {
            container = new CustomMySqlContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("TEST_DB_URL", container.getJdbcUrl());
        System.setProperty("TEST_DB_USERNAME", container.getUsername());
        System.setProperty("TEST_DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        super.stop();
    }
}
