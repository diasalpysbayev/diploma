package kz.iitu.diploma.impl;

import kz.iitu.diploma.dao.AdminDao;
import kz.iitu.diploma.model.admin.ClientRecord;
import kz.iitu.diploma.register.AdminRegister;
import kz.iitu.diploma.register.SessionRegister;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminRegisterImpl implements AdminRegister {

  private static final Logger log = LogManager.getLogger(AdminRegisterImpl.class);

  @Autowired
  private SessionRegister sessionRegister;

  @Autowired
  private AdminDao adminDao;

  @Override
  public List<ClientRecord> getClientList() {
    return adminDao.getClientList();
  }

  @Override
  public ClientRecord getClientDetail(Long id) {
    return adminDao.getClientDetail(id);
  }

  @Override
  public void updateClientInfo(ClientRecord record) {
    adminDao.updateClientDetail(record);
  }

  @Override
  public void blockQuery(String query) {
    adminDao.blockWord(query);
  }

  @Override
  public void unblockQuery(String query) {
    adminDao.unblockWord(query);
  }
}
