package kz.aitu.fs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class VisitorReuseTest {

    @Test
    void totalSizeDoesNotAccumulateAcrossRuns() {
        TotalSizeVisitor visitor = new TotalSizeVisitor();
        DirectoryNode tree = TestTrees.sample();

        assertEquals(3868, visitor.calculate(tree));
        assertEquals(3868, visitor.calculate(tree));
    }

    @Test
    void resultDoesNotDependOnPreviousTree() {
        TotalSizeVisitor visitor = new TotalSizeVisitor();

        visitor.calculate(TestTrees.sample());

        assertEquals(0, visitor.calculate(new DirectoryNode("empty")));
    }

    @Test
    void searchDoesNotDuplicateMatchesAndKeepsEarlierResult() {
        SearchVisitor visitor = new SearchVisitor(name -> name.endsWith(".txt"), 0);
        DirectoryNode tree = TestTrees.sample();

        List<String> first = visitor.search(tree);
        List<String> second = visitor.search(tree);

        assertEquals(3, first.size());
        assertEquals(first, second);
    }

    @Test
    void statisticsDoNotMultiplyAcrossRuns() {
        StatisticsVisitor visitor = new StatisticsVisitor();
        DirectoryNode tree = TestTrees.sample();

        visitor.collect(tree);

        assertEquals(new Statistics(6, 6, 3), visitor.collect(tree));
    }

    @Test
    void statisticsAfterSmallerTreeForgetPreviousDepth() {
        StatisticsVisitor visitor = new StatisticsVisitor();

        visitor.collect(TestTrees.sample());

        assertEquals(new Statistics(0, 1, 0), visitor.collect(new DirectoryNode("empty")));
    }
}