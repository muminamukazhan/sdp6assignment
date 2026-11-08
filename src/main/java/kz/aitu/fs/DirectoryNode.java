package kz.aitu.fs;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public final class DirectoryNode extends Node {

    private final Map<String, Node> children = new TreeMap<>();

    public DirectoryNode(String name) {
        super(name);
    }

    public void add(Node child) {
        requireCanAdd(child);
        children.put(child.name(), child);
        child.attachTo(this);
    }

    public boolean remove(String childName) {
        Node removed = children.remove(childName);
        if (removed == null) {
            return false;
        }
        removed.detach();
        return true;
    }

    public Collection<Node> children() {
        return Collections.unmodifiableCollection(children.values());
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    private void requireCanAdd(Node child) {
        if (child == null) {
            throw new IllegalArgumentException("Child must not be null");
        }
        if (child.hasParent()) {
            throw new IllegalArgumentException("Node already has a parent: " + child.name());
        }
        if (createsCycle(child)) {
            throw new IllegalArgumentException("Adding would create a cycle: " + child.name());
        }
        if (children.containsKey(child.name())) {
            throw new IllegalArgumentException("Duplicate sibling name: " + child.name());
        }
    }

    private boolean createsCycle(Node child) {
        for (DirectoryNode current = this; current != null; current = current.parent()) {
            if (current == child) {
                return true;
            }
        }
        return false;
    }
}