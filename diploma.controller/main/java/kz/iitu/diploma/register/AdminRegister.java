package kz.iitu.diploma.register;

import kz.iitu.diploma.model.admin.ClientRecord;
import kz.iitu.diploma.model.query.QueryRecord;
import kz.iitu.diploma.model.search_engine.SearchInformation;

import java.util.List;

public interface AdminRegister {

  List<ClientRecord> getClientList();

}