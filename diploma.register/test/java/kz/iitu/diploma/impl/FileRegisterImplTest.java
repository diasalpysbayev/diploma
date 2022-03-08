package kz.iitu.diploma.impl;

import kz.greetgo.file_storage.FileDataReader;
import kz.greetgo.file_storage.FileStorage;
import kz.iitu.diploma.model.file.FileInfo;
import kz.iitu.diploma.register.FileRegister;
import kz.iitu.diploma.util.MimeTypeUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

public class FileRegisterImplTest extends AbstractTestParent {

  @Autowired
  FileRegister fileRegister;

  @Autowired
  private FileStorage fs;


  @Test
  public void save() throws IOException {

    FileInfo fileInfo = prepareFileInfo("files/image.jpeg");

    //
    //
    String id = fileRegister.save(fileInfo);
    //
    //

    FileDataReader fileDataReader = fs.readOrNull(id);

    assertThat(fileDataReader.name()).isEqualTo(fileInfo.name);
    assertThat(fileDataReader.mimeType()).isEqualTo(MimeTypeUtil.extractMimeType(fileInfo.name));

  }

  public FileInfo prepareFileInfo(String fileName) throws IOException {
    FileInfo fileInfo = new FileInfo();
    fileInfo.name = fileName;
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
      assert inputStream != null;
      byte[] imageBytes = new byte[inputStream.available()];
      //noinspection ResultOfMethodCallIgnored
      inputStream.read(imageBytes, 0, imageBytes.length);
      fileInfo.base64data = Base64.getEncoder().encodeToString(imageBytes);
    }
    return fileInfo;
  }

}
