package kz.iitu.diploma.model.search_engine;

import lombok.Builder;

@Builder
public class SearchInformation {

  public String url;
  public String title;
  public PlaceInfo placeInfo;

}
