package kz.aitu.fs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TreeIntegrityTest {

    @Test
    void rejectsNegativeFileSize() {
        assertThrows(IllegalArgumentException.class, () -> new FileNode("a.txt", -1));
    }

    @Test
    void acceptsZeroFileSize() {
        assertEquals(0, new FileNode("a.txt", 0).size());
    }

    @Test
    void rejectsInvalidNames() {
        assertThrows(IllegalArgumentException.class, () -> new FileNode("", 1));
        assertThrows(IllegalArgumentException.class, () -> new FileNode("  ", 1));
        assertThrows(IllegalArgumentException.class, () -> new DirectoryNode("a/b"));
        assertThrows(IllegalArgumentException.class, () -> new DirectoryNode(null));
    }

    @Test
    void rejectsDuplicateSiblingNamesAndKeepsStructure() {
        DirectoryNode dir = new DirectoryNode("dir");
        dir.add(new FileNode("x", 1));

        assertThrows(IllegalArgumentException.class, () -> dir.add(new FileNode("x", 2)));
        assertThrows(IllegalArgumentException.class, () -> dir.add(new DirectoryNode("x")));
        assertEquals(1, dir.children().size());
    }

    @Test
    void rejectsSecondParent() {
        DirectoryNode first = new DirectoryNode("first");
        DirectoryNode second = new DirectoryNode("second");
        FileNode file = new FileNode("a.txt", 1);
        first.add(file);

        assertThrows(IllegalArgumentException.class, () -> second.add(file));
        assertTrue(second.children().isEmpty());
    }

    @Test
    void rejectsAddingDirectoryToItself() {
        DirectoryNode dir = new DirectoryNode("dir");

        assertThrows(IllegalArgumentException.class, () -> dir.add(dir));
    }

    @Test
    void rejectsCycleThroughAncestor() {
        DirectoryNode a = new DirectoryNode("a");
        DirectoryNode b = new DirectoryNode("b");
        DirectoryNode c = new DirectoryNode("c");
        a.add(b);
        b.add(c);

        assertThrows(IllegalArgumentException.class, () -> c.add(a));
        assertTrue(c.children().isEmpty());
    }

    @Test
    void rejectsNullChild() {
        assertThrows(IllegalArgumentException.class, () -> new DirectoryNode("d").add(null));
    }

    @Test
    void childrenViewIsReadOnly() {
        DirectoryNode dir = new DirectoryNode("dir");
        dir.add(new FileNode("a.txt", 1));

        assertThrows(UnsupportedOperationException.class, () -> dir.children().clear());
    }

    @Test
    void supportsDeepValidNesting() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode current = root;
        for (int i = 0; i < 50; i++) {
            DirectoryNode next = new DirectoryNode("level" + i);
            current.add(next);
            current = next;
        }
        current.add(new FileNode("deep.txt", 7));

        assertEquals(new Statistics(1, 51, 51), new StatisticsVisitor().collect(root));
        assertEquals(7, new TotalSizeVisitor().calculate(root));
    }

    @Test
    void removedNodeCanBeAttachedElsewhere() {
        DirectoryNode first = new DirectoryNode("first");
        DirectoryNode second = new DirectoryNode("second");
        FileNode file = new FileNode("a.txt", 1);
        first.add(file);

        assertTrue(first.remove("a.txt"));
        second.add(file);

        assertTrue(first.children().isEmpty());
        assertEquals(1, second.children().size());
        assertFalse(first.remove("a.txt"));
    }

    @Test
    void sameNameAllowedInDifferentDirectories() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode a = new DirectoryNode("a");
        DirectoryNode b = new DirectoryNode("b");
        root.add(a);
        root.add(b);

        a.add(new FileNode("same.txt", 1));
        b.add(new FileNode("same.txt", 2));

        assertEquals(3, new TotalSizeVisitor().calculate(root));
    }
}