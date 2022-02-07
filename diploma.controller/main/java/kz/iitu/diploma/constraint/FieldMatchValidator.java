package kz.iitu.diploma.constraint;

import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
  private String firstFieldName;
  private String secondFieldName;

  public FieldMatchValidator() {
  }

  public void initialize(final FieldMatch constraintAnnotation) {
    this.firstFieldName = constraintAnnotation.first();
    this.secondFieldName = constraintAnnotation.second();
  }

  public boolean isValid(final Object value, final ConstraintValidatorContext context) {
    try {
      Object firstObj = BeanUtils.getProperty(value, this.firstFieldName);
      Object secondObj = BeanUtils.getProperty(value, this.secondFieldName);
      return firstObj == null && secondObj == null || firstObj != null && firstObj.equals(secondObj);
    } catch (Exception var5) {
      return true;
    }
  }
}
