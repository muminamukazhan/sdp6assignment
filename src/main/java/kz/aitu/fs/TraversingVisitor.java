package kz.aitu.fs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public abstract class TraversingVisitor implements Visitor {

    private final Deque<String> pathSegments = new ArrayDeque<>();
    private int currentDepth = -1;

    protected final void run(Element root) {
        pathSegments.clear();
        currentDepth = -1;
        reset();
        root.accept(this);
    }

    @Override
    public final void visit(FileNode file) {
        onFile(file, currentDepth + 1);
    }

    @Override
    public final void visit(DirectoryNode directory) {
        currentDepth++;
        boolean isRoot = currentDepth == 0;
        if (!isRoot) {
            pathSegments.addLast(directory.name());
        }
        onDirectory(directory, currentDepth);
        for (Node child : directory.children()) {
            child.accept(this);
        }
        if (!isRoot) {
            pathSegments.removeLast();
        }
        currentDepth--;
    }

    protected final String pathOf(FileNode file) {
        List<String> parts = new ArrayList<>(pathSegments);
        parts.add(file.name());
        return String.join("/", parts);
    }

    protected abstract void reset();

    protected void onFile(FileNode file, int depth) {
    }

    protected void onDirectory(DirectoryNode directory, int depth) {
    }
}