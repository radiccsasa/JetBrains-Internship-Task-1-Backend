package com.example.queryapp.service;

import com.example.queryapp.model.Query;
import com.example.queryapp.repository.QueryRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class QueryService {

    private final QueryRepository queryRepository;
    private final JdbcTemplate jdbcTemplate;

    private Map<Long, List<List<Object>>> queryCache = new ConcurrentHashMap<>();

    public QueryService(QueryRepository queryRepository, JdbcTemplate jdbcTemplate) {
        this.queryRepository = queryRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long saveQuery(String text)
    {
        Query q = new Query();
        q.setText(text);
        return queryRepository.save(q).getId();
    }

    public List<Query> getAll()
    {
        return queryRepository.findAll();
    }

    public List<List<Object>> executeQuery(Long id)
    {
        //when you execute query more than once, result as they are saved in cashe, we return them from cashe
        if (queryCache.containsKey(id)) {
            return queryCache.get(id);
        }

        Query query = queryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Query not found"));

        String sqlText = query.getText();

        //only select queries are allowed
        if (!sqlText.trim().toUpperCase().startsWith("SELECT")) {
            throw new RuntimeException("Only SELECT queries are allowed");
        }

        return jdbcTemplate.query(sqlText, rs -> {
            List<List<Object>> rows = new ArrayList<>();
            int columnCount = rs.getMetaData().getColumnCount();

            while (rs.next())
            {
                List<Object> row = new ArrayList<>();
                for (int i=1; i <=columnCount; i++)
                {
                    row.add(rs.getObject(i));
                }
                rows.add(row);
            }
            return rows;
        });


    }

    @Async
    public CompletableFuture<List<List<Object>>> executeQueryAsync(Long id) {
        List<List<Object>> result = executeQuery(id);
        return CompletableFuture.completedFuture(result);
    }

}
