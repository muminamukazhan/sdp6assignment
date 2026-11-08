package kz.aitu.fs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public final class SearchVisitor extends TraversingVisitor {

    private final Predicate<String> nameCriterion;
    private final long minSize;
    private final List<String> matches = new ArrayList<>();

    public SearchVisitor(Predicate<String> nameCriterion, long minSize) {
        if (nameCriterion == null) {
            throw new IllegalArgumentException("Name criterion must not be null");
        }
        if (minSize < 0) {
            throw new IllegalArgumentException("Minimum size must not be negative: " + minSize);
        }
        this.nameCriterion = nameCriterion;
        this.minSize = minSize;
    }

    public List<String> search(Element root) {
        run(root);
        List<String> result = new ArrayList<>(matches);
        Collections.sort(result);
        return result;
    }

    @Override
    protected void reset() {
        matches.clear();
    }

    @Override
    protected void onFile(FileNode file, int depth) {
        if (nameCriterion.test(file.name()) && file.size() >= minSize) {
            matches.add(pathOf(file));
        }
    }
}