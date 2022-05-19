package kz.iitu.diploma.model.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "queryRequest", namespace = "osint")
@XmlAccessorType(XmlAccessType.FIELD)
public class  QueryDetail {

  public String     name;
  public Boolean    isVideo;
  public BigDecimal latitude;
  public BigDecimal longitude;

}
