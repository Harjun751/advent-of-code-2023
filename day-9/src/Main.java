import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        File f = new File("input.txt");
        Scanner s = new Scanner(f);

        int total = 0;
        while (s.hasNext()){
            String line = s.nextLine();
            String[] split = line.split(" ");
            ArrayList<Long> numbers = new ArrayList<>();
            for (String n : split){
                numbers.add(Long.parseLong(n));
            }
            total += reverse_extrapolate(numbers, numbers.size());
        }
        System.out.println(total);
    }

    public static long extrapolate(ArrayList<Long> nums, int size){
        // create the array of differences, while checking
        // if they are all 0
        boolean zeroed = true;
        ArrayList<Long> newNums = new ArrayList<>();
        Long previous = null;
        for (long number : nums){
            if (number!=0){
                zeroed = false;
            }
            if (previous!=null){
                newNums.add(number - previous);
            }
            previous = number;
        }

        // base case, all array of differences created.
        if (zeroed) {
            return 0;
        } else {
            // get the extrapolated value and add
            long num = extrapolate(newNums, size-1);
            return nums.get(size-1) + num;
        }
    }

    public static long reverse_extrapolate(ArrayList<Long> nums, int size){
        // create the array of differences, while checking
        // if they are all 0
        boolean zeroed = true;
        ArrayList<Long> newNums = new ArrayList<>();
        Long previous = null;
        for (long number : nums){
            if (number!=0){
                zeroed = false;
            }
            if (previous!=null){
                newNums.add(number - previous);
            }
            previous = number;
        }

        // base case -> we reached the end of differences
        if (zeroed) {
            return 0;
        } else {
            // get the (reverse) extrapolated value and take the difference
            // between the first number and it
            long num = reverse_extrapolate(newNums, size-1);
            return nums.get(0) - num;
        }
    }
}