package kz.iitu.diploma.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import kz.greetgo.class_scanner.ClassScanner;
import kz.iitu.diploma.logging.LOG;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.format.*;
import org.springframework.format.Formatter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class WebFormatters {

  private final static LOG log = LOG.forClass(WebFormatters.class);

  public static void registerDateFormatter(FormatterRegistry registry) {
    registry.addFormatterForFieldType(Date.class, new Formatter<Date>() {
      @Override
      public Date parse(String text, Locale locale) {

        try {
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
          return sdf.parse(text);
        } catch (ParseException ignore) {
          //ignore
        }
        try {
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
          return sdf.parse(text);
        } catch (ParseException ignore) {
          //ignore
        }
        try {
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          return sdf.parse(text);
        } catch (ParseException ignore) {
          //ignore
        }
        try {
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
          return sdf.parse(text);
        } catch (ParseException ignore) {
          //ignore
        }

        try {
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
          return sdf.parse(text);
        } catch (ParseException ignore) {
          //ignore
        }

        {
          long time = Long.parseLong(text);
          return new Date(time);
        }
      }

      @Override
      public String print(Date object, Locale locale) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.format(object);
      }
    });
  }

  public static void registerJsonFormatter(FormatterRegistry registry) {

    registry.addFormatterForFieldAnnotation(new AnnotationFormatterFactory<Json>() {
      @Override
      public Set<Class<?>> getFieldTypes() {
        Set<Class<?>> ret = new HashSet<>();

        ClassScanner classScanner = ClassScannerUtil.createClassScanner();

        Set<Class<?>> classes = classScanner.scanPackage(CommonConst.APP_PACKAGE_NAME);

        for (Class<?> aClass : classes) {
          if (aClass.getAnnotation(Json.class) != null) {
            ret.add(aClass);
          }
        }

        ret.add(List.class);

        return ret;
      }

      @Override
      public Printer<?> getPrinter(Json annotation, Class<?> fieldType) {
        //noinspection Convert2Lambda
        return new Printer<>() {
          @Override
          public String print(Object object, Locale locale) {
            return object == null ? "NULL" : object.toString();
          }
        };
      }

      @Override
      public Parser<?> getParser(Json annotation, Class<?> fieldType) {
        //noinspection Convert2Lambda
        return new Parser<>() {
          @Override
          public Object parse(String text, Locale locale) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
              return objectMapper.reader().forType(fieldType).readValue(text);
            } catch (IOException e) {
              log.error("GetParserException", e);
              throw new RuntimeException(e);
            }
          }
        };
      }
    });
  }

  public static void registerListObjectConverter(FormatterRegistry registry) {

    registry.addConverter(new GenericConverter() {
      @Override
      public Set<ConvertiblePair> getConvertibleTypes() {
        ConvertiblePair stringToList = new ConvertiblePair(String.class, List.class);
        Set<ConvertiblePair> ret = new HashSet<>();
        ret.add(stringToList);
        return ret;
      }

      @Override
      public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null) {
          return null;
        }
        if (!(source instanceof String)) {
          throw new RuntimeException("Unknown source type " + sourceType + " : " + source);
        }

        ResolvableType[] generics = targetType.getResolvableType().getGenerics();
        if (generics.length == 0) {
          throw new RuntimeException("Unknown destination list component : " + targetType);
        }

        Type listComponentType = generics[0].getType();

        JavaType listComponentJavaType = TypeFactory.defaultInstance().constructType(listComponentType);

        CollectionType targetJacksonType = TypeFactory.defaultInstance().constructCollectionType(List.class,
            listComponentJavaType);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
          return objectMapper.readValue((String) source, targetJacksonType);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    });
  }

}
