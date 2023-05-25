package com.hope.sps;

import com.hope.sps.s3.S3Buckets;
import com.hope.sps.s3.S3Service;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpsApplication {

    public static void main(String[] args) throws InterruptedException {

        SpringApplication.run(SpsApplication.class, args);
    }

    CommandLineRunner runner(
            S3Buckets s3Buckets,
            S3Service s3Service
    ){
        return args -> {

            textBucketUploadAndDownload(s3Buckets, s3Service);
        };
    }

    private static void textBucketUploadAndDownload(S3Buckets s3Buckets, S3Service s3Service) {
        s3Service.putObject(
                s3Buckets.getViolations(),
                "XDD",
                "GIGA CHAD".getBytes());

        byte[] object = s3Service.getObject(
                s3Buckets.getViolations(),
                "XDD"
        );

        System.out.println("HAAAHHAHHAHA" + new String(object));
    }
}
