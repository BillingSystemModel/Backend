package ru.trkpo.common;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"ru.trkpo.common"})
@EntityScan(basePackages = {"ru.trkpo.common"})
@ComponentScan(basePackages = {"ru.trkpo.common"})
public class CommonConfig {
}
