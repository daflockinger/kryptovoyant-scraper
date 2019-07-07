package com.flockinger.kryptovoyant.scraper.resources;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flockinger.kryptovoyant.scraper.exception.ReaderException;
import com.flockinger.kryptovoyant.scraper.resources.dto.CoinInfo;
import java.util.List;
import org.junit.Test;


public class JsonFileReaderTest {

  private ResourceReader reader = new JsonFileReader(new ObjectMapper());


  @Test
  public void testRead_withExistingFileAndCorrectType_shouldReadCorrectly() {

    List<CoinInfo> infos = reader.read("/links/CoinInfos.json", new TypeReference<>() {
    });

    assertThat(infos.get(0).getSymbol()).isNotBlank();

    assertThat(infos).isNotNull().hasSize(2250)
        .extracting("symbol", "website", "sources", "documentation")
        .contains(tuple("BTC", "https://bitcoin.org/", "https://github.com/bitcoin/",
            "https://bitcoin.org/bitcoin.pdf"));
  }

  @Test(expected = ReaderException.class)
  public void testRead_withNonExistingFile_shouldThrowException() {
    reader.read("/links/FunkyCoinInfos.json", new TypeReference<List<CoinInfo>>() {
    });
  }

}