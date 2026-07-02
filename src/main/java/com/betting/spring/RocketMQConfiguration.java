package com.betting.spring;

import com.alibaba.cloud.stream.binder.rocketmq.convert.RocketMQMessageConverter;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.MessageConverter;

@Configuration
class RocketMQConfiguration {

  /*
   * exclude fastjson from list of converters, as we want to use Jackson across the application
   * */
  @Bean
  @Primary
  RocketMQMessageConverter rocketMQMessageConverter() {
    RocketMQMessageConverter original = new RocketMQMessageConverter();
    CompositeMessageConverter composite = original.getMessageConverter();

    List<MessageConverter> filtered =
        composite.getConverters().stream()
            .filter(c -> !c.getClass().getName().startsWith("com.alibaba.fastjson"))
            .toList();

    return new RocketMQMessageConverter() {
      @Override
      public CompositeMessageConverter getMessageConverter() {
        return new CompositeMessageConverter(filtered);
      }
    };
  }
}
