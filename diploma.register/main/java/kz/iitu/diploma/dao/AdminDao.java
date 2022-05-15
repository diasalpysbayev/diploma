package kz.iitu.diploma.dao;

import kz.iitu.diploma.model.admin.ClientRecord;
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

}
