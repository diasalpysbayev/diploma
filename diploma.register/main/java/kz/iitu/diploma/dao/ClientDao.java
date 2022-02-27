package kz.iitu.diploma.dao;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientDao {

  @Select("select nextval('client_id_seq') as id")
  Long nextClientId();

  @Select("select id from client_token_storage where client_id=#{id}")
  String getSecretKeyId(Long id);

  @Select("select email from client where client_id=#{id}")
  String getEmail(Long id);

}
