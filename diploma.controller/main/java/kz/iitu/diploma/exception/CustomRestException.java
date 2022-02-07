package kz.iitu.diploma.exception;

public class CustomRestException extends RuntimeException {
  public CustomRestException(String message) {
    super(message);
  }

  public CustomRestException(String message, Throwable cause) {
    super(message, cause);
  }

  public String toString() {
    return "CustomRestException()";
  }
}
