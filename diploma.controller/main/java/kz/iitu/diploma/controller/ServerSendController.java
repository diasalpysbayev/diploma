package kz.iitu.diploma.controller;

import kz.iitu.diploma.model.server_send.ServerSendEmitter;
import kz.iitu.diploma.register.AuthRegister;
import kz.iitu.diploma.register.SessionRegister;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/server-send")
public class ServerSendController {

  @Autowired
  private SessionRegister sessionRegister;

  private static final Logger log = LogManager.getLogger(ServerSendController.class);

  @RequestMapping(value = "subscribe", consumes = MediaType.ALL_VALUE)
  public SseEmitter subscribe() {
    SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

    try {
      sseEmitter.send(SseEmitter.event().name("SESSION"));
    } catch (Exception e) {
      throw new RuntimeException();
    }

    ServerSendEmitter.addEmitters(sessionRegister.getPrincipal(), sseEmitter);

    sseEmitter.onCompletion(() -> ServerSendEmitter.getEmitters().remove(sseEmitter));
    sseEmitter.onTimeout(() -> ServerSendEmitter.getEmitters().remove(sseEmitter));
    sseEmitter.onError((e) -> ServerSendEmitter.getEmitters().remove(sseEmitter));

    log.info("5lI7wZMm7R :: Event Emitters = " + ServerSendEmitter.getEmitters());

    return sseEmitter;
  }

}
