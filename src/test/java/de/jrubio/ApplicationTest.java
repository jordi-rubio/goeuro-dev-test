package de.jrubio;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.jrubio.csv.PositionWriter;
import de.jrubio.goeuroposition.GoEuroPosition;
import de.jrubio.goeuroposition.GoEuroPositionClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.ApplicationArguments;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationTest {

  public static final String BERLIN = "Berlin";
  @Mock
  private PositionWriter positionWriter;

  @Mock
  private GoEuroPositionClient goEuroPositionClient;

  @InjectMocks
  private Application application;

  private List<GoEuroPosition> positions;


  @Test
  public void shouldGetPositionsAndWriteACsv() throws Exception {
    givenPositions();

    whenRunningApp();

    thenPositionsAreWriten();
  }

  private void thenPositionsAreWriten() {
    verify(positionWriter).write(positions);
  }

  private void whenRunningApp() throws Exception {
    application.run(new ApplicationArguments() {
      @Override
      public String[] getSourceArgs() {
        return new String[0];
      }

      @Override
      public Set<String> getOptionNames() {
        return null;
      }

      @Override
      public boolean containsOption(final String s) {
        return false;
      }

      @Override
      public List<String> getOptionValues(final String s) {
        return null;
      }

      @Override
      public List<String> getNonOptionArgs() {
        return newArrayList(BERLIN);
      }
    });
  }

  private void givenPositions() throws Exception {
    final String json = new String(Files.readAllBytes(Paths.get(this.getClass().getResource("/result.json").toURI())));
    positions = new ObjectMapper().readValue(json, new TypeReference<List<GoEuroPosition>>() {
    });
    when(goEuroPositionClient.getPositions(BERLIN)).thenReturn(positions);
  }
}