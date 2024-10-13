package dominikschweigl.github.api.provider;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import dominikschweigl.github.dto.Commit;
import org.apache.commons.io.IOUtils;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ApacheCommitProvider implements CommitProvider {
    private final String owner;
    private final String repo;
    private final String token;

    @Override
    public List<Commit> getCommitsStartingAt(String commit) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet get = new HttpGet("https://api.github.com/repos/" + owner + "/" + repo + "/commits?per_page=100&sha=" + commit);

            if (token != null) {
                get.addHeader("Authorization", "Bearer " +  token);
            }

            String commits = httpClient.execute(get, response -> {
                if (!Integer.toString(response.getCode()).startsWith("2") ) {
                    throw new IOException(response.getCode() + " " + response.getReasonPhrase());
                }
                return IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
            });

            Type listType = new TypeToken<ArrayList<Commit>>() {}.getType();
            return new Gson().fromJson(commits, listType);
        } catch (JsonParseException e) {
            throw new IOException(e);
        }
    }

    public ApacheCommitProvider(String owner, String repo, String token) {
        this.owner = owner;
        this.repo = repo;
        this.token = token;
    }
}
