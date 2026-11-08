package kz.aitu.fs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TotalSizeVisitorTest {

    @Test
    void sumsFilesAcrossNestingLevels() {
        assertEquals(3868, new TotalSizeVisitor().calculate(TestTrees.sample()));
    }

    @Test
    void sumsOnlySubtree() {
        DirectoryNode root = TestTrees.sample();
        DirectoryNode docs = (DirectoryNode) root.children().stream()
                .filter(n -> n.name().equals("docs")).findFirst().orElseThrow();

        assertEquals(2668, new TotalSizeVisitor().calculate(docs));
    }

    @Test
    void emptyDirectoryHasZeroSize() {
        assertEquals(0, new TotalSizeVisitor().calculate(new DirectoryNode("empty")));
    }

    @Test
    void singleFileRootCountsOnce() {
        assertEquals(42, new TotalSizeVisitor().calculate(new FileNode("a.bin", 42)));
    }
}