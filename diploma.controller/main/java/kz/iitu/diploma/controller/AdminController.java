package kz.iitu.diploma.controller;

import kz.iitu.diploma.model.admin.ClientRecord;
import kz.iitu.diploma.model.admin.ClientStatus;
import kz.iitu.diploma.register.AdminRegister;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/query")
public class AdminController {

  @Autowired
  private AdminRegister adminRegister;

  @GetMapping("/get-all-clients")
  public List<ClientRecord> getClientList() {
    return adminRegister.getClientList();
  }

  @GetMapping("/get-client-detail")
  public ClientRecord getClientDetail(@Param(value = "id") Long id) {
    return adminRegister.getClientDetail(id);
  }

  @PostMapping("/update-client-info")
  public void updateClientInfo(@RequestBody ClientRecord clientRecord) {
    adminRegister.updateClientInfo(clientRecord);
  }

  @PostMapping("/block-query")
  public void blockQuery(@Param(value = "query") String query) {
    adminRegister.blockQuery(query);
  }

  @PostMapping("/unblock-query")
  public void unblockQuery(@Param(value = "query") String query) {
    adminRegister.unblockQuery(query);
  }

  @PostMapping("/change-status")
  public void changeStatus(@Param(value = "status") ClientStatus status, @Param(value = "id") Long id) {
    adminRegister.changeStatus(status, id);
  }

}
