package com.flockinger.kryptovoyant.scraper.resources;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flockinger.kryptovoyant.scraper.exception.ReaderException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class JsonFileReader implements ResourceReader {

  private final ObjectMapper mapper;

  public JsonFileReader(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public <T> List<T> read(String resourceName, TypeReference<List<T>> type) {
    InputStream resourceStream = this.getClass().getResourceAsStream(resourceName);
    try {
      return mapper.readValue(resourceStream, type);
    } catch (IOException e) {
      throw new ReaderException("Cannot read/deserialize resource " + resourceName, e);
    }
  }

}
