package kz.iitu.diploma.model.file;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class FileInfo {

  public String fileId;
  public String name;
  public String base64data;
  public String mimeType;
  public Long   size;
  public byte[] data;

}
