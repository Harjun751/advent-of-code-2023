import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        File f = new File("input.txt");
        Scanner sc = new Scanner(f);
        // obtain the full pipe map
        ArrayList<String> map = new ArrayList<>();
        while (sc.hasNext()){
            String line = sc.nextLine();
            map.add(line);
        }

        PipeNetwork p = new PipeNetwork(map);
        ArrayList<PipeNetwork.Pipe> loop = p.find_loop();
        System.out.println(loop.size());

        // prints out the map with the borders highlighted
        for (int i = 0; i < map.size(); i++){
            for (int y = 0; y < map.get(0).length(); y++){
                boolean isInPath = false;
                for (PipeNetwork.Pipe pipe : loop){
                    if (pipe.x==i && pipe.y==y){
                        isInPath = true;
                        break;
                    }
                }
                if (isInPath){
                    System.out.print("\u001B[31m"+map.get(i).charAt(y));
                } else {
                    System.out.print("\u001B[38m"+map.get(i).charAt(y));
                }
            }
            System.out.print("\n");
        }


        // part 2
        System.out.println(p.enclosed());
    }
}

class PipeNetwork{
    private final HashMap<Integer, Pipe> pipes = new HashMap<>();
    private final ArrayList<String> map;

    private final int width;
    private final int height;
    private Pipe start;

    private final HashSet<Character> validAbove;
    private final HashSet<Character> validBelow;
    private final HashSet<Character> validLeft;
    private final HashSet<Character> validRight;

    public PipeNetwork(ArrayList<String> map){
        this.map = map;
        this.width = map.get(0).length();
        this.height = map.size();

        // these define the sets of valid pipes that go above, below, to the right, or to the left of a pipe
        // e.g. a pipe going right to top cannot be put on the right of a tile, as there would be no connection
        validAbove = new HashSet<>();
        validAbove.add('|');
        validAbove.add('7');
        validAbove.add('F');

        validBelow = new HashSet<>();
        validBelow.add('|');
        validBelow.add('L');
        validBelow.add('J');

        validLeft = new HashSet<>();
        validLeft.add('-');
        validLeft.add('F');
        validLeft.add('L');

        validRight = new HashSet<>();
        validRight.add('-');
        validRight.add('7');
        validRight.add('J');

        // create the network of pipes
        for (int i = 0; i < map.size(); i++){
            for (int y = 0; y < map.get(i).length(); y++){
                char nodeString = map.get(i).charAt(y);
                if (nodeString=='S'){
                    // the starting node can connect to all pipes around it
                    Pipe current = getAt(i,y);
                    Pipe above = getAbove(i, y);
                    Pipe below = getBelow(i, y);
                    Pipe left = getLeft(i, y);
                    Pipe right = getRight(i, y);

                    for (Pipe p : new Pipe[] {above, below, left, right}){
                        if (p!=null){
                            // add the surrounding pipes to connected
                            current.connected.add(p);
                        }
                    }
                    start = current;
                } else if (nodeString=='|'){
                    // the bar can only connect below to top
                    Pipe current = getAt(i,y);
                    Pipe above = getAbove(i, y);
                    Pipe below = getBelow(i, y);
                    for (Pipe p : new Pipe[] {above, below}){
                        if (p!=null){
                            current.connected.add(p);
                        }
                    }
                } else if (nodeString=='-'){
                    // the dash connects left to right
                    Pipe current = getAt(i,y);
                    Pipe left = getLeft(i, y);
                    Pipe right = getRight(i, y);
                    for (Pipe p : new Pipe[] {left, right}){
                        if (p!=null){
                            current.connected.add(p);
                        }
                    }
                } else if (nodeString=='L'){
                    // the L connects top to right
                    Pipe current = getAt(i,y);
                    Pipe above = getAbove(i, y);
                    Pipe right = getRight(i, y);
                    for (Pipe p : new Pipe[] {above, right}){
                        if (p!=null){
                            current.connected.add(p);
                        }
                    }
                } else if (nodeString=='J'){
                    // the J connects top to left
                    Pipe current = getAt(i,y);
                    Pipe above = getAbove(i, y);
                    Pipe left = getLeft(i, y);
                    for (Pipe p : new Pipe[] {above, left}){
                        if (p!=null){
                            current.connected.add(p);
                        }
                    }
                } else if (nodeString=='7'){
                    // the 7 connects below to left
                    Pipe current = getAt(i,y);
                    Pipe below = getBelow(i, y);
                    Pipe left = getLeft(i, y);
                    for (Pipe p : new Pipe[] {below, left}){
                        if (p!=null){
                            current.connected.add(p);
                        }
                    }
                } else if (nodeString=='F'){
                    // the 7 connects right to below
                    Pipe current = getAt(i,y);
                    Pipe below = getBelow(i, y);
                    Pipe right = getRight(i, y);
                    for (Pipe p : new Pipe[] {below, right}){
                        if (p!=null){
                            current.connected.add(p);
                        }
                    }
                }
            }
        }
    }

