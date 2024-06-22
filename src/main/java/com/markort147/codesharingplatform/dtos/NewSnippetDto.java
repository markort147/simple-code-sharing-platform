package com.markort147.codesharingplatform.dtos;

public record NewSnippetDto(
        String code,
        Long time,
        Integer views
) {
}
