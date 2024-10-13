package dominikschweigl.github.api.provider;

import dominikschweigl.github.dto.Commit;

import java.io.IOException;
import java.util.List;

public interface CommitProvider {

    /**
     * Creates a List of ancestors to given commit
     *
     * @param commit SHA of commit/branch or branch name
     * @return List of ancestors to given commit with itself at index 0
     * @throws IOException if any error occurs
     * */
    List<Commit> getCommitsStartingAt(String commit) throws IOException;
}