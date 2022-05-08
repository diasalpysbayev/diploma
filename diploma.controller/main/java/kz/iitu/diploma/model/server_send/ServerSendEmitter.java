package kz.iitu.diploma.model.server_send;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;

public class ServerSendEmitter {

  private static final Map<Long, SseEmitter> emitters = new HashMap<>();

  public static Map<Long, SseEmitter> getEmitters() {
    return emitters;
  }

  public static void addEmitters(Long clientId, SseEmitter emitter) {
    ServerSendEmitter.emitters.put(clientId, emitter);
  }
}
