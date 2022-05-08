package kz.iitu.diploma.model.server_send;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerSendEmitter {

  private static final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

  public static List<SseEmitter> getEmitters() {
    return emitters;
  }

  public static void addEmitters(SseEmitter emitter) {
    ServerSendEmitter.emitters.add(emitter);
  }
}
