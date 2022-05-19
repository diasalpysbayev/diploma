package kz.iitu.diploma.controller;

import kz.iitu.diploma.model.query.QueryDetail;
import kz.iitu.diploma.model.query.QueryRecord;
import kz.iitu.diploma.model.query.QuerySoap;
import kz.iitu.diploma.model.search_engine.SearchInformation;
import kz.iitu.diploma.register.QueryRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.ArrayList;
import java.util.List;

import static kz.iitu.diploma.util.StaticUtil.initQueryDetail;

@Endpoint
public class SoapController {

  private static final String NAMESPACE = "osint";

  @Autowired
  private QueryRegister queryRegister;

  @PayloadRoot(namespace = NAMESPACE, localPart = "queryRequest")
  @ResponsePayload
  public QuerySoap executeQuery(@RequestPayload QueryDetail queryDetail) {
    QueryRecord queryRecord = new QueryRecord();
    queryRecord.queryList = new ArrayList<>();

    if (queryDetail == null || queryDetail.name == null) {
      queryDetail = initQueryDetail();
    }

    System.out.println(queryDetail);
    queryRecord.queryList.add(queryDetail);

    List<SearchInformation> searchInformationList = queryRegister.executeQuery(queryRecord);

    QuerySoap querySoap = new QuerySoap();
    querySoap.list = searchInformationList;
    System.out.println(searchInformationList);
    return querySoap;
  }




}
