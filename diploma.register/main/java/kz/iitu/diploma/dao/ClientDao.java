package kz.iitu.diploma.dao;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientDao {

  @Select("select nextval('client_id_seq') as id")
  Long nextClientId();

}
