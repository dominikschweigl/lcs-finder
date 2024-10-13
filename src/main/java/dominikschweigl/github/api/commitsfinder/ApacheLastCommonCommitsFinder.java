package dominikschweigl.github.api.commitsfinder;

import dominikschweigl.github.api.provider.CommitProvider;
import dominikschweigl.github.dto.Commit;
import dominikschweigl.github.dto.Parent;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Queue;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ApacheLastCommonCommitsFinder implements LastCommonCommitsFinder {
    final private CommitProvider provider;

    final private Map<String, Commit> commits = new HashMap<>();

    @Override
    public Collection<String> findLastCommonCommits(String branchA, String branchB) throws IOException {
        List<Commit> commitsBranchA = provider.getCommitsStartingAt(branchA);
        commitsBranchA.forEach(c -> commits.put(c.sha(), c));

        List<Commit> commitsBranchB = provider.getCommitsStartingAt(branchB);
        commitsBranchB.forEach(c -> commits.put(c.sha(), c));

        Set<String> reachableFromLCC = new HashSet<>();

        Queue<String> queueA = new LinkedList<>();
        Set<String> seenA = new HashSet<>();
        queueA.add(commitsBranchA.getFirst().sha());
        seenA.add(commitsBranchA.getFirst().sha());

        Queue<String> queueB = new LinkedList<>();
        Set<String> seenB = new HashSet<>();
        queueB.add(commitsBranchB.getFirst().sha());
        seenB.add(commitsBranchB.getFirst().sha());

        Set<String> lastCommonCommits = new HashSet<>();

        // do a breadth first search from the branch commits and add to last common commits if reachable from both
        // branches and not reachable from any other lastCommonCommit
        while (!queueA.isEmpty() || !queueB.isEmpty()) {
            searchStep(queueA, seenA, seenB, reachableFromLCC, lastCommonCommits);
            searchStep(queueB, seenB, seenA, reachableFromLCC, lastCommonCommits);
        }

        return lastCommonCommits.stream().filter(sha -> !reachableFromLCC.contains(sha)).toList();
    }

    /**
     * Executes one search step in the breadth first search for finding
     * last common commits of 2 branches
     *
     * @param queue queue containing SHAs of the breadth first search
     * @param seen set of seen SHAs of current branch
     * @param seenByOtherBranch set of seen SHAs of corresponding branch
     * @param reachableFromLCC set of SHAs already reachable by last common commits
     * @param lastCommonCommits set of already found last common commits
     * @throws IOException if fetching commits from provider fails
     */
    private void searchStep(@NotNull Queue<String> queue, Set<String> seen, Set<String> seenByOtherBranch, Set<String> reachableFromLCC, Set<String> lastCommonCommits) throws IOException {
        if (!queue.isEmpty()) {
            String currentSha = queue.poll();
            seen.add(currentSha);

            if (!commits.containsKey(currentSha)) {
                commits.putAll(
                        provider.getCommitsStartingAt(currentSha)
                        .stream()
                        .collect(Collectors.toMap(Commit::sha, Function.identity()))
                );
                if (reachableFromLCC.contains(currentSha)) {
                    reachableFromLCC.addAll(reachableCommits(currentSha, commits, reachableFromLCC));
                }
            }

            if (reachableFromLCC.contains(currentSha)) { return; }

            if (seenByOtherBranch.contains(currentSha)) {
                lastCommonCommits.add(currentSha);
                reachableFromLCC.addAll(reachableCommits(currentSha, commits, reachableFromLCC));
            } else {
                Commit currentCommit = commits.get(currentSha);
                if (currentCommit.parents() != null) {
                    currentCommit.parents().forEach(c -> queue.add(c.sha()));
                }
            }
        }
    }

    /**
     * Finds SHAs of the commits that are newly reachable from a given root
     * sha
     *
     * @param rootSha sha of root commit
     * @param commits map of commits in the history
     * @param alreadyReachableCommits set of SHAs that are already reachable
     * @return Collection of SHAs of commits that are newly reachable
     */
    private @NotNull Collection<String> reachableCommits(String rootSha, Map<String, Commit> commits, Set<String> alreadyReachableCommits) {
        List<String> reachableCommits = new ArrayList<>();

        Queue<String> queue = new LinkedList<>();
        queue.add(rootSha);
        while (!queue.isEmpty()) {
            Commit commit = commits.get(queue.poll());
            if (alreadyReachableCommits.contains(commit.sha())) { continue; }
            if (commit.parents() == null) { continue; }
            commit.parents().stream().map(Parent::sha).forEach(reachableCommits::add);
            commit.parents().stream().map(Parent::sha).forEach(queue::add);
        }

        return reachableCommits;
    }

    public ApacheLastCommonCommitsFinder (CommitProvider provider) {
        this.provider = provider;
    }
}
