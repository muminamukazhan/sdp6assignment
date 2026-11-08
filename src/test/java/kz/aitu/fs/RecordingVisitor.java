package kz.aitu.fs;

import java.util.ArrayList;
import java.util.List;

final class RecordingVisitor implements Visitor {

    private final List<String> calls = new ArrayList<>();

    @Override
    public void visit(FileNode file) {
        calls.add("file:" + file.name());
    }

    @Override
    public void visit(DirectoryNode directory) {
        calls.add("dir:" + directory.name());
    }

    List<String> calls() {
        return calls;
    }
}