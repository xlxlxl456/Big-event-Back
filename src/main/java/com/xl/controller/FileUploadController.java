package com.xl.controller;

import com.xl.pojo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class FileUploadController {

    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String filename = UUID.randomUUID().toString()+"."+originalFileName.substring(originalFileName.lastIndexOf("."));

        file.transferTo(new File("/Users/xinglang/Downloads/SpringbootTestFile/"+filename));
        return Result.success("file url...");
    }
}
