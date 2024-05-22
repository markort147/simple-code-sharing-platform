package platform.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import platform.dtos.AddedSnippetDto;
import platform.dtos.NewSnippetDto;
import platform.models.Snippet;
import platform.services.SnippetService;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final SnippetService snippetService;
    private final static HttpHeaders HEADERS;

    static {
        HEADERS = new HttpHeaders();
        HEADERS.setContentType(MediaType.APPLICATION_JSON);
    }

    @Autowired
    public ApiController(SnippetService snippetService) {
        this.snippetService = snippetService;
    }

    @GetMapping("/code/{id}")
    public ResponseEntity<?> getCode(@PathVariable String id) {
        logger.info("Getting code for Snippet with id: " + id);
        Optional<Snippet> snippet = snippetService.getSnippet(id);
        if (snippet.isPresent()) {
            logger.info("Returning Snippet: " + snippet.get());
            return ResponseEntity.ok().headers(HEADERS).body(snippet.get());
        }
        logger.info("Snippet not found");
        return ResponseEntity.notFound().headers(HEADERS).build();
    }

    @GetMapping("/code/latest")
    public ResponseEntity<?> getCode() {
        List<Snippet> snippets = snippetService.getLastUpdatedSnippets();
        if (snippets.isEmpty()) {
            return ResponseEntity.notFound().headers(HEADERS).build();
        }
        return ResponseEntity.ok().headers(HEADERS).body(snippets);
    }

    @PostMapping("/code/new")
    public ResponseEntity<AddedSnippetDto> postNewCode(@RequestBody NewSnippetDto inputSnippet) {
        logger.info("Posting new Snippet: " + inputSnippet);
        String id = snippetService.addSnippetByCode(inputSnippet);
        return ResponseEntity.ok().headers(HEADERS).body(new AddedSnippetDto(id));
    }

}
