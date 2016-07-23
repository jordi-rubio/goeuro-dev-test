package de.jrubio.goeuroposition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoEuroPosition {
  @JsonProperty("_id")
  private long id;

  private String name;

  private String type;

  @JsonProperty("geo_position")
  private GeoPosition geoPosition;

  public long getId() {
    return id;
  }

  public void setId(final long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public GeoPosition getGeoPosition() {
    return geoPosition;
  }

  public void setGeoPosition(final GeoPosition geoPosition) {
    this.geoPosition = geoPosition;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final GoEuroPosition that = (GoEuroPosition) o;
    return id == that.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "GoEuroPosition{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", type='" + type + '\'' +
        ", geoPosition=" + geoPosition +
        '}';
  }
}
