package kz.iitu.diploma.model.query;

import kz.iitu.diploma.model.search_engine.SearchInformation;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "result")
public class QuerySoap {
  public List<SearchInformation> list = new ArrayList<>();
}
