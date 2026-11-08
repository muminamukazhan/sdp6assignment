package kz.aitu.fs;

final class TestTrees {

    private TestTrees() {
    }

    static DirectoryNode sample() {
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