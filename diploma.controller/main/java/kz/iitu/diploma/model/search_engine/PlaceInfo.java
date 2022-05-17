package kz.iitu.diploma.model.search_engine;

import kz.iitu.diploma.util.Json;
import lombok.Builder;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@Builder
@Json
@XmlRootElement(name = "placeInfo")
public class PlaceInfo {

  public BigDecimal latitude;
  public BigDecimal longitude;
  public BigDecimal rating;
  public String     description;
  public String     address;
  public String     phone;

  public PlaceInfo(BigDecimal latitude, BigDecimal longitude, BigDecimal rating, String description, String address, String phone) {
    this.latitude    = latitude;
    this.longitude   = longitude;
    this.rating      = rating;
    this.description = description;
    this.address     = address;
    this.phone       = phone;
  }
}
