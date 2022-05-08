package kz.iitu.diploma.impl;

import kz.iitu.diploma.model.server_send.ServerSendEmitter;
import kz.iitu.diploma.register.ServerSendRegister;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class ServerSendRegisterImpl implements ServerSendRegister {

  @Override
  public void emitEvent() {
    for (SseEmitter sseEmitter : ServerSendEmitter.getEmitters()) {
      try {
        sseEmitter.send(SseEmitter.event().name("SESSION_OUT").data(true));
      } catch (Exception e) {
        ServerSendEmitter.getEmitters().remove(sseEmitter);
      }
    }
  }
}
