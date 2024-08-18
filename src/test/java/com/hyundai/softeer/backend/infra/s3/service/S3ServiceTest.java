package com.hyundai.softeer.backend.infra.s3.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
class S3ServiceTest {

    @Autowired
    private S3Service s3Service;

    @Test
    void listFileNamesInBucket() {
        List<String> strings = s3Service.listFileNamesInBucket();
        log.info("strings = {}", strings);
    }
}