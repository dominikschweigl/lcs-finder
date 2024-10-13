package dominikschweigl.github.api.commitsfinder;

import dominikschweigl.github.api.provider.CommitProvider;

public interface LastCommonCommitsFinderFactory {

    /**
     * Creates an instance of LastCommonCommitsFinder for a particular GitHub.com repository.
     * This method must not check connectivity.
     *
     * @param owner repository owner
     * @param repo  repository name
     * @param token personal access token or null for anonymous access
     * @return an instance of LastCommonCommitsFinder
     */
    LastCommonCommitsFinder create(String owner, String repo, String token);

    /**
     * Creates an instance of LastCommonCommitsFinder for a custom commit provider.
     * This method must not check connectivity.
     *
     * @param provider repository owner
     * @return an instance of LastCommonCommitsFinder
     */
    LastCommonCommitsFinder create(CommitProvider provider);
}
