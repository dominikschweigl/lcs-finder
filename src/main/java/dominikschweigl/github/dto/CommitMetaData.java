package dominikschweigl.github.dto;

public record CommitMetaData(
        Author author,
        Committer committer,
        String message,
        Tree tree,
        String url,
        int comment_count,
        Verification verification
) {}