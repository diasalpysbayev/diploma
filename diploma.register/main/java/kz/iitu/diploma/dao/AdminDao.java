package kz.iitu.diploma.dao;

import kz.iitu.diploma.model.admin.ClientRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminDao {

  @Select("select id,\n" +
      "       surname,\n" +
      "       name,\n" +
      "       patronymic,\n" +
      "       email,\n" +
      "       phone_number as phoneNumber\n" +
      "from client\n" +
      "where actual = 1;")
  List<ClientRecord> getClientList();

  @Select("select id,\n" +
      "       surname,\n" +
      "       name,\n" +
      "       patronymic,\n" +
      "       email,\n" +
      "       phone_number as phoneNumber\n" +
      "from client\n" +
      "where actual = 1 and id = #{id};")
  ClientRecord getClientDetail(Long id);

  @Select("update client\n" +
      "set surname      = #{record.surname},\n" +
      "    patronymic   = #{record.name},\n" +
      "    email        = #{record.email},\n" +
      "    phone_number = #{record.phoneNumber}\n" +
      "where actual = 1 and id = #{id};")
  void updateClientDetail(@Param(value = "record") ClientRecord record);

}
