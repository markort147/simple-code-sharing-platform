package platform.dtos;

public record NewSnippetDto(
        String code,
        Long time,
        Integer views
) {
}
