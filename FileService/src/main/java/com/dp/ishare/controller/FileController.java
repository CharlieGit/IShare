package com.dp.ishare.controller;

import com.dp.ishare.constants.CommonConstants;
import com.dp.ishare.constants.ResponseMsg;
import com.dp.ishare.entry.ApiResult;
import com.dp.ishare.entry.ResponseBuilder;
import com.dp.ishare.entry.FileInfo;
import com.dp.ishare.entry.UploadResponse;
import com.dp.ishare.exception.FileException;
import com.dp.ishare.service.FileService;
import com.dp.ishare.util.FileUtil;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

@Api(value = "file upload and download")
@Controller
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    /**
     * upload file API
     */
    @ApiOperation(value = "uploadFile",notes = "upload file to server")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "query",name = "userId",value = "mayun666",required = true),
        @ApiImplicitParam(paramType = "query",name = "effectiveDays",value = "7",defaultValue = "9999"),
        @ApiImplicitParam(paramType = "query",name = "needEncrypt",value = "true|false",defaultValue = "false")
    })
    @ApiResponses({
        @ApiResponse(code = 500,message = "unknown error",response = UploadResponse.class)
    })
    @PostMapping("/uploadFile")
    @ResponseBody
    public ApiResult<UploadResponse> uploadFile(@ApiParam(value="choose file", required = true) MultipartFile file, String userId,
                                     Integer effectiveDays, Boolean needEncrypt){
        if (file == null || StringUtils.isEmpty(userId)) {
            return ResponseBuilder.fail(ResponseMsg.MISSING_PARAMETER);
        }

        logger.info("file uploading, userId={}, fileName={}, fileSize={}", userId, file.getOriginalFilename(), file.getSize());

        ApiResult<UploadResponse> response = fileService.storeFile(file, userId, effectiveDays, needEncrypt);

        logger.info("file upload done, {}", response.toString());
        return response;
    }



    /**
     *
     * @param fileName
     * @param userId
     * @param request
     * @return
     * case
     * error: "file not found or expired"
     */
    @RequestMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId, String code, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException {
        response.addHeader("Cache-control", "no-store");
        session.removeAttribute("fileId");
        FileInfo fileInfo = fileService.getFileInfoById(fileId);
        if (fileInfo == null || fileInfo.getExpireDate().before(new Date())) {
            logger.info("the file not found or expired");
            throw new FileException("", "error.html");
        }

        if (!StringUtils.isEmpty(fileInfo.getEncryptCode()) && StringUtils.isEmpty(code)) {
            session.setAttribute("fileId",fileInfo.getFileId());
            throw new FileException("", "index.html");
        }

        if (!StringUtils.isEmpty(fileInfo.getEncryptCode()) && !fileInfo.getEncryptCode().equalsIgnoreCase(code)) {
            throw new FileException("", "error.html");
        }

        // Load file as Resource
        String serverFileName = FileUtil.getFileName(fileId, fileInfo.getFileType());
        Resource resource = fileService.loadFileAsResource(serverFileName, fileInfo.getUserId());
        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        String downloadName = FileUtil.getFileName(fileInfo.getFileName(), fileInfo.getFileType());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + URLEncoder.encode(downloadName, "UTF-8") + "\"")
                .body(resource);
    }

    @PostMapping("/downloadFile")
    public ResponseEntity<Resource> downloadFileWithCode(
            @RequestParam("file") String fileId,
            @RequestParam("code") String code,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        FileInfo fileInfo = fileService.getFileInfoById(fileId);
        if (fileInfo == null || !fileInfo.getEncryptCode().equals(code)) {
            request.setAttribute("fileId", fileInfo.getFileId());
//            response.sendRedirect("index.html");
            request.getRequestDispatcher("error.html").forward(request, response);
        }

        // Load file as Resource
        String serverFileName = FileUtil.getFileName(fileId, fileInfo.getFileType());
        Resource resource = fileService.loadFileAsResource(serverFileName, fileInfo.getUserId());
        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        String downloadName = FileUtil.getFileName(fileInfo.getFileName(), fileInfo.getFileType());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + downloadName + "\"")
                .body(resource);
    }

    @GetMapping("/downloadFile/{userId}/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, @PathVariable String userId, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileService.loadFileAsResource(fileName, userId);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
