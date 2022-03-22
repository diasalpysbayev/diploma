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

  @Insert("insert into query(id, client_id, valuestr) values (#{id}, #{clientId}, #{valuestr})")
  void saveQuery(@Param(value = "id") Long id,
                 @Param(value = "clientId") Long clientId,
                 @Param(value = "valuestr") String valuestr);

}
