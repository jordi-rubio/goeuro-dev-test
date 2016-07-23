package de.jrubio.goeuroposition;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static de.jrubio.goeuroposition.GoEuroPositionClientImpl.CITY_NAME_PARAMETER_NAME;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GoEuroPositionClientImplTest {
  private static final String BERLIN = "Berlin";
  public static final String TEST_URL = "http://test-url/position/" + CITY_NAME_PARAMETER_NAME;

  @Mock
  private RestTemplate restTemplate;

  @InjectMocks
  private GoEuroPositionClientImpl goEuroPositionClient;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private List<GoEuroPosition> positions;

  @Before
  public void setUp() throws Exception {
    Whitebox.setInternalState(goEuroPositionClient, "url", TEST_URL);

  }

  @Test
  public void shouldGetDataFromApi() throws Exception {
    givenPositionData();
    whenGettingPosition();
    thenPositonsWereRead();
  }

  @Test
  public void shouldHandleErrorFromApi() throws Exception {
    givenErrorFromApi();
    thenCustomExceptionIsExpected();
    whenGettingPosition();
  }

  private void thenCustomExceptionIsExpected() {
    thrown.expect(GoEuroPositionException.class);
  }

  private void thenEmptyArrayIsReturned() {
    assertThat(positions, hasSize(0));
  }

  private void thenPositonsWereRead() {
    assertThat(positions, hasSize(2));
    assertThat(positions.get(0).getName(), is(BERLIN));
    // A Custom Matcher could be used here
  }

  private void whenGettingPosition() {
    positions = goEuroPositionClient.getPositions(BERLIN);
  }

  private void givenErrorFromApi() {
    when(restTemplate.exchange(
        eq(TEST_URL.replace(CITY_NAME_PARAMETER_NAME, BERLIN)),
        eq(HttpMethod.GET),
        isNull(HttpEntity.class),
        any(ParameterizedTypeReference.class),
        (String) Matchers.<String>anyVararg())).thenReturn(
        new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR));
  }

  private void givenPositionData() throws Exception {
    final LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    headers.add("Content-Type", "application/json;charset=UTF-8");
    when(restTemplate.exchange(
        eq(TEST_URL.replace(CITY_NAME_PARAMETER_NAME, BERLIN)),
        eq(HttpMethod.GET),
        isNull(HttpEntity.class),
        any(ParameterizedTypeReference.class),
        (String) Matchers.<String>anyVararg())).thenReturn(
        new ResponseEntity<>(new ObjectMapper().readValue(getJsonResult(), new TypeReference<List<GoEuroPosition>>() {
        }), headers, HttpStatus.OK));
  }

  private String getJsonResult() throws IOException, URISyntaxException {
    return new String(Files.readAllBytes(Paths.get(this.getClass().getResource("/result.json").toURI())));
  }
}