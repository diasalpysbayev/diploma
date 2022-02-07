package kz.iitu.diploma.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.function.Supplier;

public class LOG {
  final Logger logger;

  private LOG(String name) {
    logger = LoggerFactory.getLogger(name);
  }

  @SuppressWarnings("unused")
  public static LOG forClass(Class<?> aClass) {
    return new LOG(aClass);
  }

  @SuppressWarnings("unused")
  public static LOG byName(String name) {
    return new LOG(name);
  }

  private LOG(Class<?> aClass) {
    logger = LoggerFactory.getLogger(aClass);
  }

  public void debug(Supplier<String> supplier) {
    if (logger.isDebugEnabled()) logger.debug(supplier.get());
  }

  @SuppressWarnings("unused")
  public void info(Supplier<String> supplier) {
    if (logger.isInfoEnabled()) logger.info(supplier.get());
  }

  public void warn(String message) {
    logger.warn(message);
  }

  private static String nanosToSec(long delayNanos) {
    DecimalFormat df = new DecimalFormat("0.00000000");
    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
    dfs.setDecimalSeparator('.');
    dfs.setGroupingSeparator('_');
    df.setDecimalFormatSymbols(dfs);
    return df.format(delayNanos / 1_000_000_000.0) + "sec";
  }

  public void infoDelay(long delayNanos, String message) {
    if (!logger.isInfoEnabled()) return;

    logger.info("Delay" + nanosToSec(delayNanos) + " " + message);
  }

  @SuppressWarnings("unused")
  public void trace(Supplier<String> supplier) {
    if (logger.isTraceEnabled()) logger.trace(supplier.get());
  }

  @SuppressWarnings("unused")
  public void error(String errorMessage) {
    logger.error(errorMessage);
  }

  @SuppressWarnings("unused")
  public void error(String errorMessage, Throwable t) {
    logger.error(errorMessage, t);
  }

  public void errorThrowable(Throwable t) {
    if (t == null) {
      try {
        throw new Exception("<NULL MESSAGE>");
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
      }
    } else {
      logger.error(t.getMessage(), t);
    }
  }

  @SuppressWarnings("unused")
  public void errorDelay(long delayNanos, String message, Throwable error) {
    logger.error("Delay" + nanosToSec(delayNanos) + " " + message, error);
  }

  @SuppressWarnings("unused")
  public void errorDelayThrowable(long delayNanos, Throwable error) {
    if (error == null) {
      try {
        throw new Exception("Delay" + nanosToSec(delayNanos) + " <NULL ERROR>");
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
      }
    } else {
      logger.error("Delay" + nanosToSec(delayNanos) + " " + error.getMessage(), error);
    }
  }

  @SuppressWarnings("unused")
  public String view(String str) {
    debug(() -> str);
    return str;
  }

  public boolean isTraceEnabled() {
    return logger.isTraceEnabled();
  }
}
