package dominikschweigl.github.dto;

import java.util.List;
import java.util.Objects;

public record Commit  (
        String sha,
        String node_id,
        CommitMetaData commit,
        String url,
        String html_url,
        String comments_url,
        Author author,
        Committer committer,
        List<GitHubParent> parents
) implements Parent {
    public Commit(String sha, List<Parent> parents) {
        this(
                sha,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                parents != null
                        ? parents.stream().map(p -> new GitHubParent(p.sha())).toList()
                        : null
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Commit commit = (Commit) o;
        return Objects.equals(sha, commit.sha());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sha);
    }
}