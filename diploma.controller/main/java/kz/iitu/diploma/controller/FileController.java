package kz.iitu.diploma.controller;

import kz.iitu.diploma.model.file.FileInfo;
import kz.iitu.diploma.register.FileRegister;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

  private final FileRegister fileRegister;

  @GetMapping("/download-photo")
  public void downLoadStoryMedia(@RequestParam("fileId") String fileId, HttpServletResponse response)
      throws IOException {

    FileInfo file = fileRegister.getFileInfoWithByteArray(fileId);

    if (file == null) {
      response.setStatus(404);
      return;
    }

    response.setHeader("Content-Disposition", "attachment; filename=" + file.name);
    response.setHeader("Content-type", file.mimeType);
    response.setHeader("cache-control", "public");
    response.setHeader("accept-ranges", "bytes");
    response.setContentLength(file.data.length);
    ServletOutputStream outputStream = response.getOutputStream();

    outputStream.write(file.data);

    outputStream.flush();
    outputStream.close();
  }

}
