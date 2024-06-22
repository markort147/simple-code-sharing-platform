package com.markort147.codesharingplatform.services;

import com.markort147.codesharingplatform.models.Snippet;
import com.markort147.codesharingplatform.repositories.SnippetsRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class SnippetService {

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

    private static Snippet buildSnippetFromNewSnippetDto(NewSnippetDto inputSnippet) {
        Snippet snippet = new Snippet();
        snippet.setCode(inputSnippet.code());
        snippet.setDate(LocalDateTime.now());
        snippet.setTime(inputSnippet.time());
        snippet.setViews(inputSnippet.views());
        return snippet;
    }

    public List<Snippet> getLastUpdatedSnippets() {
        return snippetsRepository.findNotRestricted(PageRequest.of(0, 10));
    }

    private static SavedSnippetDto buildSavedSnippetDtoFromSnippet(Snippet snippet) {
        return new SavedSnippetDto(snippet.getId());
    }

    public SavedSnippetDto addSnippetByCode(NewSnippetDto newSnippetDto) {
        Snippet snippet = buildSnippetFromNewSnippetDto(newSnippetDto);
        snippet = snippetsRepository.save(snippet);
        log.info("Snippet added: %s".formatted(snippet));

        return buildSavedSnippetDtoFromSnippet(snippet);
    }

    public record NewSnippetDto(
            String code,
            Long time,
            Integer views
    ) {
    }

    public record SavedSnippetDto(String id) {
    }
}
