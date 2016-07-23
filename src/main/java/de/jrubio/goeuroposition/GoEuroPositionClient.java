package de.jrubio.goeuroposition;

import java.util.List;

public interface GoEuroPositionClient {
  List<GoEuroPosition> getPositions(String city) throws GoEuroPositionException;
}
