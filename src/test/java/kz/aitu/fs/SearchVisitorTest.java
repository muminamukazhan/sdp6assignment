package kz.aitu.fs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class SearchVisitorTest {

    @Test
    void combinesNameAndSizeConditions() {
        SearchVisitor visitor = new SearchVisitor(name -> name.endsWith(".txt"), 120);

        assertEquals(List.of("docs/archive/old.txt", "docs/notes.txt"),
                visitor.search(TestTrees.sample()));
    }

    @Test
    void sizeThresholdIsInclusive() {
        SearchVisitor visitor = new SearchVisitor(name -> name.equals("readme.txt"), 100);

        assertEquals(List.of("readme.txt"), visitor.search(TestTrees.sample()));
    }

    @Test
    void nameConditionExcludesLargeFilesOfOtherNames() {
        SearchVisitor visitor = new SearchVisitor(name -> name.endsWith(".java"), 0);

        assertEquals(List.of("src/Main.java", "src/util/Helper.java"),
                visitor.search(TestTrees.sample()));
    }

    @Test
    void pathsAreRootRelativeAndSorted() {
        SearchVisitor visitor = new SearchVisitor(name -> name.endsWith(".txt"), 0);

        assertEquals(List.of("docs/archive/old.txt", "docs/notes.txt", "readme.txt"),
                visitor.search(TestTrees.sample()));
    }

    @Test
    void sortingIsByFullPathNotTraversalOrder() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode a = new DirectoryNode("a");
        a.add(new FileNode("x.txt", 1));
        root.add(a);
        root.add(new FileNode("a.txt", 1));

        assertEquals(List.of("a.txt", "a/x.txt"),
                new SearchVisitor(name -> true, 0).search(root));
    }

    @Test
    void noMatchReturnsEmptyList() {
        SearchVisitor visitor = new SearchVisitor(name -> name.endsWith(".exe"), 0);

        assertTrue(visitor.search(TestTrees.sample()).isEmpty());
    }

    @Test
    void emptyDirectoryReturnsEmptyList() {
        SearchVisitor visitor = new SearchVisitor(name -> true, 0);

        assertTrue(visitor.search(new DirectoryNode("empty")).isEmpty());
    }

    @Test
    void fileAsRootReturnsItsName() {
        SearchVisitor visitor = new SearchVisitor(name -> true, 0);

        assertEquals(List.of("a.txt"), visitor.search(new FileNode("a.txt", 1)));
    }
}