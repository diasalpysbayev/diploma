package kz.iitu.diploma.dao;

import kz.iitu.diploma.model.auth.ClientRegisterRecord;
import kz.iitu.diploma.model.auth.SessionInfo;
import kz.iitu.diploma.model.auth.SmsRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthDao {

  @Insert("insert into sms_verification(id, phone_number, code) " +
      "values(nextval('sms_verification_id_seq'), #{phoneNumber}, #{code})")
  void insertSmsCode(SmsRecord smsRecord);

  @Select("with x as (select code\n" +
      "           from sms_verification\n" +
      "           where phone_number = #{phoneNumber}\n" +
      "             and actual = true\n" +
      "           order by created_at desc\n" +
      "           limit 1)\n" +
      "select case when x.code = #{code} then true else false end\n" +
      "from x;")
  boolean checkCode(SmsRecord smsRecord);

  @Select("select case when count(*) > 3 then false else true end\n" +
      "from sms_verification\n" +
      "where created_at >= (now() at time zone 'Asia/Almaty') - interval '1 hour'\n" +
      "and phone_number=#{phoneNumber}")
  Boolean checkNumberOfAttempts(@Param(value = "phoneNumber") String phoneNumber);

  @Select("select id, phone_number as phoneNumber, name, surname, email " +
      "from client " +
      "where phone_number=#{phoneNumber} and actual=true")
  SessionInfo getClientByPhoneAndPassword(@Param("phoneNumber") String phoneNumber);

  @Select("select id, name, surname, email from client where id=#{id} and actual=true")
  SessionInfo getClientById(@Param("id") Long id);

  @Insert("insert into client_token_storage (id, client_id) "
      + "  values(#{tokenId}, #{clientId})")
  void setTokenId(@Param("tokenId") String tokenId, @Param("clientId") Long clientId);

  @Select("select count(1) from client where phone_number=#{phoneNumber} and actual=true")
  Long checkIsClientExist(@Param("phoneNumber") String phoneNumber);

  @Insert("insert into client (id, phone_number, password, name, surname, patronymic, email) " +
      "values(#{id}, #{phoneNumber}, #{password}, #{firstName}, #{lastName}, #{patronymic}, #{email})")
  void createClient(ClientRegisterRecord registerRecord);

  @Select("select c.id as id, name, surname, email, phone_number as phone from client_token_storage ct  \n" +
      "    left join client c on ct.client_id = c.id and c.actual = true \n" +
      "    where ct.id = #{tokenId} and ct.actual = true ")
  SessionInfo getAuthDetailsByToken(@Param("tokenId") String tokenId);

  @Select("select exists(select 1 from client where phone_number = #{phoneNumber});")
  boolean checkPhone(String phoneNumber);

  @Insert("insert into client_key_storage (id, client_id) "
      + "  values(#{id}, #{clientId})")
  void setSecretKeyId(@Param("id") String id, @Param("clientId") Long clientId);

  @Update("update client\n" +
      "set password     = #{registerRecord.password},\n" +
      "    surname      = #{registerRecord.surname},\n" +
      "    name         = #{registerRecord.name},\n" +
      "    patronymic   = #{registerRecord.patronymic},\n" +
      "    email        = #{registerRecord.email},\n" +
      "    phone_number = #{registerRecord.phoneNumber}\n" +
      "where id = #{registerRecord.id}")
  void updateDate(ClientRegisterRecord registerRecord);
}
