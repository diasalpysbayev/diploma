package kz.iitu.diploma.controller;

import kz.iitu.diploma.model.server_send.ServerSendEmitter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/server-send")
public class ServerSendController {

  private static final Logger log = LogManager.getLogger(ServerSendController.class);

  @RequestMapping(value = "subscribe", consumes = MediaType.ALL_VALUE)
  public SseEmitter subscribe() {
    SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

    try {
      sseEmitter.send(SseEmitter.event().name("SESSION"));
    } catch (Exception e) {
      throw new RuntimeException();
    }

    sseEmitter.onCompletion(() -> ServerSendEmitter.getEmitters().remove(sseEmitter));
    ServerSendEmitter.addEmitters(sseEmitter);
    log.info("5lI7wZMm7R :: Event Emitters = " + ServerSendEmitter.getEmitters().toString());
    return sseEmitter;
  }

}
