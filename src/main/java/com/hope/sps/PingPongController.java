package com.hope.sps;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class PingPongController {

    private static int COUNTER = 0;

    @Value("${aws.last_update}")
    private static String last_update;
    
     @Value("${aws.secret}")
    private static String key;

    record PingPong(String result) {
    }
    record LastUpdated(String updateString) {
    }
    @GetMapping("/ping")
    public PingPong getPingPong() {
        return new PingPong("PongPongi: %s".formatted(++COUNTER));
    }

    @GetMapping("/servertime")
    public ResponseEntity<String> getCurrentTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = currentTime.format(formatter);
        return ResponseEntity.ok(formattedTime);
    }

    @GetMapping("/last_update")
    public LastUpdated lastUpdated() {
        return new LastUpdated("26/6/2023");
    }
     @GetMapping("/key")
    public String getKey() {
       return key;
    }
}
