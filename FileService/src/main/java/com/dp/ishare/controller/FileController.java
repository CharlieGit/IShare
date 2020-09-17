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
@RequestMapping("/ishare")
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
                                     Integer effectiveDays, Boolean needEncrypt, HttpServletRequest request){
        long start = System.currentTimeMillis();
        if (file == null || StringUtils.isEmpty(userId)) {
            return ResponseBuilder.fail(ResponseMsg.MISSING_PARAMETER);
        }

        logger.info("file uploading, userId={}, fileName={}, fileSize={}", userId, file.getOriginalFilename(), file.getSize());

        ApiResult<UploadResponse> response = fileService.storeFile(file, userId, effectiveDays, needEncrypt);

        long end = System.currentTimeMillis();
        logger.info("file upload done, took {}ms, {}", end - start, response.toString());
        return response;
    }



    /**
     * download file
     */
    @ApiOperation(value = "down",notes = "download file")
    @RequestMapping("/down/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId, String code, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException {
        response.addHeader("Cache-control", "no-store");
        session.removeAttribute("fileId");
        FileInfo fileInfo = fileService.getFileInfoById(fileId);
        if (fileInfo == null || fileInfo.getExpireDate().before(new Date())) {
            logger.info("the file not found or expired");
            throw new FileException("file not found or expired", "error.html");
        }

        if (!StringUtils.isEmpty(fileInfo.getEncryptCode()) && StringUtils.isEmpty(code)) {
            session.setAttribute("fileId",fileInfo.getFileId());
            throw new FileException("code is empty", "index.html");
        }

        if (!StringUtils.isEmpty(fileInfo.getEncryptCode()) && !fileInfo.getEncryptCode().equalsIgnoreCase(code)) {
            throw new FileException("wrong code", "error.html");
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
        String dispositionHeader = "inline; filename=\"" + URLEncoder.encode(downloadName, "UTF-8") + "\"";
        if (fileInfo.getFileType().matches("^(html)")) {
            dispositionHeader = "attachment; filename=\"" + URLEncoder.encode(downloadName, "UTF-8") + "\"";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, dispositionHeader)
                .body(resource);
    }

}
