package com.markort147.codesharingplatform.controllers;

import com.markort147.codesharingplatform.models.Snippet;
import com.markort147.codesharingplatform.services.SnippetService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class WebController {

    private final FreeMarkerConfigurer freeMarkerConfigurer;
    private final SnippetService snippetService;
    private static final HttpHeaders HEADERS = new HttpHeaders();

    static {
        HEADERS.setContentType(MediaType.TEXT_HTML);
    }

    @Autowired
    public WebController(FreeMarkerConfigurer freeMarkerConfigurer, SnippetService snippetService) {
        this.freeMarkerConfigurer = freeMarkerConfigurer;
        this.snippetService = snippetService;
    }

    @GetMapping("/code/{id}")
    public ResponseEntity<String> getCode(@PathVariable String id) {
        Optional<Snippet> snippetOpt = snippetService.getSnippet(id);
        if (snippetOpt.isPresent()) {
            try {
                return buildResponseForGetCodeTemplate("Code", List.of(snippetOpt.get()));
            } catch (IOException | TemplateException e) {
                return new ResponseEntity<>("Error processing template", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return ResponseEntity.notFound().headers(HEADERS).build();
        }
    }

    @GetMapping("/code/latest")
    public ResponseEntity<String> getCode() {
        List<Snippet> snippets = snippetService.getLastUpdatedSnippets();
        if (snippets.isEmpty()) {
            return ResponseEntity.notFound().headers(HEADERS).build();
        } else {
            try {
                return buildResponseForGetCodeTemplate("Latest", snippets);
            } catch (IOException | TemplateException e) {
                return new ResponseEntity<>("Error processing template", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    private ResponseEntity<String> buildResponseForGetCodeTemplate(String pageTitle, List<Snippet> snippets) throws IOException, TemplateException {
        Template template = freeMarkerConfigurer.getConfiguration().getTemplate("getCode.ftlh");
        Map<String, Object> templateModel = Map.of(
                "title", pageTitle,
                "snippets", snippets.stream().map(snippet -> Map.of(
                        "code", snippet.getCode(),
                        "date", snippet.getDateAsString(),
                        "time", snippet.getTimeAsLong(),
                        "views", snippet.getViews(),
                        "is_time_restricted", snippet.isTimeRestricted(),
                        "is_view_restricted", snippet.isViewRestricted()
                )).toList()
        );
        StringWriter writer = new StringWriter();
        template.process(templateModel, writer);
        String html = writer.toString();
        return ResponseEntity.ok().headers(HEADERS).body(html);
    }

    @GetMapping("/code/new")
    public String getNewCodeForm() {
        return "newCode";
    }

}
