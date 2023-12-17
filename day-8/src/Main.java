import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        File f = new File("input.txt");
        Scanner sc = new Scanner(f);

        String instructions = sc.nextLine();

        // skips an empty line
        sc.nextLine();

        // regex for later
        Pattern regx = Pattern.compile("([a-zA-Z0-9_.-]+) = \\(([a-zA-Z0-9_.-]+), ([a-zA-Z0-9_.-]+)\\)");
        // a hash map of network nodes
        HashMap<String, NetworkNode> nodes = new HashMap<>();

        // for part 2
        ArrayList<NetworkNode> starting_nodes = new ArrayList<>();


        while (sc.hasNext()){
            String line = sc.nextLine();
            Matcher m = regx.matcher(line);
            m.find();
            String current = m.group(1);
            String left = m.group(2);
            String right = m.group(3);

            NetworkNode curr;
            NetworkNode l;
            NetworkNode r;

            // for left, right and current
            // Checks if the nodes exist in the hashmap
            // if they don't, create new node and add
            // if they do, edit the current node to include left and right nodes
            if (!nodes.containsKey(current)){
                curr = new NetworkNode(current);
                nodes.put(current, curr);
            } else {
                curr = nodes.get(current);
            }
            if (!nodes.containsKey(left)){
                l = new NetworkNode(left);
                nodes.put(left, l);
            } else {
                l = nodes.get(left);
            }
            if (!nodes.containsKey(right)){
                r = new NetworkNode(right);
                nodes.put(right, r);
            } else {
                r = nodes.get(right);
            }
            curr.left = l;
            curr.right = r;
            // below is for part 2
            if (current.charAt(2) == 'A'){
                starting_nodes.add(curr);
            }
        }

        // part 1
        NetworkNode i = nodes.get("AAA");
        int steps = 0;
        int index = 0;
        while (!i.identifier.equals("ZZZ")){
            char direction = instructions.charAt(index);
            if (direction == 'L'){
                i = i.left;
            } else {
                i = i.right;
            }
            index +=1;
            steps += 1;
            if (index>instructions.length()-1){
                index = 0;
            }
        }
        System.out.println(steps);

        //variables for part 2
        int pt2_steps = 0;
        index = 0;
        int count = starting_nodes.size();
        // an array of numbers, where each number is where a first ending node
        // located for the respective starting node
        long[] LCM = new long[count];
        int fill_count = 0;

        while (true){
            boolean goLeft = instructions.charAt(index) == 'L';
            for (NetworkNode n : starting_nodes){
                int x = starting_nodes.indexOf(n);
                if (goLeft){
                    n = n.left;
                } else {
                    n = n.right;
                }
                // update the node in the array to reflect
                // the traversal
                starting_nodes.set(x, n);

                // set the first encounter of an ending element
                // in LCM
                if (n.identifier.charAt(2) == 'Z'){
                    if (LCM[x] == 0){
                        LCM[x] = pt2_steps+1;
                        fill_count+=1;
                    }
                }
            }
            pt2_steps += 1;

            // check if the LCM array is filled
            if (fill_count==starting_nodes.size()){
                break;
            }

            // increment index, loop around if required
            index += 1;
            if (index>instructions.length()-1){
                index = 0;
            }
        }

        // calculate lowest common multiple of the numbers in the array
        // to find the step at which all nodes will be at an ending node
        System.out.println(lowest_common_multiple(LCM));
    }

    public static long lowest_common_multiple(long[] numbers){
        if (numbers.length==2){
            return lowest_common_multiple(numbers[0], numbers[1]);
        } else {
            // reduce the array, taking the first 2 elements of the array,
            // finding the LCM. Once the LCM is found, the 2 elements are removed
            // and is replaced with the LCM value.
            // note: there must be some other way to do this more gracefully but I am not think.
            long a  = numbers[0];
            long b = numbers[1];
            long c = lowest_common_multiple(a, b);
            long[] aux = new long[numbers.length-1];
            aux[0] = c;
            int index = 2;
            while (index < numbers.length){
                aux[index - 1] = numbers[index];
                index +=1;
            }
            return lowest_common_multiple(aux);
        }
    }

    public static long lowest_common_multiple(long a, long b){
        long gcd = greatest_common_divisor(a, b);
        return a * b / gcd;
    }

    // using the Euclidean algorithm to calculate GCD. Thought i needed this but i don't.
    public static long greatest_common_divisor(long[] numbers){
        if (numbers.length==2){
            return greatest_common_divisor(numbers[0], numbers[1]);
        } else {
            long a  = numbers[0];
            long b = numbers[1];
            long c = greatest_common_divisor(a, b);
            long[] aux = new long[numbers.length-1];
            aux[0] = c;
            int index = 2;
            while (index < numbers.length){
                aux[index - 1] = numbers[index];
                index +=1;
            }
            return greatest_common_divisor(aux);
        }
    }

    public static long greatest_common_divisor(long a, long b){
        while (a!=b){
            if (a>b){
                a = a-b;
            } else {
                b = b-a;
            }
        }
        return a;
    }
}


class NetworkNode{
    public NetworkNode left;
    public NetworkNode right;
    public String identifier;

    public NetworkNode(NetworkNode left, NetworkNode right, String identifier){
        this.left = left;
        this.right = right;
        this.identifier = identifier;
    }

    public NetworkNode(String identifier){
        this.identifier = identifier;
    }
}