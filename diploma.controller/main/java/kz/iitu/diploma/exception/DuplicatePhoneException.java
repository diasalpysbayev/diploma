package kz.iitu.diploma.exception;

public class DuplicatePhoneException extends CustomRestException {
  public DuplicatePhoneException() {
    this("Этот номер телефона уже существует попробуйте другую");
  }

  public DuplicatePhoneException(String message) {
    super(message);
  }

  public DuplicatePhoneException(String message, Throwable cause) {
    super(message, cause);
  }
}
