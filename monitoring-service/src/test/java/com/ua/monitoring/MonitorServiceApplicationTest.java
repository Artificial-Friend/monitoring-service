package com.ua.monitoring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class MonitorServiceApplicationTest {
    public static final String USER_ACCESS_TOKEN = "1337-riddler-1337";

    @Autowired
    private WebTestClient webClient;

    @Test
    public void getAllByUser_Success() {
        webClient.get()
                .uri("/monitored-endpoint/all")
                .header("accessToken", USER_ACCESS_TOKEN)
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    public void getAllByWrongUser_Forbidden() {
        webClient.get()
                .uri("/monitored-endpoint/all")
                .header("accessToken", "bad token")
                .exchange()
                .expectStatus().isForbidden();
    }
}
