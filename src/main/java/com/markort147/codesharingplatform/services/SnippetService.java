package com.markort147.codesharingplatform.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.markort147.codesharingplatform.dtos.NewSnippetDto;
import com.markort147.codesharingplatform.models.Snippet;
import com.markort147.codesharingplatform.repositories.SnippetsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class SnippetService {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final SnippetsRepository snippetsRepository;

    @Autowired
    public SnippetService(SnippetsRepository snippetsRepository) {
        this.snippetsRepository = snippetsRepository;
    }

    public Optional<Snippet> getSnippet(String id) {
        Optional<Snippet> snippetOptional = snippetsRepository.findById(id);
        if (snippetOptional.isPresent()) {
            Snippet snippet = snippetOptional.get();
            if (snippet.isTimeExpired()) {
                snippetsRepository.delete(snippet);
                return Optional.empty();
            }
            if (updateVisitCountdownAndCheckIfShouldBeRemoved(snippet)) {
                snippetsRepository.delete(snippet);
            } else {
                snippetsRepository.save(snippet);
            }
        }
        return snippetOptional;
    }

    private boolean updateVisitCountdownAndCheckIfShouldBeRemoved(Snippet snippet) {
        if (snippet.isViewRestricted()) {
            snippet.decrementVisitCountdown();
            return snippet.areViewsExpired();
        }
        return false;
    }

    public String addSnippetByCode(NewSnippetDto inputSnippet) {
        Snippet snippet = mapNewSnippetToSnippet(inputSnippet);
        String id = snippetsRepository.save(snippet).getId();
        snippet.setId(id);
        logger.info("Snippet added: " + snippet);

        return id;
    }

    public List<Snippet> getLastUpdatedSnippets() {
        return snippetsRepository.findNotRestricted(PageRequest.of(0, 10));
    }

    private Snippet mapNewSnippetToSnippet(NewSnippetDto inputSnippet) {
        Snippet snippet = new Snippet();
        snippet.setCode(inputSnippet.code());
        snippet.setDate(LocalDateTime.now());
        snippet.setTime(inputSnippet.time());
        snippet.setViews(inputSnippet.views());
        return snippet;
    }

}
