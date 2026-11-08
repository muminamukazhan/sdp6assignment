package kz.aitu.fs;

public final class TotalSizeVisitor extends TraversingVisitor {

    private long total;

    public long calculate(Element root) {
        run(root);
        return total;
    }

    @Override
    protected void reset() {
        total = 0;
    }

    @Override
    protected void onFile(FileNode file, int depth) {
        total += file.size();
    }
}