package kz.iitu.diploma.dao;

import kz.iitu.diploma.model.auth.UserInfo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientDao {

  @Select("select nextval('client_id_seq') as id")
  Long nextClientId();

  @Select("select cks.id from client_key_storage cks " +
      "left join client c on c.id = cks.client_id and c.actual = true and c.phone_number = #{phoneNumber}")
  String getSecretKeyId(String phoneNumber);

  @Select("select case when status = 'BLOCKED' then true else false end\n" +
      "from client\n" +
      "where id = #{id}")
  Boolean checkBlockedUser(Long id);

  @Select("select email from client where id=#{id}")
  String getEmail(Long id);

  @Select("select client.id, phone_number as phoneNumber, name, surname, email \n" +
      "from client \n" +
      "left join client_token_storage ct on ct.client_id = client.id and ct.actual = true\n" +
      "where ct.id=#{tokenId} and client.actual=true")
  UserInfo getUserInfo(String tokenId);
}
