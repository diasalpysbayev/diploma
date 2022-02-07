package kz.iitu.diploma.config;

import kz.greetgo.conf.hot.DefaultIntValue;
import kz.greetgo.conf.hot.Description;
import kz.greetgo.conf.hot.FirstReadEnv;

@Description("Scheduler Configuration")
public interface SchedulerConfig {

  @Description("Thread Pool size")
  @DefaultIntValue(100)
  @FirstReadEnv("THREAD_POOL")
  int threadPool();

}

