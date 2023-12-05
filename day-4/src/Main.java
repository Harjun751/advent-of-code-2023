import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        int tPoints = 0;
        int cardsProcessed = 0;
        Pattern numbers = Pattern.compile("[0-9]+");
        Pattern cardNumber = Pattern.compile("Card +([0-9]+)");

        ArrayList<Integer> duplicatedCards = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            String line = br.readLine();
            while (line != null) {
                int currPoints = 0;

                Matcher cn = cardNumber.matcher(line);
                int card_number = -1;
                if (cn.find()){
                    card_number = Integer.parseInt(cn.group(1));
                } else {
                    throw new RuntimeException();
                }

                line = line.trim();
                String[] lots = line.split(":")[1].split("\\|");
                ArrayList<String> winning_numbers = new ArrayList<>();
                ArrayList<String> obtained_numbers = new ArrayList<>();
                Matcher obt = numbers.matcher(lots[1]);
                Matcher win = numbers.matcher(lots[0]);

                while (obt.find()){
                    obtained_numbers.add(obt.group(0));
                }
                while (win.find()){
                    winning_numbers.add(win.group(0));
                }

                for (String o : obtained_numbers){
                    for (String c : winning_numbers){
                        if (o.equals(c)){
                            currPoints+=1;
//                            if (currPoints==0){
//                                currPoints = 1;
//                            } else {
//                                currPoints *= 2;
//                            }
//                            break;
                        }
                    }
                }

                while (currPoints>0){
                    duplicatedCards.add(card_number + currPoints);
                    int toAdd = 0;
                    for (int x : duplicatedCards){
                        if (x==card_number){
                            toAdd+=1;
                        }
                    }
                    while (toAdd>0){
                        duplicatedCards.add(card_number + currPoints);
                        toAdd-=1;
                    }
                    currPoints -= 1;
                }

                line = br.readLine();
                cardsProcessed+=1;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(tPoints);
        System.out.println(cardsProcessed + duplicatedCards.size());
    }
}