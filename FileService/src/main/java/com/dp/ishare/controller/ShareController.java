package com.dp.ishare.controller;

import com.dp.ishare.constants.ResponseMsg;
import com.dp.ishare.entry.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Date;

@Api(value = "file share")
@RestController
@RequestMapping("/ishare")
public class ShareController {

    @ApiOperation(value = "getOnlineUsers",notes = "get online users")
    @PostMapping("/getOnlineUsers")
    public ApiResult<UserListResponse> getUserList(String userId){

        UserListResponse userListResponse = new UserListResponse();
        User user1 = new User("zhangsan", "1.1.1.1", new Date(), "aaa.txt", "http://baidu.com", "10MB");
        User user2 = new User("lisi", "2.2.2.2", new Date(), null, null, null);
        userListResponse.setUsers(Arrays.asList(user1, user2));
        return ResponseBuilder.success(userListResponse, ResponseMsg.SUCCESS);
    }

    @ApiOperation(value = "sendFile",notes = "send file")
    @PostMapping("/sendFile")
    public ApiResult<String> sendFile(String fromUserId, String toUserId, String fileName, String fileUrl, String fileSize){
        // write DB
        return ResponseBuilder.successNoData(ResponseMsg.SUCCESS);
    }

}
