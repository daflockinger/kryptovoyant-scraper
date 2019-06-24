package com.flockinger.kryptovoyant.scraper.messaging;

import static org.assertj.core.api.Assertions.assertThat;

import com.flockinger.kryptovoyant.scraper.client.metadata.dto.Coin;
import com.flockinger.kryptovoyant.scraper.exception.MessagingException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;


public class KafkaSenderTest {

  private Sender sender;

  private final static String topic = "meta-topic";

  @ClassRule
  public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, true, topic);
  private static KafkaMessageListenerContainer<String, Coin> messageListener;
  private static BlockingQueue<ConsumerRecord<String, Coin>> records;
  private static KafkaTemplate template;

  @BeforeClass
  public static void setup() {
    records = new LinkedBlockingQueue<>();

    // set up message listener
    Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("sender", "false",
        embeddedKafka.getEmbeddedKafka());
    consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    ConsumerFactory<String, Coin> consumerFactory = new DefaultKafkaConsumerFactory<>(
        consumerProps, new StringDeserializer(), new JsonDeserializer<>(Coin.class));
    messageListener = new KafkaMessageListenerContainer<>(consumerFactory,
        new ContainerProperties(topic));
    messageListener
        .setupMessageListener((MessageListener<String, Coin>) record -> {
          records.add(record);
          System.out.println("RECEIVED_STUFF!!");
        });
    messageListener.start();

    ContainerTestUtils.waitForAssignment(messageListener,
        embeddedKafka.getEmbeddedKafka().getPartitionsPerTopic());

    // set up sender
    Map<String, Object> senderProperties =
        KafkaTestUtils.senderProps(embeddedKafka.getEmbeddedKafka().getBrokersAsString());
    senderProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    senderProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

    ProducerFactory<String, Object> producerFactory
        = new DefaultKafkaProducerFactory<>(senderProperties);
    template = new KafkaTemplate<>(producerFactory);
  }

  @Before
  public void before() {
    sender = new KafkaSender(template);
  }

  @Test
  public void testSendMessage_withValidMessage_shouldSendSuccessfully() throws Exception {
    Coin bitcoin = new Coin();
    bitcoin.setSymbol("BTC");
    bitcoin.setCurrencyName("Bitcoin");
    bitcoin.setTradeCount(123L);

    sender.sendMessage(bitcoin, topic);

    ConsumerRecord<String, Coin> received = records.poll(10, TimeUnit.SECONDS);

    assertThat(received.value())
        .isNotNull()
        .extracting("symbol", "currencyName", "tradeCount")
        .containsExactly("BTC", "Bitcoin", 123L);
  }

  @Test(expected = MessagingException.class)
  public void testSendMessage_withNullMessage_shouldThrowException() throws Exception {
    sender.sendMessage(null, topic);
  }

  @Test(expected = MessagingException.class)
  public void testSendMessage_withEmptyTopic_shouldThrowException() throws Exception {
    Coin bitcoin = new Coin();
    bitcoin.setSymbol("BTC");
    bitcoin.setCurrencyName("Bitcoin");
    bitcoin.setTradeCount(123L);

    sender.sendMessage(bitcoin, "");
  }

  @Test(timeout = 500L)
  public void testSendMessage_withNoneExistingTopic_shouldNotLoopForever() throws Exception {
    Coin bitcoin = new Coin();
    bitcoin.setSymbol("BTC");
    bitcoin.setCurrencyName("Bitcoin");
    bitcoin.setTradeCount(123L);

    sender.sendMessage(bitcoin, "non-existante");
  }

}