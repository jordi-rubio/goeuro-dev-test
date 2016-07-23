package de.jrubio.csv;

import au.com.bytecode.opencsv.CSVWriter;
import de.jrubio.goeuroposition.GeoPosition;
import de.jrubio.goeuroposition.GoEuroPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Component
public class CsvPositionWriterImpl implements PositionWriter {

  private static final Logger LOGGER = LoggerFactory.getLogger(CsvPositionWriterImpl.class);
  static final String OUTPUT_CSV_FILE_NAME = "output.csv";

  @Override
  public void write(final List<GoEuroPosition> positions) {
    CSVWriter writer = null;
    try {
      writer = new CSVWriter(new FileWriter(OUTPUT_CSV_FILE_NAME), ',');
      writeCsvHeader(writer);

      for (final GoEuroPosition p : positions) {
        final GeoPosition geoPosition = p.getGeoPosition();
        writer.writeNext(
            new String[]{
                String.valueOf(p.getId()),
                p.getName(),
                p.getType(),
                geoPosition != null ? String.valueOf(geoPosition.getLatitude()) : "",
                geoPosition != null ? String.valueOf(geoPosition.getLongitude()) : ""});
      }
    } catch (final IOException e) {
      LOGGER.error("Error writing csv!", e);
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (final IOException e) {
          LOGGER.error("Error closing csv writer!", e);
        }
      }
    }
  }

  private void writeCsvHeader(final CSVWriter writer) {
    writer.writeNext(new String[]{"id", "name", "type", "latitude", "longitude"});
  }
}
