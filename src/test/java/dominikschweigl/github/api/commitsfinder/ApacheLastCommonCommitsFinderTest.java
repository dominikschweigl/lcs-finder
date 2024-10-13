package dominikschweigl.github.api.commitsfinder;

import dominikschweigl.github.api.provider.ApacheCommitProviderFactory;
import dominikschweigl.github.api.provider.CommitProviderFactory;
import dominikschweigl.github.dto.Commit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class ApacheLastCommonCommitsFinderTest {
    private final static LastCommonCommitsFinderFactory commitFinderFactory = new ApacheLastCommonCommitsFinderFactory();
    private final static CommitProviderFactory commitProviderFactory = new ApacheCommitProviderFactory();

    @Test
    public void testFindLastCommonCommitsAncestor() throws IOException {
        Commit A = new Commit("A", null);
        Commit B = new Commit("B", List.of(A));
        Commit C = new Commit("C", List.of(B));

        List<Commit> crissCrossGraph = List.of(A,B,C);

        LastCommonCommitsFinder commitsFinder = commitFinderFactory.create(
                commitProviderFactory.create(crissCrossGraph)
        );

        Collection<String> lastCommonCommits = commitsFinder.findLastCommonCommits("B", "C");

        Assertions.assertIterableEquals(List.of("B"), lastCommonCommits);
    }

    @Test
    public void testFindLastCommonCommitsCrissCross() throws IOException {
        Commit A = new Commit("A", null);
        Commit B = new Commit("B", null);
        Commit C = new Commit("C", List.of(A, B));
        Commit D = new Commit("D", List.of(A, B));

        List<Commit> crissCrossGraph = List.of(A,B,C,D);

        LastCommonCommitsFinder commitsFinder = commitFinderFactory.create(
                commitProviderFactory.create(crissCrossGraph)
        );

        Collection<String> lastCommonCommits = commitsFinder.findLastCommonCommits("C", "D");

        Assertions.assertIterableEquals(List.of("A", "B"), lastCommonCommits);
    }

    @Test
    public void testFindLastCommonCommitsMultipleConnections() throws IOException {
        Commit A = new Commit("A", null);
        Commit B = new Commit("B", List.of(A));
        Commit C = new Commit("C", List.of(A, B));
        Commit D = new Commit("D", List.of(B));
        Commit E = new Commit("E", List.of(C));

        List<Commit> multipleConnectionsGraph = List.of(A,B,C,D,E);

        LastCommonCommitsFinder commitsFinder = commitFinderFactory.create(
                commitProviderFactory.create(multipleConnectionsGraph)
        );

        Collection<String> lastCommonCommits = commitsFinder.findLastCommonCommits("D", "E");

        Assertions.assertIterableEquals(List.of("B"), lastCommonCommits);
    }

    @Test
    public void testFindLastCommonCommitsMultipleMerges() throws IOException {
        Commit A = new Commit("A", null);
        Commit B = new Commit("B", List.of(A));
        Commit C = new Commit("C", List.of(B));
        Commit D = new Commit("D", List.of(B));
        Commit E = new Commit("E", List.of(C, D));
        Commit F = new Commit("F", List.of(A, D));

        List<Commit> multipleConnectionsGraph = List.of(A,B,C,D,E,F);

        LastCommonCommitsFinder commitsFinder = commitFinderFactory.create(
                commitProviderFactory.create(multipleConnectionsGraph)
        );

        Collection<String> lastCommonCommits = commitsFinder.findLastCommonCommits("E", "F");

        Assertions.assertIterableEquals(List.of("D"), lastCommonCommits);
    }

    @Test
    public void testFindLastCommonCommitsInvalidToken() {
        LastCommonCommitsFinder commitsFinder = commitFinderFactory.create("JetBrains", "Kotlin.TeamCity", "invalid token");
        Assertions.assertThrows(IOException.class, () -> commitsFinder.findLastCommonCommits("master", "master"));
    }

    @Test
    public void testFindLastCommonCommitsNonExistingBranch() {
        LastCommonCommitsFinder commitsFinder = commitFinderFactory.create("JetBrains", "Kotlin.TeamCity", null);
        Assertions.assertThrows(IOException.class, () -> commitsFinder.findLastCommonCommits("master", "non-exising-$32394"));
    }

    @Test
    public void testFindLastCommonCommitsEmptyGraph() {
        List<Commit> emptyGraph = List.of();

        LastCommonCommitsFinder commitsFinder = commitFinderFactory.create(
                commitProviderFactory.create(emptyGraph)
        );

        Assertions.assertThrows(IOException.class, () -> commitsFinder.findLastCommonCommits("A", "B"));
    }

    @Test
    public void testFindLastCommonCommitsNonExistingRepo() {
        LastCommonCommitsFinder commitsFinder = commitFinderFactory.create("JetBrains", "Kotlin.TeamCity34", null);
        Assertions.assertThrows(IOException.class, () -> commitsFinder.findLastCommonCommits("master", "master"));
    }
}
