import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        Pattern regex = Pattern.compile("([0-9]+)");
        ArrayList<Long> timings = new ArrayList<>();
        ArrayList<Long> distances = new ArrayList<>();
        timings.add(49877895L);
        distances.add(356137815021882L);

//        try(BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
//            String line = br.readLine();
//            int counter = 0;
//            while (line != null) {
//                Matcher numbers = regex.matcher(line.split(":")[1]);
//                if (counter == 0){
//                    while (numbers.find()){
//                        timings.add(Integer.parseInt(numbers.group(0)));
//                    }
//                    counter+=1;
//                } else {
//                    while (numbers.find()){
//                        distances.add(Integer.parseInt(numbers.group(0)));
//                    }
//                }
//
//                line = br.readLine();
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        int total = 1;
        for (int i = 0; i < timings.size(); i++){
            long timing = timings.get(i);
            long distance = distances.get(i);
            long aux = timing;
            int waystowin = 0;
            while (aux > 0){
                // calculate speed gain
                long time_held = timing - aux;
                long time_travel = aux;
                long dist = time_held * time_travel;
                if (dist > distance){
                    waystowin+=1;
                }
                aux-=1;
            }
            total += waystowin;
        }
        System.out.println(total);
    }
}