package com.markort147.codesharingplatform.controllers;

import com.markort147.codesharingplatform.models.Snippet;
import com.markort147.codesharingplatform.services.SnippetService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Log
public class ApiController {

    private static final HttpHeaders HEADERS = new HttpHeaders();
    private final SnippetService snippetService;

    static {
        HEADERS.setContentType(MediaType.APPLICATION_JSON);
    }

    @Autowired
    public ApiController(SnippetService snippetService) {
        this.snippetService = snippetService;
    }

    @GetMapping("/code/{id}")
    public ResponseEntity<Snippet> getCode(@PathVariable String id) {
        log.info(() -> "Getting code for Snippet with id: %s".formatted(id));
        return snippetService.getSnippet(id)
                .map(snippet -> ResponseEntity.ok()
                        .headers(HEADERS)
                        .body(snippet))
                .orElseGet(() -> {
                    log.info(() -> "Snippet not found");
                    return ResponseEntity.notFound()
                            .headers(HEADERS)
                            .build();
                });
    }

    @GetMapping("/code/latest")
    public ResponseEntity<List<Snippet>> getCode() {
        List<Snippet> snippets = snippetService.getLastUpdatedSnippets();
        if (snippets.isEmpty()) {
            return ResponseEntity.notFound()
                    .headers(HEADERS)
                    .build();
        }
        return ResponseEntity.ok()
                .headers(HEADERS)
                .body(snippets);
    }

    @PostMapping("/code/new")
    public ResponseEntity<SnippetService.SavedSnippetDto> postNewCode(@RequestBody SnippetService.NewSnippetDto inputSnippet) {
        log.info(() -> "Posting new Snippet: %s".formatted(inputSnippet));
        return ResponseEntity.ok()
                .headers(HEADERS)
                .body(snippetService.addSnippetByCode(inputSnippet));
    }

}
