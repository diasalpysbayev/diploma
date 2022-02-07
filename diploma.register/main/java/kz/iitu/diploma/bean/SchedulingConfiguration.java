package kz.iitu.diploma.bean;

import kz.iitu.diploma.config.SchedulerConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
@Profile({"dev", "prod"})
@RequiredArgsConstructor
public class SchedulingConfiguration implements SchedulingConfigurer {

  private final SchedulerConfig config;

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    taskRegistrar.setScheduler(taskExecutor());
  }

  @Bean()
  public Executor taskExecutor() {
    return Executors.newScheduledThreadPool(config.threadPool());
  }

}

