package kz.iitu.diploma.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  private final List<WebSocketSession> webSocketSessions = new ArrayList<>();

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(getWebSocketHandler(), "/close-session").setAllowedOrigins("*");
  }

  @Bean
  public WebSocketHandler getWebSocketHandler() {
    return new WebSocketHandler() {
      @Override
      public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        webSocketSessions.add(session);
      }

      @Override
      public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

      }

      @Override
      public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("WEB SOCKET");
      }

      @Override
      public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        webSocketSessions.remove(session);
      }

      @Override
      public boolean supportsPartialMessages() {
        return false;
      }
    };
  }

}
