package kz.iitu.diploma.impl;

import kz.iitu.diploma.model.server_send.ServerSendEmitter;
import kz.iitu.diploma.register.ServerSendRegister;
import kz.iitu.diploma.register.SessionRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class ServerSendRegisterImpl implements ServerSendRegister {

  @Autowired
  private SessionRegister sessionRegister;

  @Override
  public void emitEvent() {
    SseEmitter sseEmitter = ServerSendEmitter.getEmitters().get(sessionRegister.getPrincipal());

    try {
      sseEmitter.send(SseEmitter.event().name("SESSION_OUT").data(true));
    } catch (Exception e) {
      ServerSendEmitter.getEmitters().remove(sseEmitter);
    }

  }
}
