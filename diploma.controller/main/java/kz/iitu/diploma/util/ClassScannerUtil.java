package kz.iitu.diploma.util;

import kz.greetgo.class_scanner.ClassScanner;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;

import java.util.HashSet;
import java.util.Set;

public class ClassScannerUtil {

  public static ClassScanner createClassScanner() {

    var metadataReaderFactory = new CachingMetadataReaderFactory();
    var resolver = new PathMatchingResourcePatternResolver();

    var scanner = new ClassPathScanningCandidateComponentProvider(false) {

      Set<Class<?>> find(String packageName) throws Exception {

        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
            + resolveBasePackage(packageName) + '/' + "**/*.class";

        Resource[] resources = resolver.getResources(packageSearchPath);

        Set<Class<?>> ret = new HashSet<>();

        for (Resource resource : resources) {
          var metadataReader = metadataReaderFactory.getMetadataReader(resource);

          var className = metadataReader.getClassMetadata().getClassName();

          try {
            ret.add(Class.forName(className));
          } catch (ClassNotFoundException | NoClassDefFoundError e) {
            continue;
          }
        }

        return ret;

      }

    };


    return new ClassScanner() {
      @Override
      public void addClassLoader(ClassLoader classLoader) {
        //ignore this method
      }

      @Override
      public Set<Class<?>> scanPackage(String packageName) {
        try {
          return scanner.find(packageName);
        } catch (RuntimeException e) {
          throw e;
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    };
  }
}
