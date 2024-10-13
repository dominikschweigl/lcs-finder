package dominikschweigl.github.api.provider;

import dominikschweigl.github.dto.Commit;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class ApacheCommitProviderTest {
    public static final CommitProviderFactory providerFactory = new ApacheCommitProviderFactory();

    @Test void getCommitsStartsWithItself() throws IOException {
        Commit A = new Commit("A", null);
        Commit B = new Commit("B", List.of(A));
        Commit C = new Commit("C", List.of(A));
        Commit D = new Commit("D", List.of(B, C));

        CommitProvider commitsFinder = providerFactory.create(List.of(A,B,C,D));

        Assertions.assertSame(D, commitsFinder.getCommitsStartingAt("D").getFirst());
    }

    @Test
    public void getCommitsFromLinearHistory() throws IOException{
        Commit A = new Commit("A", null);
        Commit B = new Commit("B", List.of(A));
        Commit C = new Commit("C", List.of(B));
        Commit D = new Commit("D", List.of(C));
        Commit E = new Commit("E", null);

        CommitProvider commitsFinder = providerFactory.create(List.of(A,B,C,D,E));

        MatcherAssert.assertThat(commitsFinder.getCommitsStartingAt("D"), Matchers.containsInAnyOrder(A,B,C,D));
    }

    @Test void getCommitsFromDivergentHistory() throws IOException{
        Commit A = new Commit("A", null);
        Commit B = new Commit("B", null);
        Commit C = new Commit("C", List.of(A, B));
        Commit D = new Commit("D", List.of(C));

        CommitProvider commitsFinder = providerFactory.create(List.of(A,B,C,D));

        MatcherAssert.assertThat(commitsFinder.getCommitsStartingAt("C"), Matchers.containsInAnyOrder(A, B, C));
    }

    @Test void getCommitsFromInterconnectedHistory() throws IOException {
        Commit A = new Commit("A", null);
        Commit B = new Commit("B", List.of(A));
        Commit C = new Commit("C", List.of(A));
        Commit D = new Commit("D", List.of(B, C));

        CommitProvider commitsFinder = providerFactory.create(List.of(A,B,C,D));

        MatcherAssert.assertThat(commitsFinder.getCommitsStartingAt("D"), Matchers.containsInAnyOrder(A, B, C, D));
    }
}
