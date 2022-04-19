package kz.iitu.diploma.model.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryRecord {

  public List<QueryDetail> queryList;

}
