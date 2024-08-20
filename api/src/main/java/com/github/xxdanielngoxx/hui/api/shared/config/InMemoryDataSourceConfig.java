package com.github.xxdanielngoxx.hui.api.shared.config;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Profile("standalone")
@Configuration(proxyBeanMethods = false)
public class InMemoryDataSourceConfig {
  @Bean
  public DataSource dataSource() {
    return new EmbeddedDatabaseBuilder()
        .generateUniqueName(true)
        .setType(EmbeddedDatabaseType.HSQL)
        .setScriptEncoding("UTF-8")
        .ignoreFailedDrops(true)
        .build();
  }
}
