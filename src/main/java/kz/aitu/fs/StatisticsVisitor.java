package kz.aitu.fs;

public final class StatisticsVisitor extends TraversingVisitor {

    private int fileCount;
    private int directoryCount;
    private int maxDepth;

    public Statistics collect(Element root) {
        run(root);
        return new Statistics(fileCount, directoryCount, maxDepth);
    }

    @Override
    protected void reset() {
        fileCount = 0;
        directoryCount = 0;
        maxDepth = 0;
    }

    @Override
    protected void onFile(FileNode file, int depth) {
        fileCount++;
        maxDepth = Math.max(maxDepth, depth);
    }

    @Override
    protected void onDirectory(DirectoryNode directory, int depth) {
        directoryCount++;
        maxDepth = Math.max(maxDepth, depth);
    }
}