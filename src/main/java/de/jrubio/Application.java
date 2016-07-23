package de.jrubio;

import de.jrubio.csv.PositionWriter;
import de.jrubio.goeuroposition.GoEuroPosition;
import de.jrubio.goeuroposition.GoEuroPositionClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class Application implements ApplicationRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

  @Autowired
  private PositionWriter positionWriter;

  @Autowired
  private GoEuroPositionClient goEuroPositionClient;

  public static void main(final String[] args) {
    if (args.length < 1) {
      LOGGER.error("Invalid usage!");
      LOGGER.info("Expected: java -jar GoEuroTest.jar \"CITY_NAME\"");
      return;
    }
    SpringApplication.run(Application.class, args);
  }

  @Override
  public void run(final ApplicationArguments applicationArguments) throws Exception {
    final List<GoEuroPosition> positions = getPositions(applicationArguments.getNonOptionArgs().get(0));
    writeCsv(positions);
  }

  private void writeCsv(final List<GoEuroPosition> positions) {
    positionWriter.write(positions);
  }

  private List<GoEuroPosition> getPositions(final String arg) {
    return goEuroPositionClient.getPositions(arg);
  }
}