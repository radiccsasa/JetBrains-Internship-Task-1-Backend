package com.example.queryapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/queries")
public class QueryController {

    @PostMapping
    public ResponseEntity<?> addQuery(@RequestBody String SqlText)
    {
        return ResponseEntity.status();
    }

    @GetMapping
    public List<Map<String, Object>> listAll(){

    }


    @GetMapping("/execute")
    public execute(@RequestParam("query") Long id)
    {
        
    }

}
