import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        ArrayList<String> graph = (ArrayList<String>) Files.readAllLines(Path.of("test.txt"));
        DijkstraSP sp = new DijkstraSP(graph);
        sp.printPath();
    }
}

class DijkstraSP{
    HashSet<Traverser> visited;
    static ArrayList<String> graph;

    static int width;
    static int height;
    static PriorityQueue<Traverser> queue;
    static class Traverser implements Comparable<Traverser>{
        public final int index;

        private final int accumulatedHeatLoss;

        private final Direction dir;
        private final int numberRepeats;

        private final int steps;

        Traverser prev;

        public Traverser(int index, int heat, int repeats, Direction dir, Traverser prev, int steps){
            this.index = index;
            this.accumulatedHeatLoss = heat;
            this.numberRepeats = repeats;
            this.dir = dir;
            this.prev = prev;
            this.steps = steps;
        }

        private boolean hasTraversed(int check){
            Traverser x = this.prev;
            while (x!=null){
                if (x.index == check){
                    return true;
                }
                x = x.prev;
            }
            return false;
        }

        public void relax() {
            Direction previousDir = this.dir;
            // get below
            if (previousDir != Direction.UP) {
                int[] coords = translate1Dto2D(index);
                if (coords[0] + 1 < height && (this.dir!=Direction.DOWN || this.numberRepeats<2)){
                    int belowIndex = translate2Dto1D(coords[0] + 1, coords[1]);
                    if (!hasTraversed(belowIndex)){
                        int heat = getHeat(belowIndex) + this.accumulatedHeatLoss;
                        int numRepeats = (previousDir == Direction.DOWN) ? this.numberRepeats + 1 : 0;
                        Traverser newT = new Traverser(belowIndex, heat, numRepeats, Direction.DOWN, this, steps+1);
                        queue.add(newT);
                    }
                }
            }
            // get right
            if (previousDir != Direction.LEFT) {
                int[] coords = translate1Dto2D(index);
                if (coords[1] + 1 < width && (this.dir!=Direction.RIGHT || this.numberRepeats<2)){
                    int rightIndex = translate2Dto1D(coords[0], coords[1]+1);
                    if (!hasTraversed(rightIndex)){
                        int heat = getHeat(rightIndex) + this.accumulatedHeatLoss;
                        int numRepeats = (previousDir == Direction.RIGHT) ? this.numberRepeats + 1 : 0;
                        Traverser newT = new Traverser(rightIndex, heat, numRepeats, Direction.RIGHT, this, steps+1);
                        queue.add(newT);
                    }
                }
            }
            // get LEFT
            if (previousDir != Direction.RIGHT) {
                int[] coords = translate1Dto2D(index);
                if (coords[1] - 1 >= 0 && (this.dir!=Direction.LEFT || this.numberRepeats<2)){
                    int leftIndex = translate2Dto1D(coords[0], coords[1]-1);
                    if (!hasTraversed(leftIndex)){
                        int heat = getHeat(leftIndex) + this.accumulatedHeatLoss;
                        int numRepeats = (previousDir == Direction.LEFT) ? this.numberRepeats + 1 : 0;
                        Traverser newT = new Traverser(leftIndex, heat, numRepeats, Direction.LEFT, this, steps+1);
                        queue.add(newT);
                    }
                }
            }
            // get above
            if (previousDir != Direction.DOWN) {
                int[] coords = translate1Dto2D(index);
                if (coords[0] - 1 >= 0 && (this.dir!=Direction.UP || this.numberRepeats<2)){
                    int aboveIndex = translate2Dto1D(coords[0] - 1, coords[1]);
                    if (!hasTraversed(aboveIndex)){
                        int heat = getHeat(aboveIndex) + this.accumulatedHeatLoss;
                        int numRepeats = (previousDir == Direction.UP) ? this.numberRepeats + 1 : 0;
                        Traverser newT = new Traverser(aboveIndex, heat, numRepeats, Direction.UP, this, steps+1);
                        queue.add(newT);
                    }
                }
            }
        }

        @Override
        public int compareTo(Traverser traverser) {
            // prioritise lower heat, closer to goal
            int priority = this.accumulatedHeatLoss - traverser.accumulatedHeatLoss;
            // if going same direction, choose node with lower steps
            if (priority == 0 && this.dir == traverser.dir) {
                priority = this.steps - traverser.steps;
            }
            if (priority == 0) {
                int[] otherCoords = translate1Dto2D(traverser.index);
                int[] coords = translate1Dto2D(this.index);
                priority = otherCoords[0] + otherCoords[1] - coords[0] - coords[1];
            }
            return priority;
        }

        @Override
        public boolean equals(Object o){
            if (o.getClass()!=Traverser.class){
                return false;
            }
            Traverser t = (Traverser) o;
            if (this.index==t.index){
                if(this.dir==t.dir){
                    return this.accumulatedHeatLoss == t.accumulatedHeatLoss;
                }
            }
            return false;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + (this.index + this.dir.hashCode() + this.accumulatedHeatLoss);
            return result;
        }
    }

    private Traverser end;

    public DijkstraSP(ArrayList<String> graph){
        width = graph.get(0).length();
        height = graph.size();
        this.graph = graph;
        int nvertexes = width*height;



        Traverser curr = new Traverser(0, 0, 0, Direction.START, null, 0);
        queue = new PriorityQueue<>();
        queue.add(curr);
        this.visited = new HashSet<>();
        while (!queue.isEmpty()){
            Traverser v = queue.remove();
            if (!visited.contains(v)){
                System.out.println("Best dist for node "+v.index+": "+v.accumulatedHeatLoss);
            } else {
                continue;
            }
            if (v.index==width*height-1){
                this.end = v;
                return;
            }
            visited.add(v);

            // relax the adjacent travelers
            v.relax();
        }
        return;
    }

    private static int getHeat(int index){
        int[] coords = translate1Dto2D(index);
        return Character.getNumericValue(graph.get(coords[0]).charAt(coords[1]));
    }

    public void printPath(){
        LinkedList<Integer> path = new LinkedList<>();
        Traverser v = end;
        while (v.index!=0){
            path.add(v.index);
            v = v.prev;
        }

        for (int i = 0; i < height; i++){
            for (int x = 0; x < width; x++){
                int index = translate2Dto1D(i, x);
                if (path.contains(index)){
                    System.out.print('#');
                } else {
                    System.out.print(graph.get(i).charAt(x));
                }
            }
            System.out.println();
        }
        System.out.println("Heat loss: " + end.accumulatedHeatLoss);
    }


    private static int[] translate1Dto2D(int index){
        int[] coords = new int[2];
        coords[0] = index/width;
        coords[1] = index%width;
        return coords;
    }

    private static int translate2Dto1D(int x, int y){
        return width*x + y;
    }

    enum Direction{
        UP,
        DOWN,
        LEFT,
        RIGHT,
        START;
    }
}

