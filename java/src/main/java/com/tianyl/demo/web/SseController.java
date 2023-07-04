package com.tianyl.demo.web;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/sse")
public class SseController {

    @GetMapping(path = "/start", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter createConnect(HttpServletResponse response) {
        response.setHeader("X-Accel-Buffering", "no");
        SseEmitter emitter = new SseEmitter();
        new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    emitter.send("time:" + i, MediaType.TEXT_EVENT_STREAM);
                }
                emitter.complete();
            } catch (IOException e) {
                e.printStackTrace();
                emitter.completeWithError(e);
            }
        }).start();
        return emitter;
    }


}
