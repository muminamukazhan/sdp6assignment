package kz.aitu.fs;

public abstract class Node implements Element {

    private final String name;
    private DirectoryNode parent;

    Node(String name) {
        if (name == null || name.isBlank() || name.contains("/")) {
            throw new IllegalArgumentException("Invalid node name: " + name);
        }
        this.name = name;
    }

    @Override
    public final String name() {
        return name;
    }

    final DirectoryNode parent() {
        return parent;
    }

    final boolean hasParent() {
        return parent != null;
    }

    final void attachTo(DirectoryNode newParent) {
        this.parent = newParent;
    }

    final void detach() {
        this.parent = null;
    }
}