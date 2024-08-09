package com.github.xxdanielngoxx.hui.api;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration(proxyBeanMethods = false)
@Import(PostgresTestcontainerConfiguration.class)
class TestcontainersConfiguration {}
