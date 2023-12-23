import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DijkstraSPTest {
    @Test
    void testTraversalGoesDownWhenForced(){
        ArrayList<String> graph = new ArrayList<String>();
        graph.add("01234");
        graph.add("01234");
        DijkstraSP sp = new DijkstraSP(graph);
        DijkstraSP.Traverser first = new DijkstraSP.Traverser(0, 0, 0, DijkstraSP.Direction.START, null);
        DijkstraSP.Traverser second = new DijkstraSP.Traverser(1, 0, 0, DijkstraSP.Direction.RIGHT, first);
        DijkstraSP.Traverser third = new DijkstraSP.Traverser(2, 0, 1, DijkstraSP.Direction.RIGHT, second);
        DijkstraSP.Traverser fourth = new DijkstraSP.Traverser(3, 0, 2, DijkstraSP.Direction.RIGHT, third);

        fourth.relax();
        ArrayList<Integer> validIndexes = new ArrayList<>();
        validIndexes.add(8);
        assertTrue(DijkstraSP.queue.size() == validIndexes.size());
        for (DijkstraSP.Traverser x : DijkstraSP.queue){
            validIndexes.remove((Integer) x.index);
        }
        assertTrue(validIndexes.isEmpty());
    }

    @Test
    void testTraversalGoesUpWhenForced(){
        ArrayList<String> graph = new ArrayList<String>();
        graph.add("01234");
        graph.add("01234");
        DijkstraSP sp = new DijkstraSP(graph);
        DijkstraSP.Traverser first = new DijkstraSP.Traverser(0, 0, 0, DijkstraSP.Direction.START, null);
        DijkstraSP.Traverser second = new DijkstraSP.Traverser(5, 0, 0, DijkstraSP.Direction.DOWN, first);
        DijkstraSP.Traverser third = new DijkstraSP.Traverser(6, 0, 0, DijkstraSP.Direction.RIGHT, second);
        DijkstraSP.Traverser fourth = new DijkstraSP.Traverser(7, 0, 1, DijkstraSP.Direction.RIGHT, third);
        DijkstraSP.Traverser fifth = new DijkstraSP.Traverser(8, 0, 2, DijkstraSP.Direction.RIGHT, fourth);

        fifth.relax();
        ArrayList<Integer> validIndexes = new ArrayList<>();
        validIndexes.add(3);
        for (DijkstraSP.Traverser x : DijkstraSP.queue){
            validIndexes.remove((Integer) x.index);
        }
        assertTrue(validIndexes.isEmpty());
    }

    @Test
    void testTraversalGoesLeftWhenForced(){
        ArrayList<String> graph = new ArrayList<String>();
        graph.add("01234");
        graph.add("01234");
        graph.add("01234");
        graph.add("01234");
        graph.add("01234");
        DijkstraSP sp = new DijkstraSP(graph);
        DijkstraSP.Traverser first = new DijkstraSP.Traverser(4, 0, 0, DijkstraSP.Direction.START, null);
        DijkstraSP.Traverser second = new DijkstraSP.Traverser(9, 0, 0, DijkstraSP.Direction.DOWN, first);
        DijkstraSP.Traverser third = new DijkstraSP.Traverser(9+5, 0, 1, DijkstraSP.Direction.DOWN, second);
        DijkstraSP.Traverser fourth = new DijkstraSP.Traverser(9+5+5, 0, 2, DijkstraSP.Direction.DOWN, third);

        fourth.relax();
        ArrayList<Integer> validIndexes = new ArrayList<>();
        validIndexes.add(18);
        for (DijkstraSP.Traverser x : DijkstraSP.queue){
            validIndexes.remove((Integer) x.index);
        }
        assertTrue(validIndexes.isEmpty());
    }
    @Test
    void testTraversalGoesRightWhenForced(){
        ArrayList<String> graph = new ArrayList<String>();
        graph.add("01234");
        graph.add("01234");
        graph.add("01234");
        graph.add("01234");
        graph.add("01234");
        DijkstraSP sp = new DijkstraSP(graph);
        DijkstraSP.Traverser first = new DijkstraSP.Traverser(0, 0, 0, DijkstraSP.Direction.START, null);
        DijkstraSP.Traverser second = new DijkstraSP.Traverser(5, 0, 0, DijkstraSP.Direction.DOWN, first);
        DijkstraSP.Traverser third = new DijkstraSP.Traverser(10, 0, 1, DijkstraSP.Direction.DOWN, second);
        DijkstraSP.Traverser fourth = new DijkstraSP.Traverser(15, 0, 2, DijkstraSP.Direction.DOWN, third);

        fourth.relax();
        ArrayList<Integer> validIndexes = new ArrayList<>();
        validIndexes.add(16);
        for (DijkstraSP.Traverser x : DijkstraSP.queue){
            validIndexes.remove((Integer) x.index);
        }
        assertTrue(validIndexes.isEmpty());
    }

    @Test
    void testTraversalEveryOptionButReverse(){
        ArrayList<String> graph = new ArrayList<String>();
        graph.add("01234");
        graph.add("01234");
        graph.add("01234");
        graph.add("01234");
        graph.add("01234");
        DijkstraSP sp = new DijkstraSP(graph);
        DijkstraSP.Traverser first = new DijkstraSP.Traverser(0, 0, 0, DijkstraSP.Direction.START, null);
        DijkstraSP.Traverser second = new DijkstraSP.Traverser(5, 0, 0, DijkstraSP.Direction.DOWN, first);
        DijkstraSP.Traverser third = new DijkstraSP.Traverser(6, 0, 0, DijkstraSP.Direction.RIGHT, second);

        third.relax();
        ArrayList<Integer> validIndexes = new ArrayList<>();
        validIndexes.add(1);
        validIndexes.add(7);
        validIndexes.add(11);
        for (DijkstraSP.Traverser x : DijkstraSP.queue){
            validIndexes.remove((Integer) x.index);
        }
        assertTrue(validIndexes.isEmpty());
    }
    @Test
    void testTraversalSkipsPreviouslyTraversed(){
        ArrayList<String> graph = new ArrayList<String>();
        graph.add("01234");
        graph.add("01234");
        graph.add("01234");
        graph.add("01234");
        graph.add("01234");
        DijkstraSP sp = new DijkstraSP(graph);
        DijkstraSP.Traverser first = new DijkstraSP.Traverser(1, 0, 0, DijkstraSP.Direction.START, null);
        DijkstraSP.Traverser second = new DijkstraSP.Traverser(0, 0, 0, DijkstraSP.Direction.LEFT, first);
        DijkstraSP.Traverser third = new DijkstraSP.Traverser(5, 0, 0, DijkstraSP.Direction.DOWN, second);
        DijkstraSP.Traverser fourth = new DijkstraSP.Traverser(6, 0, 0, DijkstraSP.Direction.RIGHT, third);

        fourth.relax();
        ArrayList<Integer> validIndexes = new ArrayList<>();
        validIndexes.add(7);
        validIndexes.add(11);
        for (DijkstraSP.Traverser x : DijkstraSP.queue){
            validIndexes.remove((Integer) x.index);
        }
        assertTrue(validIndexes.isEmpty());
    }
    @Test
    void testTraversalUpwardOOB(){
        ArrayList<String> graph = new ArrayList<String>();
        graph.add("01234");
        graph.add("01234");
        graph.add("01234");
        graph.add("01234");
        graph.add("01234");
        DijkstraSP sp = new DijkstraSP(graph);
        DijkstraSP.Traverser first = new DijkstraSP.Traverser(1, 0, 0, DijkstraSP.Direction.START, null);

        first.relax();
        ArrayList<Integer> validIndexes = new ArrayList<>();
        validIndexes.add(0);
        validIndexes.add(2);
        validIndexes.add(6);
        for (DijkstraSP.Traverser x : DijkstraSP.queue){
            validIndexes.remove((Integer) x.index);
        }
        assertTrue(validIndexes.isEmpty());
    }
    @Test
    void testTraversalDownwardOOB(){
        ArrayList<String> graph = new ArrayList<String>();
        graph.add("01234");
        graph.add("01234");
        graph.add("01234");
        graph.add("01234");
        graph.add("01234");
        DijkstraSP sp = new DijkstraSP(graph);
        DijkstraSP.Traverser first = new DijkstraSP.Traverser(21, 0, 0, DijkstraSP.Direction.START, null);

        first.relax();
        ArrayList<Integer> validIndexes = new ArrayList<>();
        validIndexes.add(20);
        validIndexes.add(22);
        validIndexes.add(21-5);
        for (DijkstraSP.Traverser x : DijkstraSP.queue){
            validIndexes.remove((Integer) x.index);
        }
        assertTrue(validIndexes.isEmpty());
    }
    @Test
    void testTraversalLeftwardOOB(){
        ArrayList<String> graph = new ArrayList<String>();
        graph.add("01234");
        graph.add("01234");
        graph.add("01234");
        graph.add("01234");
        graph.add("01234");
        DijkstraSP sp = new DijkstraSP(graph);
        DijkstraSP.Traverser first = new DijkstraSP.Traverser(10, 0, 0, DijkstraSP.Direction.START, null);

        first.relax();
        ArrayList<Integer> validIndexes = new ArrayList<>();
        validIndexes.add(5);
        validIndexes.add(11);
        validIndexes.add(15);
        for (DijkstraSP.Traverser x : DijkstraSP.queue){
            validIndexes.remove((Integer) x.index);
        }
        assertTrue(validIndexes.isEmpty());
    }
    @Test
    void testTraversalRightwardOOB(){
        ArrayList<String> graph = new ArrayList<String>();
        graph.add("01234");
        graph.add("01234");
        graph.add("01234");
        graph.add("01234");
        graph.add("01234");
        DijkstraSP sp = new DijkstraSP(graph);
        DijkstraSP.Traverser first = new DijkstraSP.Traverser(14, 0, 0, DijkstraSP.Direction.START, null);

        first.relax();
        ArrayList<Integer> validIndexes = new ArrayList<>();
        validIndexes.add(13);
        validIndexes.add(9);
        validIndexes.add(19);
        for (DijkstraSP.Traverser x : DijkstraSP.queue){
            validIndexes.remove((Integer) x.index);
        }
        assertTrue(validIndexes.isEmpty());
    }
    @Test
    void testTopRightCornerCase(){
        ArrayList<String> graph = new ArrayList<String>();
        graph.add("01234");
        graph.add("01234");
        graph.add("01234");
        graph.add("01234");
        graph.add("01234");
        DijkstraSP sp = new DijkstraSP(graph);
        DijkstraSP.Traverser first = new DijkstraSP.Traverser(0, 0, 0, DijkstraSP.Direction.START, null);

        first.relax();
        ArrayList<Integer> validIndexes = new ArrayList<>();
        validIndexes.add(1);
        validIndexes.add(5);
        for (DijkstraSP.Traverser x : DijkstraSP.queue){
            validIndexes.remove((Integer) x.index);
        }
        assertTrue(validIndexes.isEmpty());
    }

}