package kz.iitu.diploma.controller;

import kz.iitu.diploma.model.analytics.AnalyticsRecord;
import kz.iitu.diploma.model.query.QueryRecord;
import kz.iitu.diploma.model.search_engine.SearchInformation;
import kz.iitu.diploma.register.QueryRegister;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/server-send")
public class ServerSendController {

  public List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

  @RequestMapping(value = "subscribe", consumes = MediaType.ALL_VALUE)
  public SseEmitter subscribe() {
    SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

    try {
      sseEmitter.send(SseEmitter.event().name("INIT"));
    } catch (Exception e) {
      throw new RuntimeException();
    }

    sseEmitter.onCompletion(() -> emitters.remove(sseEmitter));
    emitters.add(sseEmitter);
    return sseEmitter;
  }

  @PostMapping(value = "event")
  public void event(@RequestParam("value") String value) {
    for (SseEmitter sseEmitter : emitters) {
      try {
        sseEmitter.send(SseEmitter.event().name("hello").data(value));
      } catch (Exception e) {
        emitters.remove(sseEmitter);
      }
    }
  }

}
