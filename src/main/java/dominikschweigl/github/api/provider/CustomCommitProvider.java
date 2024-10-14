package dominikschweigl.github.api.provider;

import dominikschweigl.github.dto.Commit;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CustomCommitProvider implements CommitProvider {
    private final Map<String, Commit> commits;

    @Override
    public List<Commit> getCommitsStartingAt(String commit) throws IOException {
        if (!commits.containsKey(commit)) {
            throw new IOException("Commit does not exist.");
        }

        List<Commit> commitsStartingAt = new ArrayList<>();

        Set<String> seen = new HashSet<>();
        seen.add(commit);

        Queue<String> queue = new LinkedList<>();
        queue.add(commit);

        while (!queue.isEmpty() && commitsStartingAt.size() <= 100) {
            String currentSha = queue.poll();
            seen.add(currentSha);

            Commit current = commits.get(currentSha);
            if (current == null) {
                throw new IOException("Graph references Node not contained");
            }
            commitsStartingAt.add(current);

            if (current.parents() != null) {
                current.parents().stream()
                        .filter(c -> !seen.contains(c.sha()))
                        .forEach(c -> {
                            seen.add(c.sha());
                            queue.add(c.sha());
                        });
            }
        }

        return commitsStartingAt;
    }

    public CustomCommitProvider(Collection<Commit> commits) {
        this.commits = commits.stream()
                .collect(Collectors.toMap(Commit::sha, Function.identity()));;
    }
}
