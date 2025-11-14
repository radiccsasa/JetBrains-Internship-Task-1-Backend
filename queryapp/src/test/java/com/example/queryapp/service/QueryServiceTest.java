package com.example.queryapp.service;

import com.example.queryapp.model.Query;
import com.example.queryapp.repository.QueryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class QueryServiceTest {

    @Autowired
    private QueryService queryService;
    @Autowired
    private QueryRepository queryRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void test_1()
    {
        Query q = new Query();
        q.setText("SELECT * FROM passengers");
        queryRepository.save(q);

        //test to add query
        Long queryId = q.getId();
        assertThat(queryId).isNotNull();

        //executing added query
        List<List<Object>> result = queryService.executeQuery(queryId);

        //did we get result from database
        assertThat(result).isNotNull();
        assertThat(result.size()).isGreaterThan(0);
    }


}
