package dominikschweigl.github.api.commitsfinder;


import dominikschweigl.github.api.provider.ApacheCommitProviderFactory;
import dominikschweigl.github.api.provider.CommitProviderFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ApacheLastCommonCommitsFinderFactoryTest {

    @Test
    public void createFactoryTest() {
        LastCommonCommitsFinderFactory factory = new ApacheLastCommonCommitsFinderFactory();
        Assertions.assertInstanceOf(LastCommonCommitsFinderFactory.class, factory);
    }

    @Test
    public void createCommitFinderTest() {
        LastCommonCommitsFinderFactory factory = new ApacheLastCommonCommitsFinderFactory();
        Assertions.assertInstanceOf(LastCommonCommitsFinder.class, factory.create("", "", null));
    }

    @Test
    public void createCustomCommitFinderTest() {
        LastCommonCommitsFinderFactory factory = new ApacheLastCommonCommitsFinderFactory();
        CommitProviderFactory providerFactory = new ApacheCommitProviderFactory();
        Assertions.assertInstanceOf(LastCommonCommitsFinder.class, factory.create(providerFactory.create("", "", "")));
    }
}
