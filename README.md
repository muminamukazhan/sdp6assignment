# Visitor Pattern: Advanced File System

Author:Mumina Mukazhan

Group:SE-2530

Assignment 06

An in-memory hierarchical file system with three independent operations implemented through the Visitor pattern: total size, filtered search and tree statistics.

## Requirements

- JDK 17
- Maven 3.8 or newer

## How to run

Run all commands from the project root (the folder that contains `pom.xml`).

Run the tests:

```
mvn test
```

Run the demonstration:

```
mvn -q compile exec:java
```

Expected output of the demonstration (deterministic):

```
Total size: 3868 bytes
Search (*.txt, size >= 100): [docs/archive/old.txt, docs/notes.txt, readme.txt]
Search (*.java, size >= 500): [src/Main.java]
Statistics: files=6, directories=6, maxDepth=3
Repeated total size: 3868 bytes
```

## Project structure

```
src/main/java/kz/aitu/fs/
    Element.java            common interface: name(), accept(Visitor)
    Visitor.java            visit(FileNode), visit(DirectoryNode)
    Node.java               abstract base: name and parent link
    FileNode.java           file with a non-negative byte size
    DirectoryNode.java      directory with named children, protects tree invariants
    TraversingVisitor.java  abstract base visitor that owns the traversal
    TotalSizeVisitor.java   sum of file sizes of a subtree
    SearchVisitor.java      name criterion + inclusive minimum size, root-relative sorted paths
    StatisticsVisitor.java  file count, directory count, maximum depth
    Statistics.java         result record of StatisticsVisitor
    Main.java               demonstration with own data
src/test/java/kz/aitu/fs/   JUnit 5 tests (36 tests in 6 classes)
```

## Design notes

**Double dispatch.** `element.accept(visitor)` is resolved by the runtime type of the element. Inside `FileNode.accept` and `DirectoryNode.accept` the call `visitor.visit(this)` is resolved by the static type of `this`, which selects the matching `visit` overload. There is no `instanceof`, `getClass()`, reflection or casting to choose behavior.

**Traversal owner.** Traversal belongs to the visitors, through `TraversingVisitor`. Its `visit(DirectoryNode)` calls the `onDirectory` hook and then `accept` on every child, so each reachable node is visited exactly once per run. `accept` only delegates and never walks children. Concrete visitors override only `onFile` and `onDirectory`.

**Depth.** A counter starts at -1 and is incremented when a directory is entered, so the root directory has depth 0. A file has the depth of its parent directory plus one. A file used as root has depth 0.

**Paths.** A stack holds the names of the entered directories, excluding the root. A file path is the stack plus the file name joined with `/`, so it is relative to the chosen root. The result list is sorted lexicographically by the full path string, not by traversal order (for example `a.txt` comes before `a/x.txt`).

**Visitor state.** Visitors are reusable. Each public operation (`calculate`, `search`, `collect`) resets all internal state before it traverses, so repeated runs give the same result regardless of order. `SearchVisitor.search` returns a copy of its result. Visitors are not thread-safe.

**Tree integrity.** The following are rejected with `IllegalArgumentException` and leave the tree unchanged: negative file size, invalid names (null, blank, containing `/`), null child, duplicate sibling name, a node that already has a parent, and cycles (including adding a directory to itself or to its own descendant). Children are stored in a private map and exposed only as an unmodifiable view.

**Extension boundary.** `StatisticsVisitor` and `Statistics` were added as new files only. `Element`, `Node`, `FileNode`, `DirectoryNode`, `Visitor` and `TraversingVisitor` were not changed. Adding a new operation never touches the element classes. Adding a new element type (for example a symbolic link) requires a new `visit` method in `Visitor` and an implementation in every visitor.

## Tests

| Test class | Tests | Checkpoints |
|---|---|---|
| `DoubleDispatchTest` | 3 | T1 |
| `TotalSizeVisitorTest` | 4 | T2, T6 |
| `SearchVisitorTest` | 8 | T3, T4, T6 |
| `StatisticsVisitorTest` | 4 | T5, T6 |
| `VisitorReuseTest` | 5 | T7 |
| `TreeIntegrityTest` | 12 | T8 |

Result of `mvn test`: 36 tests, 0 failures, 0 errors, `BUILD SUCCESS`.
