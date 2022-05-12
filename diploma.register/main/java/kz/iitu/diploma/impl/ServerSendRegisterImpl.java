package kz.iitu.diploma.impl;

import kz.iitu.diploma.model.server_send.ServerSendEmitter;
import kz.iitu.diploma.register.ServerSendRegister;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class ServerSendRegisterImpl implements ServerSendRegister {

  private static final Logger log = LogManager.getLogger(ServerSendRegisterImpl.class);

  @Override
  public void emitEvent(Long clientId) {
    SseEmitter sseEmitter = ServerSendEmitter.getEmitters().get(clientId);

    try {
      sseEmitter.send(SseEmitter.event().name("SESSION_OUT").data(true));
      log.info("uME9fd2MA6 :: SESSION_OUT :: CLIENT ID = " + clientId);
    } catch (Exception e) {
      ServerSendEmitter.getEmitters().remove(sseEmitter);
      log.info("g1eBjFX6jq :: REMOVED ::  " + sseEmitter);
    }

  }
}
