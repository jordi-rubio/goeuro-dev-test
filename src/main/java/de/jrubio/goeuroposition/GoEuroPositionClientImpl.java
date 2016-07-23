package de.jrubio.goeuroposition;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Component
public class GoEuroPositionClientImpl implements GoEuroPositionClient {
  private static final Logger LOGGER = LoggerFactory.getLogger(GoEuroPositionClientImpl.class);

  static final String CITY_NAME_PARAMETER_NAME = "CITY_NAME";

  @Autowired
  private RestTemplate restTemplate;

  @Value("${goeuro.position.url}")
  private String url;

  @HystrixCommand(fallbackMethod = "fallback")
  @Override
  public List<GoEuroPosition> getPositions(final String city) {
    try {
      final ResponseEntity<List<GoEuroPosition>> restResult = restTemplate.exchange(
          url.replace(CITY_NAME_PARAMETER_NAME, URLEncoder.encode(city, "UTF-8")),
          HttpMethod.GET,
          null,
          new ParameterizedTypeReference<List<GoEuroPosition>>() {
          });

      if (!restResult.getStatusCode().is2xxSuccessful()) {
        throw new GoEuroPositionException("Rest call wasn't successful. Status Code: " + restResult.getStatusCode());
      }
      return restResult.getBody();
    } catch (final UnsupportedEncodingException e) {
      LOGGER.error("Invalid encoding used! ", e);
    }

    return newArrayList();
  }

  private List<GoEuroPosition> fallback(final String city, final Throwable exception) {
    LOGGER.warn("Error getting positions for " + city, exception);
    return newArrayList();
  }
}
