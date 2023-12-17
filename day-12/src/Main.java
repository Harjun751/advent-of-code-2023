import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        File f = new File("input.txt");
        Scanner sc = new Scanner(f);
        Pattern p = Pattern.compile("([0-9]+)");
        int totalPerms = 0;


        while (sc.hasNext()){
            String line = sc.nextLine();
            ArrayList<Integer> contiguousGroups = new ArrayList<>();
            Matcher cg = p.matcher(line);

            // parse the section containing the contiguous groups
            while (cg.find()){
                contiguousGroups.add(Integer.valueOf(cg.group(0)));
            }

            // parse the "spring" section of the l
            ArrayList<Integer> unknown = new ArrayList<>();
            ArrayList<Integer> destroyed = new ArrayList<>();
            ArrayList<Integer> working = new ArrayList<>();
            for (int i = 0; i < line.length(); i++){
                if (line.charAt(i) == '?'){
                    unknown.add(i);
                } else if (line.charAt(i) == '#'){
                    destroyed.add(i);
                } else {
                    working.add(i);
                }
            }


            ArrayList<String> permutations = getPermutations(line.split(" ")[0], unknown, destroyed, working, contiguousGroups);

            int count = checkCorrectness(permutations, contiguousGroups);
            System.out.println(count);
            totalPerms += count;
        }
        System.out.println("total");
        System.out.println(totalPerms);
    }

    public static ArrayList<String> getPermutations(String line, ArrayList<Integer> unknown, ArrayList<Integer> destroyed, ArrayList<Integer> working, ArrayList<Integer> groups){
        line = line.replace('?', '.');
        int amnt = 0;
        for (int g : groups){
            amnt += g;
        }
        int placeable = amnt - destroyed.size();
        ArrayList<String> permutations = new ArrayList<>();
        if (placeable==0){
            permutations.add(line);
            return permutations;
        }
        Integer[] unknownArr = unknown.toArray(new Integer[unknown.size()]);
        ArrayList<Integer[]> combinations = combinations(unknownArr, placeable, new Integer[placeable], 0);
        for (Integer[] indexes : combinations){
            char[] input = line.toCharArray();
            for (int x : indexes){
                input[x] = '#';
            }
            permutations.add(new String(input));
        }
        return permutations;
    }

    public static ArrayList<Integer[]> combinations(Integer[] array, int k, Integer[] previous, int prevIndex){
        ArrayList<Integer[]> combinations = new ArrayList<>();
        if (prevIndex == k){
            combinations.add(previous);
            return combinations;
        }
        for (int i = 0; i < array.length; i++){
            Integer[] currCombination = previous.clone();
            currCombination[prevIndex] = array[i];
            Integer[] slices = Arrays.copyOfRange(array, i+1, array.length);
            combinations.addAll(combinations(slices, k, currCombination, prevIndex+1));
        }
        return combinations;
    }


    public static ArrayList<int[]> combinations(Integer[] array, int k){
        ArrayList<int[]> sets = new ArrayList<>();

        int number_of_blockers = array.length - k;
        ArrayList<Integer> blockers = new ArrayList<>();
        for (int i = number_of_blockers; i > 0; i--){
            blockers.add(array.length - i);
        }

        while (true){
            int[] result = new int[k];
            int count = 0;
            for (int x = 0; x < array.length; x++){
                if (blockers.contains(x)){
                    continue;
                } else {
                    result[count++] = array[x];
                }
            }
            sets.add(result);

            boolean allSequential = true;
            boolean reset = false;
            for (int i = 0; i < blockers.size(); i++){
                if (blockers.get(i) != i){
                    allSequential = false;
                    // shift blocker down
                    int shiftIndex = blockers.get(i)-1;
                    blockers.set(i, shiftIndex);

                    // if reset, bring all forward blockers back
                    if (reset){
                        for (int x = i-1; x >= 0; x--){
                            blockers.set(x, --shiftIndex);
                        }
                    }
                    break;
                } else {
                    reset = true;
                }
            }
            if (allSequential){
                break;
            }
        }
        return sets;
    }


    public static void heapsAlgorithm(int k, int[] array, ArrayList<int[]> permutations){
        if (k==1){
            permutations.add(array.clone());
        } else {
            heapsAlgorithm(k -1, array, permutations);

            for (int i = 0; i < k-1; i+=1){
                int old;
                if (k % 2 == 0){
                    old = array[i];
                    array[i] = array[k-1];
                } else {
                    old = array[0];
                    array[0] = array[k-1];
                }
                array[k-1] = old;
                heapsAlgorithm(k-1, array, permutations);
            }
        }
    }

    public static int checkCorrectness(ArrayList<String> permutations, ArrayList<Integer> contiguousGroups){
        int count = 0;
        Pattern group = Pattern.compile("(#+)");

        for (String perms : permutations){
            Matcher pounds = group.matcher(perms);
            int matched = 0;
            boolean increment = true;
            while (pounds.find()){
                Integer size = pounds.group(1).length();
                if (matched >= contiguousGroups.size()){
                    break;
                }
                if (!Objects.equals(contiguousGroups.get(matched), size)){
                    increment = false;
                    break;
                }
                matched++;
            }
            if (increment && matched == contiguousGroups.size()){
                count+=1;
            }
        }
        return count;
    }
}