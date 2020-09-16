package com.dp.ishare.dao;

import com.dp.ishare.entry.FileInfo;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FileInfoDao extends CrudRepository<FileInfo, String> {

    @Query("select * from file_info where fileId=:fileId")
    Optional<FileInfo> getById(@Param("fileId") String fileId);

}
