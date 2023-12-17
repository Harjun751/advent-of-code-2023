import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Part2 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("input.txt"));
        long total = 0;
        while (sc.hasNext()){
            String line = sc.nextLine();

            String line2 = unfoldLine(line);

            ArrayList<Integer> groups = new ArrayList<>();
            for (String num : line.split(" ")[1].split(",")){
                groups.add(Integer.parseInt(num));
            }
            ArrayList<Integer> groups2 = new ArrayList<>();
            for (String num : line2.split(" ")[1].split(",")){
                groups2.add(Integer.parseInt(num));
            }

            Line l = new Line(line.split(" ")[0], groups);
            long t1 = l.permutations(l.line, l.groups);
            long t2 = l.permutations(line2.split(" ")[0], groups2);
            long factor = t2/t1;
            long subtotal = t2;
            for (int i = 0; i <3; i++){
                subtotal = subtotal*factor;
            }
            total += subtotal;
        }
        System.out.println(total);
    }

    public static String unfoldLine(String line){
        String first = line.split(" ")[0];
        String groups = line.split(" ")[1];
        first = first+"?"+first;//+"?"+first+"?"+first+"?"+first;
        groups = groups+","+groups;//+","+groups+","+groups+","+groups;
        return first+" "+groups;
    }

    public static class Line{
        private static Cache cache = new Cache();

        private String line;
        private ArrayList<Integer> groups;

        private static int count = 0;


        public Line(String line, ArrayList<Integer> groups) {
            this.line = line;
            this.groups = groups;
        }

//        YOURE NOT LEARNING. YOU CAN'T SET AN X INTEGER BECAUSE IT SCREWS UP WITH THE DAMN PATTERN MATCHING.

        public long permutations(String line, ArrayList<Integer> groups){
            int perms = 0;
            if (cache.contains(line, groups)){
                System.out.println("cache hit: " + count++);
                return cache.get(line, groups);
            }
            if (line.equals("")){
                if (groups.isEmpty()){
                    return 1;
                }
                return 0;
            }

            char first = line.charAt(0);
            if (first=='.'){
                perms += permutations(line.substring(1), groups);
            } else if (first=='?'){
                String damaged = '#' + line.substring(1);
                String working = '.' + line.substring(1);
                perms += permutations(damaged, new ArrayList<>(groups));
                perms += permutations(working, new ArrayList<>(groups));
            } else {
                if (groups.size()==0){
                    return 0;
                }
                // it's a #
                int damagedRequired = groups.get(0);
                if (damagedRequired > line.length()){
                    return 0;
                }
                boolean allValid = true;
                for (int i = 0; i < damagedRequired; i++){
                    if (line.charAt(i) == '.'){
                        allValid = false;
                        break;
                    }
                }
                if (!allValid){
                    return 0;
                } else {
                    ArrayList<Integer> grps = new ArrayList<>(groups);
                    grps.remove(0);
                    if (damagedRequired == line.length()){
                        if (grps.size() == 0){
                            return 1;
                        }
                        return 0;
                    } else if (line.charAt(damagedRequired) == '.'){
                        perms += permutations(line.substring(damagedRequired + 1), grps);
                    } else if (line.charAt(damagedRequired) == '?'){
                        // set the next ? to a .
                        perms += permutations('.' + line.substring(damagedRequired + 1), grps);
                    }
                }
            }
            cache.put(line, groups, perms);
            return perms;
        }
    }



    public static class Cache{
        private static HashMap<String, HashMap<ArrayList<Integer>, Integer>> store = new HashMap<>();

        public boolean contains(String line, ArrayList<Integer> required){
            return store.containsKey(line) && store.get(line).containsKey(required);
        }

        public Integer get(String line, ArrayList<Integer> required){
            return store.get(line).get(required);
        }

        public void put(String line, ArrayList<Integer> required, Integer combinations){
            if (store.containsKey(line)){
                HashMap<ArrayList<Integer>, Integer> map = store.get(line);
                map.put(required,combinations);
            } else {
                HashMap<ArrayList<Integer>, Integer> map = new HashMap<>();
                map.put(required, combinations);
                store.put(line, map);
            }
        }
    }
}

