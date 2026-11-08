package kz.aitu.fs;

public final class Main {

    public static void main(String[] args) {
        DirectoryNode root = buildSampleTree();

        TotalSizeVisitor sizeVisitor = new TotalSizeVisitor();
        System.out.println("Total size: " + sizeVisitor.calculate(root) + " bytes");

        SearchVisitor textSearch = new SearchVisitor(name -> name.endsWith(".txt"), 100);
        System.out.println("Search (*.txt, size >= 100): " + textSearch.search(root));

        SearchVisitor javaSearch = new SearchVisitor(name -> name.endsWith(".java"), 500);
        System.out.println("Search (*.java, size >= 500): " + javaSearch.search(root));

        Statistics stats = new StatisticsVisitor().collect(root);
        System.out.println("Statistics: files=" + stats.fileCount()
                + ", directories=" + stats.directoryCount()
                + ", maxDepth=" + stats.maxDepth());

        System.out.println("Repeated total size: " + sizeVisitor.calculate(root) + " bytes");
    }

    private static DirectoryNode buildSampleTree() {
        DirectoryNode root = new DirectoryNode("root");
        root.add(new FileNode("readme.txt", 100));

        DirectoryNode docs = new DirectoryNode("docs");
        docs.add(new FileNode("report.pdf", 2048));
        docs.add(new FileNode("notes.txt", 120));
        DirectoryNode archive = new DirectoryNode("archive");
        archive.add(new FileNode("old.txt", 500));
        docs.add(archive);
        root.add(docs);

        DirectoryNode src = new DirectoryNode("src");
        src.add(new FileNode("Main.java", 800));
        DirectoryNode util = new DirectoryNode("util");
        util.add(new FileNode("Helper.java", 300));
        src.add(util);
        root.add(src);

        root.add(new DirectoryNode("empty"));
        return root;
    }
}