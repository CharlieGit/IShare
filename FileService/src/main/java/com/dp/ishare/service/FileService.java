package com.dp.ishare.service;

import com.dp.ishare.constants.CommonConstants;
import com.dp.ishare.constants.ResponseMsg;
import com.dp.ishare.dao.FileInfoDao;
import com.dp.ishare.entry.ApiResult;
import com.dp.ishare.entry.FileInfo;
import com.dp.ishare.entry.ResponseBuilder;
import com.dp.ishare.entry.UploadResponse;
import com.dp.ishare.exception.FileException;
import com.dp.ishare.properties.FileProperties;
import com.dp.ishare.util.FileUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

@Service
public class FileService {

    @Autowired
    private FileProperties fileProperties;

    @Autowired
    private FileInfoDao fileInfoDao;

    public ApiResult<UploadResponse> storeFile(MultipartFile file, String userId, Integer effectiveDays, Boolean needEncrypt) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // validate file size
            if (file.getSize() > fileProperties.getMaxSize() * 1024 * 1024) {
                return ResponseBuilder.fail(ResponseMsg.FILE_SIZE_LIMIT);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            String fileId = FileUtil.getFileId(file, userId);
            if (StringUtils.isEmpty(fileId)) {
                return ResponseBuilder.fail(ResponseMsg.COMMON_ERROR);
            }
            int dotIndex = fileName.lastIndexOf(CommonConstants.DOT);
            String name = dotIndex == -1 ? fileName : fileName.substring(0, dotIndex);
            String type = dotIndex == -1 ? "" : fileName.substring(dotIndex + 1);
            String directory = fileProperties.getUploadDir() + userId;
            Path targetLocation = Paths.get(directory).toAbsolutePath().normalize().resolve(fileId + CommonConstants.DOT + type);
            if (!Files.exists(targetLocation)) {
                Files.createDirectories(targetLocation);
            }
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // insert DB
            FileInfo info = new FileInfo();
            info.setFileId(fileId);
            info.setFileName(name);
            info.setFileType(type);
            info.setFileSize(file.getSize());
            info.setUserId(userId);
            info.setEncryptCode(BooleanUtils.isTrue(needEncrypt) ? FileUtil.generateExtractCode() : null);
            info.setUploadDate(new Date());
            info.setExpireDate(FileUtil.getExpireDate(effectiveDays));
            if (fileInfoDao.existsById(fileId)) {
                fileInfoDao.save(info);
            } else {
                fileInfoDao.insert(fileId, name, type, file.getSize(), userId, new Date(), info.getExpireDate(), info.getEncryptCode());
            }

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/ishare/down/")
                    .path(fileId)
                    .toUriString();
            UploadResponse response = new UploadResponse(fileDownloadUri, info.getEncryptCode(), file.getSize());
            return ResponseBuilder.success(response, ResponseMsg.SUCCESS);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseBuilder.fail(ResponseMsg.COMMON_ERROR);
        }
    }

    /**
     * download file
     * @param fileName
     * @param userId
     * @return
     */
    public Resource loadFileAsResource(String fileName, String userId) {
        try {
            String directory = fileProperties.getUploadDir() + userId;
            Path targetLocation = Paths.get(directory).toAbsolutePath().normalize();;
            Path filePath = targetLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new FileException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileException("File not found " + fileName, ex);
        }
    }

    public FileInfo getFileInfoById(String fileId){
        return fileInfoDao.getById(fileId).orElse(null);
    }

}
