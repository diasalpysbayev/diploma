package kz.iitu.diploma.util;

import kz.greetgo.file_storage.impl.FileStorageBuilder;
import kz.greetgo.file_storage.impl.FileStorageBuilderConfigurator;

public class MimeTypeConfigurator implements FileStorageBuilderConfigurator {

  private MimeTypeConfigurator() {
  }

  public static MimeTypeConfigurator get() {
    return Ins.VALUE.instance;
  }

  @Override
  public void configure(FileStorageBuilder fileStorageBuilder) {
    fileStorageBuilder.mimeTypeExtractor(MimeTypeUtil::extractMimeType);
    fileStorageBuilder.mimeTypeValidator(MimeTypeUtil::validator);
  }

  enum Ins {
    VALUE;

    final MimeTypeConfigurator instance = new MimeTypeConfigurator();
  }
}
