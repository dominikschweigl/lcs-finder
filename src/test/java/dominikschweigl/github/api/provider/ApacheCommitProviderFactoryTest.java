package dominikschweigl.github.api.provider;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ApacheCommitProviderFactoryTest {

    @Test
    public void createFactoryTest() {
        CommitProviderFactory factory = new ApacheCommitProviderFactory();
        Assertions.assertInstanceOf(CommitProviderFactory.class, factory);
    }

    @Test
    public void createCommitProviderTest() {
        CommitProviderFactory factory = new ApacheCommitProviderFactory();
        Assertions.assertInstanceOf(CommitProvider.class, factory.create("", "", ""));
    }

    @Test
    public void createCustomCommitProviderTest() {
        CommitProviderFactory factory = new ApacheCommitProviderFactory();
        Assertions.assertInstanceOf(CommitProvider.class, factory.create(List.of()));
    }
}
