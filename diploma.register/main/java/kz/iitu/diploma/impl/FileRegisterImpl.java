package kz.iitu.diploma.impl;

import kz.greetgo.file_storage.FileDataReader;
import kz.greetgo.file_storage.FileStorage;
import kz.iitu.diploma.model.file.FileInfo;
import kz.iitu.diploma.register.FileRegister;
import kz.iitu.diploma.util.MimeTypeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class FileRegisterImpl implements FileRegister {

  private final FileStorage fileStorage;

  @Override
  public String save(FileInfo fileInfo) {

    byte[] dataInByte = Base64.getDecoder().decode(fileInfo.base64data);
    String mimeType   = MimeTypeUtil.extractMimeType(fileInfo.name);

    return fileStorage.storing()
        .name(fileInfo.name)
        .mimeType(mimeType)
        .data(dataInByte)
        .store();

  }

  @Override
  public FileInfo getFileInfoWithByteArray(String fileId) {

    FileInfo       ret  = new FileInfo();
    FileDataReader read = fileStorage.read(fileId);

    if (read != null) {
      ret.name     = read.name();
      ret.mimeType = read.mimeType();
      ret.data     = read.dataAsArray();
    }

    return ret;

  }

}
