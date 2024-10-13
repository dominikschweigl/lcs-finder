package dominikschweigl.github.api.provider;

import dominikschweigl.github.dto.Commit;

import java.util.Collection;

public interface CommitProviderFactory {

    /**
     * Creates an instance of CommitProvider for a particular GitHub.com repository.
     * This method must not check connectivity.
     *
     * @param owner repository owner
     * @param repo  repository name
     * @param token personal access token or null for anonymous access
     * @return an instance of CommitProvider
     */
    CommitProvider create(String owner, String repo, String token);

    /**
     * Creates an instance of CommitProvider from a custom collection of commits.
     * This method must not check connectivity.
     *
     * @param commits collection of provided commits
     * @return an instance of CommitProvider
     */
    CommitProvider create(Collection<Commit> commits);
}
