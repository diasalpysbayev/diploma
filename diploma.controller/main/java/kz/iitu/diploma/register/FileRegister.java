package kz.iitu.diploma.register;

import kz.iitu.diploma.model.file.FileInfo;

public interface FileRegister {

  String save(FileInfo fileInfo);

  FileInfo getFileInfoWithByteArray(String fileId);

}
