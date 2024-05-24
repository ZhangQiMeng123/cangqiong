package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * 通用接口
 */
@RestController
@RequestMapping("/admin/common")
@Slf4j
@Api(tags = "通用接口")
public class CommonController {
     @Autowired
     private AliOssUtil aliOssUtil;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation(value = "文件上传")
     public Result<String> upload(MultipartFile file){
         //获取文件的原始名称
         try {
             String originalFilename = file.getOriginalFilename();
             //使用UUID工具为文件设置唯一的名称
             String newName= UUID.randomUUID().toString()+originalFilename.substring(originalFilename.lastIndexOf("."));
             //文件上传的请求路径
             String filePath=aliOssUtil.upload(file.getBytes(),newName);
             return Result.success(filePath);
         } catch (IOException e) {
             log.info("文件上传失败:{}",e);
         }
         return Result.error(MessageConstant.UPLOAD_FAILED);
     }
}
