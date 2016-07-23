package de.jrubio.csv;

import de.jrubio.goeuroposition.GoEuroPosition;

import java.util.List;

public interface PositionWriter {
  void write(List<GoEuroPosition> positions);
}
