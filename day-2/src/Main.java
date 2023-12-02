import java.io.*;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {
    public static void main(String[] args) {
        int numberOfValidGames = 0;
        int powerSum = 0;
        HashMap<String, Integer> cubeSet = new HashMap<>();
        cubeSet.put("red", 12);
        cubeSet.put("green", 13);
        cubeSet.put("blue", 14);

        Pattern cubes = Pattern.compile("([0-9]+) ([a-zA-Z]+)");
        Pattern gameID = Pattern.compile("Game ([0-9]*)");

        try(BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            String line = br.readLine();

            while (line != null) {
                Matcher m = cubes.matcher(line);

                // Part 1 specific
                Matcher id = gameID.matcher(line);
                int currGameID;
                if (id.find()){
                    currGameID = Integer.parseInt(id.group(1));
                } else {
                    throw new IllegalArgumentException();
                }
                boolean valid = true;

                // Part 2 specific
                int minimumRed = 0;
                int minimumBlue = 0;
                int minimumGreen = 0;

                // general loop
                while (m.find()){
                    int number = Integer.parseInt(m.group(1));
                    String colour = m.group(2);

                    // part 1
                    if (number > cubeSet.get(colour)){
                        valid = false;
                    }

                    // part 2
                    if (colour.equals("red")){
                        minimumRed = Math.max(minimumRed, number);
                    } else if (colour.equals("blue")){
                        minimumBlue = Math.max(minimumBlue, number);
                    } else {
                        minimumGreen = Math.max(minimumGreen, number);
                    }
                }

                // part 1
                if (valid){
                    numberOfValidGames += currGameID;
                }

                // part 2
                powerSum += minimumRed * minimumBlue * minimumGreen;


                line = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(numberOfValidGames);
        System.out.println(powerSum);
    }
}