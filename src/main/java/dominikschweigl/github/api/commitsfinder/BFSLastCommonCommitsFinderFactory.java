package dominikschweigl.github.api.commitsfinder;

import dominikschweigl.github.api.provider.ApacheCommitProvider;
import dominikschweigl.github.api.provider.CommitProvider;

public class BFSLastCommonCommitsFinderFactory implements LastCommonCommitsFinderFactory {

    @Override
    public LastCommonCommitsFinder create(String owner, String repo, String token) {
        CommitProvider provider = new ApacheCommitProvider(owner, repo, token);

        return new BFSLastCommonCommitsFinder(provider);
    }

    public LastCommonCommitsFinder create(CommitProvider provider) {
        return new BFSLastCommonCommitsFinder(provider);
    }
}
