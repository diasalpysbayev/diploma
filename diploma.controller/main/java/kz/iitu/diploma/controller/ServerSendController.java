package kz.iitu.diploma.controller;

import kz.iitu.diploma.model.server_send.ServerSendEmitter;
import kz.iitu.diploma.register.QueryRegister;
import kz.iitu.diploma.register.SessionRegister;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/server-send")
public class ServerSendController {

  private static final Logger log = LogManager.getLogger(ServerSendController.class);
  @Autowired
  private SessionRegister sessionRegister;
  @Autowired
  private QueryRegister   queryRegister;

  @RequestMapping(value = "subscribe", consumes = MediaType.ALL_VALUE)
  public SseEmitter subscribe(@RequestParam(value = "clientId") Long clientId) {
    SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

    log.info("PCQxZMeQOd :: Session Id = " + clientId);

    if (clientId == null) {
      return null;
    }

    try {
      sseEmitter.send(SseEmitter.event().name("SESSION"));
    } catch (Exception e) {
      throw new RuntimeException();
    }

    ServerSendEmitter.addEmitters(clientId, sseEmitter);

    sseEmitter.onCompletion(() -> ServerSendEmitter.getEmitters().remove(sseEmitter));
    sseEmitter.onTimeout(() -> ServerSendEmitter.getEmitters().remove(sseEmitter));
    sseEmitter.onError((e) -> ServerSendEmitter.getEmitters().remove(sseEmitter));

    log.info("5lI7wZMm7R :: Event Emitters = " + ServerSendEmitter.getEmitters());

    return sseEmitter;
  }

}
