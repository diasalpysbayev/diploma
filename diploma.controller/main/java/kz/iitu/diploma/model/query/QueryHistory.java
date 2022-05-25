package kz.iitu.diploma.model.query;

import kz.iitu.diploma.model.search_engine.SearchInformation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.List;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class QueryHistory {

  public List<SearchInformation> searchInformationList;

}