    private Pipe getAt(int x, int y){
        // gets the pipe at a given coordinate
        // if the pipe does not exist, it creates it.
        int index = (x) * width + y;
        Pipe p = pipes.get(index);
        if (p==null){
            p = new Pipe(x, y);
            pipes.put(index, p);
        }
        return  p;
    }
    private Pipe getAbove(int x, int y){
        if (x==0){
            return null;
        }

        // validate that it is a valid above element
        char above = map.get(x-1).charAt(y);
        if (!validAbove.contains(above)){
            return null;
        }

        int index = (x - 1) * width + y;
        Pipe p = pipes.get(index);
        if (p==null){
            p = new Pipe(x-1, y);
            pipes.put(index, p);
        }
        return  p;
    }
    private Pipe getBelow(int x, int y){
        if (x==height-1){
            return null;
        }

        char below = map.get(x+1).charAt(y);
        if (!validBelow.contains(below)){
            return null;
        }

        int index = (x + 1) * width + y;
        Pipe p = pipes.get(index);
        if (p==null){
            p = new Pipe(x+1, y);
            pipes.put(index, p);
        }
        return  p;
    }
    private Pipe getLeft(int x, int y){
        if (y==0){
            return null;
        }

        char left = map.get(x).charAt(y-1);
        if (!validLeft.contains(left)){
            return null;
        }

        int index = x * width + (y - 1);
        Pipe p = pipes.get(index);
        if (p==null){
            p = new Pipe(x, y-1);
            pipes.put(index, p);
        }
        return  p;
    }
    private Pipe getRight(int x, int y){
        if (y==width-1){
            return null;
        }

        char right = map.get(x).charAt(y+1);
        if (!validRight.contains(right)){
            return null;
        }

        int index = x * width + (y + 1);
        Pipe p = pipes.get(index);
        if (p==null){
            p = new Pipe(x, y+1);
            pipes.put(index, p);
        }
        return  p;
    }

    public ArrayList<Pipe> find_loop(){
        // finds the loop based on DFS
        return DFS(start, new ArrayList<>());
    }

    public ArrayList<Pipe> DFS(Pipe p, ArrayList<Pipe> path){
        for (Pipe neighbour : p.connected){
            if (neighbour==start){
                return path;
            }
            // prevent backtracking
            if (!path.contains(neighbour)){
                path.add(neighbour);
                // recursively search the neighbours
                path = DFS(neighbour, path);
            }
        }
        return path;
    }

    public int enclosed(){
        // goes through every single item and checks if it's enclosed in the loop
        ArrayList<Pipe> path = DFS(start, new ArrayList<>());
        path.add(start);

        int total = 0;

        for (int i = 0; i < height; i++){
            for (int y = 0; y < width; y++){
                if (map.get(i).charAt(y)=='.'){
                    // if it's a . we can immediately check if it's enclosed
                    if (isEnclosed(i, y, path)){
                        total+=1;
                    }
                } else {
                    // if it's any other pipe element,
                    // we need to check if the element is present in the loop.
                    // if it's not, it may or may not be enclosed by the loop.
                    boolean isInPath = false;
                    for (Pipe p : path){
                        if (p.x==i && p.y==y){
                            isInPath = true;
                            break;
                        }
                    }
                    if (!isInPath){
                        if (isEnclosed(i, y, path)){
                            total+=1;
                        }
                    }
                }
            }
        }
        return total;
    }

    public boolean isEnclosed(int x, int y, ArrayList<Pipe> pipes){
        // Get the pipes in the current row of search.
        ArrayList<Pipe> pipesInRow = new ArrayList<>();
        for (Pipe p : pipes){
            if (p.x == x){
                pipesInRow.add(p);
            }
        }
        Collections.sort(pipesInRow);

        // Checks if the current element is completely out of the loop - the element's y value
        // is lesser than the LEFTMOST pipe in the row.
        if (pipesInRow.size()!=0){
            if (pipesInRow.get(pipesInRow.size()-1).y > y){
                return false;
            }
        }

        // we only need to search the string after the current y
        String toSearch = map.get(x).substring(y+1);

        // the for loop replaces all pipe elements that are not a part of the loop with a '.'
        // because we can't consider these elements when checking for the even-odd rule
        for (int c = y+1; c < width; c++){
            if (toSearch.charAt(c-y-1)=='.'){
                continue;
            }
            // check if the pipe element is in the loop or not
            boolean isInLoop = false;
            for (Pipe p : pipesInRow){
                if (p.x == x && p.y==c){
                    isInLoop = true;
                    break;
                }
            }
            if (!isInLoop){
                // replace the element at the index with a '.'
                toSearch = toSearch.substring(0, c-y-1) + '.' + toSearch.substring(c -y);
            }
        }

        // now, check for the amount of borders crossed using Regex
        int count = 0;
        Pattern border = Pattern.compile("(F-*J)|(L-*7)|(\\|)|(F-*S)");
        Matcher borderSearch = border.matcher(toSearch);
        while(borderSearch.find()){
            count+=1;
        }

        // return true if odd -> odd means that it is enclosed in the loop.
        return count % 2 == 1;
    }



    static class Pipe implements Comparable<Pipe>{
        ArrayList<Pipe> connected;
        int x;
        int y;

        public Pipe(int x, int y){
            this.connected = new ArrayList<>();
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Pipe o) {
            if (this.x < o.x){
                return 1;
            } else if (this.x > o.x){
                return -1;
            } else {
                return Integer.compare(o.y, this.y);
            }
        }
    }
}