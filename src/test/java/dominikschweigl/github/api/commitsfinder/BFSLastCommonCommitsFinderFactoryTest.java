package dominikschweigl.github.api.commitsfinder;


import dominikschweigl.github.api.provider.ApacheCommitProviderFactory;
import dominikschweigl.github.api.provider.CommitProviderFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BFSLastCommonCommitsFinderFactoryTest {

    @Test
    public void createFactoryTest() {
        LastCommonCommitsFinderFactory factory = new BFSLastCommonCommitsFinderFactory();
        Assertions.assertInstanceOf(LastCommonCommitsFinderFactory.class, factory);
    }

    @Test
    public void createCommitFinderTest() {
        LastCommonCommitsFinderFactory factory = new BFSLastCommonCommitsFinderFactory();
        Assertions.assertInstanceOf(LastCommonCommitsFinder.class, factory.create("", "", null));
    }

    @Test
    public void createCustomCommitFinderTest() {
        LastCommonCommitsFinderFactory factory = new BFSLastCommonCommitsFinderFactory();
        CommitProviderFactory providerFactory = new ApacheCommitProviderFactory();
        Assertions.assertInstanceOf(LastCommonCommitsFinder.class, factory.create(providerFactory.create("", "", "")));
    }
}
