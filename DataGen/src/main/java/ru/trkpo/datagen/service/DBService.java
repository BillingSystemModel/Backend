package ru.trkpo.datagen.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class DBService {

    private final DataSource dataSource;

    private final ResourceDatabasePopulator databasePopulator =
            new ResourceDatabasePopulator(false, false, "UTF-8",
                    new ClassPathResource("data.sql"));
    private final ResourceDatabasePopulator databaseCreator =
            new ResourceDatabasePopulator(true, false, "UTF-8",
                    new ClassPathResource("scheme.sql"));
    private final ResourceDatabasePopulator databaseDropper =
            new ResourceDatabasePopulator(false, false, "UTF-8",
                    new ClassPathResource("drop.sql"));

    public DBService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void populate() {
        databaseCreator.execute(dataSource);
        databasePopulator.execute(dataSource);
    }

    public void truncate() {
        databaseDropper.execute(dataSource);
        databaseCreator.execute(dataSource);
    }

    public void reset() {
        truncate();
        populate();
    }
}
