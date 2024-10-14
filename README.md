# lcs-finder
Library for finding the last common commits of 2 github branches/commits or from a custom collection of commits

## Usage
```Java
public class Main {
    public static void main(String[] args) {
        LastCommonCommitsFinderFactory factory = new ApacheLastCommonCommitsFinderFactory();
        LastCommonCommitsFinder commitsFinder = factory.create("Doriandarko","o1-engineer", null);
        try {
            System.out.println(commitsFinder.findLastCommonCommits("better", "main"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
```
