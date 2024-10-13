package dominikschweigl.github.dto;

public record GitHubParent (
        String sha,
        String url,
        String html_url
) implements Parent {
    public GitHubParent(String sha) {
        this(sha, "", "");
    }
}