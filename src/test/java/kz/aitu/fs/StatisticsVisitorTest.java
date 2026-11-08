package kz.aitu.fs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class StatisticsVisitorTest {

    @Test
    void countsFilesDirectoriesAndDepth() {
        Statistics stats = new StatisticsVisitor().collect(TestTrees.sample());

        assertEquals(new Statistics(6, 6, 3), stats);
    }

    @Test
    void emptyRootDirectoryCountsItself() {
        Statistics stats = new StatisticsVisitor().collect(new DirectoryNode("empty"));

        assertEquals(new Statistics(0, 1, 0), stats);
    }

    @Test
    void fileRootHasDepthZero() {
        Statistics stats = new StatisticsVisitor().collect(new FileNode("a.txt", 1));

        assertEquals(new Statistics(1, 0, 0), stats);
    }

    @Test
    void depthFollowsDeepestBranch() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode a = new DirectoryNode("a");
        DirectoryNode b = new DirectoryNode("b");
        DirectoryNode c = new DirectoryNode("c");
        root.add(a);
        a.add(b);
        b.add(c);
        root.add(new FileNode("top.txt", 1));

        assertEquals(new Statistics(1, 4, 3), new StatisticsVisitor().collect(root));
    }
}