package com.betting.testcontainers;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@Log4j2
public class KafkaTestContainerExtension implements BeforeAllCallback {

  public static final KafkaContainer container =
    new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka").withTag("7.5.0"))
      .withReuse(Boolean.TRUE)
      .withKraft();

  private void setupProperties() {
    System.setProperty("spring.kafka.bootstrap-servers", container.getBootstrapServers());
  }

  @Override
  public void beforeAll(ExtensionContext context) {
    container.start();
    setupProperties();
  }
}
