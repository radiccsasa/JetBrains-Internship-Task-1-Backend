package com.example.queryapp.service;

import com.example.queryapp.model.Query;
import com.example.queryapp.repository.QueryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class QueryServiceTest {

    @Autowired
    private QueryService queryService;
    @Autowired
    private QueryRepository queryRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDatabase() {
        queryRepository.deleteAll();
    }

    @Test
    void testSaveQueryAndGetAll() {
        Long id = queryService.saveQuery("SELECT * FROM passengers");
        assertThat(id).isNotNull();

        List<Query> queries = queryService.getAll();
        assertThat(queries.get(0).getText()).isEqualTo("SELECT * FROM passengers");
    }

    @Test
    void testExecuteQuery() {
        Long id = queryService.saveQuery("SELECT * FROM passengers LIMIT 2");

        List<List<Object>> result = queryService.executeQuery(id);

        assertThat(result).isNotNull();
        assertThat(result.size()).isGreaterThan(0);
        assertThat(result.get(0).size()).isGreaterThan(0);
    }

    @Test
    void testExecuteQueryOnlySelectAllowed() {
        Long id = queryService.saveQuery("DELETE FROM passengers");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            queryService.executeQuery(id);
        });

        assertThat(exception.getMessage()).isEqualTo("Only SELECT queries are allowed");
    }

    @Test
    void testExecuteQueryAsync() throws ExecutionException, InterruptedException {
        Long id = queryService.saveQuery("SELECT * FROM passengers LIMIT 2");

        CompletableFuture<List<List<Object>>> future = queryService.executeQueryAsync(id);

        List<List<Object>> result = future.get();

        assertThat(result).isNotNull();
        assertThat(result.size()).isGreaterThan(0);
    }

    @Test
    void testCache() {
        Long id = queryService.saveQuery("SELECT * FROM passengers LIMIT 1");

        List<List<Object>> first = queryService.executeQuery(id);

        List<List<Object>> second = queryService.executeQuery(id);

        assertThat(second).isSameAs(first);
    }

}
