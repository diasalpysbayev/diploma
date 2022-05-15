package kz.iitu.diploma.controller;

import kz.iitu.diploma.model.query.QuerySoap;
import kz.iitu.diploma.register.QueryRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class SoapController {

  private static final String NAMESPACE = "query";

  @Autowired
  private QueryRegister queryRegister;

  @PayloadRoot(namespace = NAMESPACE, localPart = "query")
  @ResponsePayload
  public QuerySoap executeQuery() {
    return new QuerySoap();
  }

}
