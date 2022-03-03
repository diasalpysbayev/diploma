package kz.iitu.diploma.util;

import kz.greetgo.file_storage.errors.UnknownMimeType;
import kz.greetgo.file_storage.impl.LocalUtil;
import kz.iitu.diploma.exception.CustomRestException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MimeTypeUtil {

  private static final Set<String> MIME_TYPE_OK = new HashSet<>();
  private static final Map<String, String> EXTENSION_MIME_TYPE = new HashMap<>();

  static {
    addMimeType(".txt   ~ Text file                       ~ text/plain                                   ");

    addMimeType(".doc   ~ Microsoft Word                  ~ application/msword                           ");
    addMimeType(".docx  ~ Microsoft Word                  ~ application/vnd.openxmlformats-officedocument" +
      ".wordprocessingml.document");
    addMimeType(".ppt   ~ Microsoft PowerPoint            ~ application/vnd.ms-powerpoint                ");
    addMimeType(".pptx  ~ Microsoft PowerPoint            ~ application/vnd.openxmlformats-officedocument" +
      ".presentationml.presentation");
    addMimeType(".rar   ~ RAR archive                     ~ application/x-rar-compressed                 ");
    addMimeType(".rtf   ~ Rich Text Format (RTF)          ~ application/rtf                              ");
    addMimeType(".tar   ~ Tape Archive (TAR)              ~ application/x-tar                           ");
    addMimeType(".xls   ~ Microsoft Excel                 ~ application/vnd.ms-excel                     ");
    addMimeType(".xlsx  ~ Microsoft Excel                 ~ application/vnd.openxmlformats-officedocument" +
      ".spreadsheetml.sheet");
    addMimeType(".zip   ~ ZIP archive                     ~ application/zip                              ");
    addMimeType(".zip   ~ ZIP archive                     ~ application/x-zip-compressed                 ");
    addMimeType(".7z    ~ 7-zip archive                   ~ application/x-7z-compressed                  ");

    addMimeType(".pdf   ~ Adobe Portable Document Format (PDF)  ~ application/pdf                        ");

    addMimeType(".png       ~ Portable Network Graphics             ~ image/png                          ");
    addMimeType(".svg       ~ Scalable Vector Graphics (SVG)        ~ image/svg+xml                      ");
    addMimeType(".gif       ~ Graphics Interchange Format (GIF)     ~ image/gif                          ");
    addMimeType(".tif .tiff ~ Tagged Image File Format (TIFF)       ~ image/tiff                         ");
    addMimeType(".jpeg .jpg ~ JPEG images                           ~ image/jpeg                         ");
    addMimeType(".mp4   ~ Video                           ~ video/mp4                                   ");
    addMimeType(".mov   ~ Video Ios                           ~ video/quicktime                                   ");

    addMimeType(".odp ~ OpenDocument presentation document~application/vnd.oasis.opendocument.presentation  ");
    addMimeType(".ods ~ OpenDocument spreadsheet document~application/vnd.oasis.opendocument.spreadsheet    ");
    addMimeType(".odt ~ OpenDocument text document~application/vnd.oasis.opendocument.text                  ");
    addMimeType(".msg ~ Outlook mail message  ~ application/vnd.ms-outlook                                  ");
  }


  private static void addMimeType(String line) {
    String mimeType = line.split("~")[2].trim();
    String extensions[] = line.split("~")[0].trim().split("\\s+");

    MIME_TYPE_OK.add(mimeType);

    for (String extension : extensions) {
      if (extension.startsWith(".")) extension = extension.substring(1);
      EXTENSION_MIME_TYPE.put(extension, mimeType);
    }
  }

  public static Boolean validator(String mimeType) {
    if (mimeType == null) return true;
    mimeType = mimeType.toLowerCase();
    if (!MIME_TYPE_OK.contains(mimeType)) {
      throw new UnknownMimeType(
        mimeType,
        "No MIME type '" + mimeType + "' in " + MIME_TYPE_OK.stream().sorted().collect(Collectors.toList())
      );
    }
    return true;
  }

  public static String extractMimeType(String fileName) {
    String extension = LocalUtil.extractExtension(fileName);
    String mimeType = EXTENSION_MIME_TYPE.get(extension);
    if (mimeType == null) throw new CustomRestException("Unknown mime type for extension of file name " + fileName);
    return mimeType;
  }
}
