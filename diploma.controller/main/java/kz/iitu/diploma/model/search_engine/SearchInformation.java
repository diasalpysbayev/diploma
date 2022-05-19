package kz.iitu.diploma.model.search_engine;

import lombok.Builder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Builder
@XmlRootElement(name = "searchInformation")
public class SearchInformation {

  public String url;
  public String title;
  @XmlElement(name = "placeInfo")
  public PlaceInfo placeInfo;

}
