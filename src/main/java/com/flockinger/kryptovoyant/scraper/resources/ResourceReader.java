package com.flockinger.kryptovoyant.scraper.resources;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;

public interface ResourceReader {

  <T> List<T> read(String resourceName, TypeReference<List<T>> type);
}
