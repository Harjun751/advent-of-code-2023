import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        File f = new File("input.txt");
        Scanner sc = new Scanner(f);
        ArrayList<String> space = new ArrayList<>();
        ArrayList<Integer> emptyRows = new ArrayList<>();
        ArrayList<Integer> emptyCols = new ArrayList<>();
        int row = 0;
        while (sc.hasNext()){
            String line = sc.nextLine();
            if (!line.contains("#")){
                // expand the universe by adding a new row
//                space.add(line);
                emptyRows.add(row);
            }
            space.add(line);
            row ++;
        }


        int i = 0;
        int width = space.get(0).length();
        while (i < width){
            boolean needExpand = true;
            for (String s : space) {
                if (s.charAt(i) == '#') {
                    needExpand = false;
                    break;
                }
            }

            if (needExpand){
//                for (int y = 0; y < space.size(); y++){
//                    String original = space.get(y);
//                    String expanded = original.substring(0, i) + '.' + original.substring(i);
//                    space.set(y, expanded);
//                }
                emptyCols.add(i);
//                i+=1;
//                width+=1;
            }
            i+=1;
        }


        // x : y : galaxy
        Coordinates galaxyMap = new Coordinates();
        for (int x = 0; x < space.size(); x++){
            for (int y = 0; y < width; y++){
                if (space.get(x).charAt(y)=='#'){
                    galaxyMap.putGalaxy(x, y);
                }
            }
        }
        System.out.println(galaxyMap.calculateShortestDistanceWithEmptySpace(emptyRows, emptyCols));
    }
}

class Coordinates{
    private int count = 1;
    public HashMap<Integer, HashMap<Integer, Galaxy>> galaxyCoords;
    public ArrayList<Galaxy> galaxyList;

    public Coordinates(){
        galaxyCoords = new HashMap<>();
        galaxyList = new ArrayList<>();
    }

    public void putGalaxy(int x, int y){
        HashMap<Integer, Galaxy> row = galaxyCoords.computeIfAbsent(x, k -> new HashMap<>());
        Galaxy g = new Galaxy(x, y, count++);
        row.put(y, g);
        galaxyList.add(g);
    }

    public int calculateShortestDistance(){
        int total = 0;
        int i = 0;
        ArrayList<Long> values = new ArrayList<>();
        while (i < galaxyList.size()){
            int start = i+1;
            while (start < galaxyList.size()){
                Galaxy g = galaxyList.get(i);
                Galaxy o = galaxyList.get(start);

                int dist = Math.abs(g.x - o.x) + Math.abs(g.y - o.y);
                total+=dist;
                values.add((long) dist);
                start+=1;
            }
            i++;
        }
        return total;
    }

    public long calculateShortestDistanceWithEmptySpace(ArrayList<Integer> emptyRows, ArrayList<Integer> emptyCols){
        long total = 0;
        int i = 0;
        ArrayList<Long> values = new ArrayList<>();
        while (i < galaxyList.size()){
            int start = i+1;
            while (start < galaxyList.size()){
                Galaxy g = galaxyList.get(i);
                Galaxy o = galaxyList.get(start);

                int cross_empty_row_count = 0;
                for (int e : emptyRows){
                    if (g.x < e && o.x > e){
                        cross_empty_row_count +=1;
                    }
                }
                int cross_empty_col_count = 0;
                for (int e : emptyCols){
                    if (e > g.y && e < o.y || e > o.y && e < g.y){
                        cross_empty_col_count +=1;
                    }
                }
                long xTravelled = Math.abs(g.x - o.x);
                xTravelled += cross_empty_col_count * (1000000L - 1L);
                long yTravelled = Math.abs(g.y - o.y);
                yTravelled += cross_empty_row_count * (1000000L - 1L);
                long dist = xTravelled + yTravelled;
                total += dist;
                values.add(dist);
                start+=1;
            }
            i++;
        }
        return total;
    }

    class Galaxy{
        private int x;
        private int y;
        private int id;

        public Galaxy(int x, int y, int id){
            this.x = x;
            this.y = y;
            this.id = id;
        }
    }
}