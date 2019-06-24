package com.flockinger.kryptovoyant.scraper.messaging;

public interface Sender {

  <T extends Object> void sendMessage(T message, String topic);

}
