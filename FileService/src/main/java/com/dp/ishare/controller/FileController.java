package com.dp.ishare.controller;

import com.dp.ishare.constants.ResponseMsg;
import com.dp.ishare.entry.ApiResult;
import com.dp.ishare.entry.ResponseBuilder;
import com.dp.ishare.entry.UploadResponse;
import com.dp.ishare.service.FileService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Api(value = "file upload and download")
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
