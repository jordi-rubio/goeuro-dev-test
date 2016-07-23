package de.jrubio.csv;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.jrubio.goeuroposition.GoEuroPosition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static de.jrubio.csv.CsvPositionWriterImpl.OUTPUT_CSV_FILE_NAME;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CsvPositionWriterImpl.class)
public class CsvPositionWriterImplTest {

  @Mock
  private FileWriter fileWriter;

  @Captor
  private ArgumentCaptor<String> writerCaptor;

  private final CsvPositionWriterImpl positionWriter = new CsvPositionWriterImpl();

  @Test
  public void shouldWritePositionsToFile() throws Exception {
    prepareWriterMock();

    whenWritingPositions();

    thenPositionsWereWriten();
    thenWriterIsClosed();
  }

  private void thenWriterIsClosed() throws IOException {
    verify(fileWriter, times(2)).close();
  }

  private void thenPositionsWereWriten() throws IOException {
    verify(fileWriter, times(3)).write(writerCaptor.capture(), anyInt(), anyInt());
    final List<String> writenValues = writerCaptor.getAllValues();
    assertThat(writenValues, hasSize(3));
    assertThat(writenValues.get(0), is("\"id\",\"name\",\"type\",\"latitude\",\"longitude\"\n"));
    assertThat(writenValues.get(1), is("\"376217\",\"Berlin\",\"location\",\"52.52437\",\"13.41053\"\n"));
    assertThat(writenValues.get(2), is("\"448103\",\"Berlingo\",\"location\",\"45.50298\",\"10.04366\"\n"));
  }

  private void whenWritingPositions() throws IOException, URISyntaxException {
    positionWriter.write(getPositions());
  }

  private void prepareWriterMock() throws Exception {
    PowerMockito.whenNew(FileWriter.class).withArguments(OUTPUT_CSV_FILE_NAME).thenReturn(fileWriter);
  }

  private List<GoEuroPosition> getPositions() throws IOException, URISyntaxException {
    final String json = new String(Files.readAllBytes(Paths.get(this.getClass().getResource("/result.json").toURI())));
    return new ObjectMapper().readValue(json, new TypeReference<List<GoEuroPosition>>() {
    });
  }

}