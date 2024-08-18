package com.hyundai.softeer.backend.infra.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 amazonS3;

    public List<String> listFileNamesInBucket(String bucketName) {
        List<String> fileNames = new ArrayList<>();
        ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName);
        ListObjectsV2Result result;

        do {
            result = amazonS3.listObjectsV2(req);

            for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                fileNames.add(objectSummary.getKey());
            }
            // 다음 페이지를 요청하기 위해 continuationToken을 설정합니다.
            req.setContinuationToken(result.getNextContinuationToken());
        } while (result.isTruncated()); // 트렁케이션된 경우 반복

        return fileNames;
    }
}

