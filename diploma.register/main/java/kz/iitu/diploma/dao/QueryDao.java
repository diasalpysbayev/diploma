package kz.iitu.diploma.dao;

import kz.iitu.diploma.model.analytics.AnalyticsToSave;
import kz.iitu.diploma.model.query.QueryHistory;
import kz.iitu.diploma.model.search_engine.PlaceInfo;
import kz.iitu.diploma.model.search_engine.SearchInformation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QueryDao {

  @Select("select nextval('query_id_seq') as id")
  Long nextQueryId();

  @Select("select nextval('query_info_id_seq') as id")
  Long nextQueryInfoId();

  @Select("select nextval('analytics_id_seq') as id")
  Long nextAnalyticsId();

  @Insert("insert into query(id, client_id, valuestr) values (#{id}, #{clientId}, #{valuestr})")
  void saveQuery(@Param(value = "id") Long id,
                 @Param(value = "clientId") Long clientId,
                 @Param(value = "valuestr") String valuestr);

  @Insert("insert into query_info(id, query_id, title, url, latitude, longitude, rating, description, address, phone) " +
      "values (#{id}, #{queryId}, #{title}, #{url}, #{placeInfo.latitude}, #{placeInfo.longitude}, #{placeInfo.rating}, " +
      "#{placeInfo.description}, #{placeInfo.address}, #{placeInfo.phone})")
  void saveQueryInfo(@Param(value = "id") Long id,
                     @Param(value = "queryId") Long queryId,
                     @Param(value = "title") String title,
                     @Param(value = "url") String url,
                     @Param(value = "placeInfo")PlaceInfo placeInfo);

  @Select("select url " +
      "from query_info " +
      "where query_id = #{id};")
  List<String> getQueryDetails(Long id);

  @Select("select id, query_id, valuestr, city, top " +
      "from analytics " +
      "where query_id = #{id} " +
      "  and actual = true;")
  AnalyticsToSave getAnalytics(Long id);

  @Insert("insert into analytics(id, query_id, valuestr, top, city) " +
      "values (#{record.id}, #{record.queryId}, #{record.valuestr}, #{record.top}, #{record.city}) " +
      "on conflict (id, query_id) do nothing")
  void insertAnalytics(@Param(value = "record") AnalyticsToSave record);

  @Select("select qi.id as id, query_id as queryId, url, title, latitude, longitude, rating, description, address, phone " +
      "from query_info qi " +
      "left join query q on q.id = qi.query_id and q.actual = true " +
      "where client_id = #{id} " +
      "  and qi.actual = true;")
  List<SearchInformation> getHistory(@Param(value = "id") Long id);

  @Select("select id " +
      "from query " +
      "where client_id = #{id} " +
      "  and actual = true;")
  List<Long> getClientQueryIds(Long id);
}
