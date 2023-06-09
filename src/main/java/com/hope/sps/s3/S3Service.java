package com.hope.sps.s3;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

    private final S3Client s3;

    public S3Service(S3Client s3) {
        this.s3 = s3;
    }

    public void putObject(final String bucketName, final String key, final String imageType, final byte[] file) {
        final var objectRequest = PutObjectRequest
                .builder()
                .bucket(bucketName)
                .key(key)
                .contentType("image/" + imageType)
                .build();

        s3.putObject(objectRequest, RequestBody.fromBytes(file));
    }
}

