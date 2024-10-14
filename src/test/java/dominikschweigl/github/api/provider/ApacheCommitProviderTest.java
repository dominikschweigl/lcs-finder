package dominikschweigl.github.api.provider;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ApacheCommitProviderTest {
    public static final CommitProviderFactory providerFactory = new ApacheCommitProviderFactory();

    @Test
    void testNonExistentRepository() {
        CommitProvider provider = providerFactory.create("__non__", "__existent__", null);
        Assertions.assertThrows(IOException.class, () -> {provider.getCommitsStartingAt("");});
    }

    @Test
    void testInvalidToken() {
        CommitProvider provider = providerFactory.create("JetBrains", "intellij-community", "invalid");
        Assertions.assertThrows(IOException.class, () -> {provider.getCommitsStartingAt("master");});
    }

    @Test
    void testGetJetBrainsIntellijCommunity() throws IOException {
        CommitProvider provider = providerFactory.create("JetBrains", "intellij-community", null);
        Assertions.assertTrue(!provider.getCommitsStartingAt("master").isEmpty());
    }
}
