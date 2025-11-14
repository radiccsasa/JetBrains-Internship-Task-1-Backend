package com.example.queryapp.controller;

import com.example.queryapp.model.Query;
import com.example.queryapp.repository.QueryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QueryControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private QueryRepository queryRepository;

    @BeforeEach
    void cleanDatabase() {
        queryRepository.deleteAll();
    }

    @Test
    void testAddQuery() {
        ResponseEntity<Map> response = restTemplate.postForEntity("/queries", "SELECT * FROM passengers", Map.class);
        Map<String, Object> body = (Map<String, Object>) response.getBody();

// AssertJ sada zna da je Map i može da koristi containsKey
        assertThat(body).isNotNull();
        assertThat(body.containsKey("Id")).isTrue();
    }

    @Test
    void testListAllQueries() {
        queryRepository.save(new Query("SELECT * FROM passengers"));
        queryRepository.save(new Query("SELECT * FROM passengers LIMIT 2"));

        ResponseEntity<Query[]> response = restTemplate.getForEntity("/queries", Query[].class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody().length).isEqualTo(2);
    }

    @Test
    void testExecuteQueryEndpoint() {
        Query q = queryRepository.save(new Query("SELECT * FROM passengers LIMIT 2"));

        ResponseEntity<List> response = restTemplate.getForEntity("/queries/execute?query=" + q.getId(), List.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody().size()).isGreaterThan(0);
    }
}
