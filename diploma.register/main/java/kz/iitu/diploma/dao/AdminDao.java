package kz.iitu.diploma.dao;

import kz.iitu.diploma.model.admin.ClientRecord;
import kz.iitu.diploma.model.admin.ClientStatus;
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
      "       phone_number as phoneNumber,\n" +
      "       status as status\n" +
      "from client\n" +
      "where actual = true;")
  List<ClientRecord> getClientList();

  @Select("select id,\n" +
      "       surname,\n" +
      "       name,\n" +
      "       patronymic,\n" +
      "       email,\n" +
      "       phone_number as phoneNumber\n" +
      "from client\n" +
      "where actual = true and id = #{id};")
  ClientRecord getClientDetail(Long id);

  @Select("update client\n" +
      "set surname      = #{record.surname},\n" +
      "    name   = #{record.name},\n" +
      "    patronymic   = #{record.patronymic},\n" +
      "    email        = #{record.email},\n" +
      "    phone_number = #{record.phoneNumber}\n" +
      "where actual = true and id = #{record.id};")
  void updateClientDetail(@Param(value = "record") ClientRecord record);

  @Select("insert into query_blocked(valuestr)\n" +
      "values (#{query});")
  void blockWord(String query);

  @Select("delete\n" +
      "from query_blocked\n" +
      "where valuestr = #{query}")
  void unblockWord(String query);

  @Select("update client\n" +
      "set status   = #{status}\n" +
      "where actual = true and id = #{id};")
  void changeStatus(@Param(value = "status") ClientStatus status,
                    @Param(value = "id") Long id);

  @Select("select valuestr from query_blocked;")
  List<String> getBlockedList();
}
