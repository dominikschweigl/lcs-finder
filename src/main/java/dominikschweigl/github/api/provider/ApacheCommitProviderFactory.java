package dominikschweigl.github.api.provider;

import dominikschweigl.github.dto.Commit;

import java.util.Collection;

public class ApacheCommitProviderFactory implements CommitProviderFactory {

    @Override
    public CommitProvider create(String owner, String repo, String token) {
        return new ApacheCommitProvider(owner, repo, token);
    }

    @Override
    public CommitProvider create(Collection<Commit> commits) {
        return new CustomCommitProvider(commits);
    }
}
