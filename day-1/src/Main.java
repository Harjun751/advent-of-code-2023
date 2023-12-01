import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        int calibrationSum = 0;
        try(BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            String line = br.readLine();
            // Add the string-based numbers into a hashset
            // add the reverse as well so that it doesn't have to be computed on the fly
            HashMap<String, Integer> numbers = new HashMap<>();
            numbers.put("one", 1);
            numbers.put("eno", 1);
            numbers.put("two", 2);
            numbers.put("owt", 2);
            numbers.put("three", 3);
            numbers.put("eerht", 3);
            numbers.put("four", 4);
            numbers.put("ruof", 4);
            numbers.put("five", 5);
            numbers.put("evif", 5);
            numbers.put("six", 6);
            numbers.put("xis", 6);
            numbers.put("seven", 7);
            numbers.put("neves", 7);
            numbers.put("eight", 8);
            numbers.put("thgie", 8);
            numbers.put("nine", 9);
            numbers.put("enin", 9);

            while (line != null) {
                boolean front = false;
                boolean back = false;
                StringBuilder frontSB = new StringBuilder();
                StringBuilder backSB = new StringBuilder();
                int frt = -1;
                int bck = -1;
                int i = 0;
                while (!front || !back){
                    if (!front){
                        char c = line.charAt(i);
                        frt = getNumber(c);
                        if (frt!=-1){
                            // if frt is a valid number
                            front = true;
                        } else {
                            // this is a string, we need to add it to a stringbuilder
                            frontSB.append(c);
                            // check if word exists in number hashset
                            // there's probably a better way to do this but it's late at night
                            // and i'm tired *_*
                            for (int x = 0; x < frontSB.length(); x++){
                                // checks thru every possible start for a valid word string
                                if (numbers.containsKey(frontSB.substring(x))){
                                    frt = numbers.get(frontSB.substring(x));
                                    front = true;
                                    break;
                                }
                            }
                        }
                    }
                    // p much the same for the back
                    if (!back){
                        char c = line.charAt(line.length()-1-i);
                        bck = getNumber(c);
                        if (bck!=-1){
                            back = true;
                        } else {
                            backSB.append(c);
                            for (int x = backSB.length(); x >= 0; x--){
                                if (numbers.containsKey(backSB.substring(x))){
                                    bck = numbers.get(backSB.substring(x));
                                    back = true;
                                    break;
                                }
                            }
                        }
                    }
                    i+=1;
                }
                int calibrationValue = Integer.parseInt("" + frt + bck);
                calibrationSum += calibrationValue;
                line = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(calibrationSum);
    }

    public static int getNumber(char str){
        int i = Character.getNumericValue(str);
        if (i>9){
            return -1;
        }
        return i;
    }
}