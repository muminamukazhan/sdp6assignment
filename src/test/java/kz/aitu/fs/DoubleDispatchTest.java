package kz.aitu.fs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class DoubleDispatchTest {

    @Test
    void fileReachesFileOverload() {
        RecordingVisitor visitor = new RecordingVisitor();
        Element element = new FileNode("a.txt", 1);

        element.accept(visitor);

        assertEquals(List.of("file:a.txt"), visitor.calls());
    }

    @Test
    void directoryReachesDirectoryOverload() {
        RecordingVisitor visitor = new RecordingVisitor();
        Element element = new DirectoryNode("docs");

        element.accept(visitor);

        assertEquals(List.of("dir:docs"), visitor.calls());
    }

    @Test
    void acceptDoesNotTraverseChildren() {
        RecordingVisitor visitor = new RecordingVisitor();
        DirectoryNode dir = new DirectoryNode("docs");
        dir.add(new FileNode("a.txt", 1));

        dir.accept(visitor);

        assertEquals(List.of("dir:docs"), visitor.calls());
    }
}