package kz.iitu.diploma.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface QueryDao {

  @Select("select nextval('query_id_seq') as id")
  Long nextQueryId();

  @Select("select nextval('query_info_id_seq') as id")
  Long nextQueryInfoId();

  @Insert("insert into query(id, client_id, valuestr) values (#{id}, #{clientId}, #{valuestr})")
  void saveQuery(@Param(value = "id") Long id,
                 @Param(value = "clientId") Long clientId,
                 @Param(value = "valuestr") String valuestr);

  @Insert("insert into query_info(id, query_id, title, url) values (#{id}, #{queryId}, #{title}, #{url})")
  void saveQueryInfo(@Param(value = "id") Long id,
                 @Param(value = "queryId") Long queryId,
                 @Param(value = "title") String title,
                 @Param(value = "url") String url);
}
