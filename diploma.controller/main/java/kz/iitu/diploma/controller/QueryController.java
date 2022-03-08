package kz.iitu.diploma.controller;

import kz.iitu.diploma.model.query.QueryRecord;
import kz.iitu.diploma.register.QueryRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/query")
public class QueryController {

  @Autowired
  private QueryRegister queryRegister;

  @PostMapping("/query")
  public void checkPhoneOnExist(@RequestBody QueryRecord queryRecord) {
    queryRegister.createQuery(queryRecord);
  }

}
