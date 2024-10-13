package dominikschweigl.github.api.commitsfinder;

import dominikschweigl.github.api.provider.ApacheCommitProvider;
import dominikschweigl.github.api.provider.CommitProvider;

public class ApacheLastCommonCommitsFinderFactory implements LastCommonCommitsFinderFactory {

    @Override
    public LastCommonCommitsFinder create(String owner, String repo, String token) {
        CommitProvider provider = new ApacheCommitProvider(owner, repo, token);

        return new ApacheLastCommonCommitsFinder(provider);
    }

    public LastCommonCommitsFinder create(CommitProvider provider) {
        return new ApacheLastCommonCommitsFinder(provider);
    }
}
