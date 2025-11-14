package com.example.queryapp.controller;

import com.example.queryapp.model.Query;
import com.example.queryapp.repository.QueryRepository;
import com.example.queryapp.service.QueryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/queries")
public class QueryController {

    private final QueryService queryService;

    public QueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    @PostMapping
    public Map<String, Long> addQuery(@RequestBody String SqlText)
    {
        return Map.of("Id", queryService.saveQuery(SqlText));
    }

    @GetMapping
    public List<Query> listAll(){
        return queryService.getAll();
    }


    @GetMapping("/execute")
    public List<List<Object>> execute(@RequestParam("query") Long id)
    {
        return queryService.executeQuery(id);
    }

}
