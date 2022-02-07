package kz.iitu.diploma.dao;

import kz.iitu.diploma.model.auth.AuthDetail;
import kz.iitu.diploma.model.auth.ClientRegisterRecord;
import kz.iitu.diploma.model.auth.SessionInfo;
import kz.iitu.diploma.model.auth.SmsRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface AuthDao {

  @Insert("insert into sms_verification(id, phone_number, code) " +
      "values(nextval('sms_verification_id_seq'), #{phoneNumber}, #{code})")
  void insertSmsCode(SmsRecord smsRecord);

  @Select("with x as (select code\n" +
      "           from sms_verification\n" +
      "           where phone_number = #{phoneNumber}\n" +
      "             and actual = true\n" +
      "           order by createdat desc\n" +
      "           limit 1)\n" +
      "select case when x.code = #{code} then true else false end\n" +
      "from x;")
  boolean checkCode(SmsRecord smsRecord);

  @Select("select id, phone from client where phone=#{phone} and password=#{password} and actual=true")
  AuthDetail getClientByPhoneAndPassword(@Param("phone") String phone, @Param("password") String password);

  @Select("select id, name, surname from client where id=#{id} and actual=true")
  SessionInfo getClientById(@Param("id") Long id);

  @Insert("insert into client_token_storage (token_id, client_id, created_at) "
      + "  values(#{tokenId}, #{clientId}, #{createdAt})")
  void setTokenId(@Param("tokenId") String tokenId, @Param("clientId") Long clientId, @Param("createdAt") Date createdAt);

  @Select("select case when count(1) from client where phone=#{phone} and actual=true")
  Long checkIsClientExist(@Param("phone") String phone);

  @Insert("insert into client (id, phone, password) " +
      "values(nextval('client_id_seq'), #{phoneNumber}, #{password})")
  void createClient(ClientRegisterRecord registerRecord);

  @Select("select token_id as id from client_token_storage where token_id = #{token_id} and actual = true")
  AuthDetail getAuthDetailsByToken(@Param("ggToken") String ggToken);
}
