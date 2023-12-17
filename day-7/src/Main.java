import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
// pain.
public class Main {
    public static void main(String[] args) {
        ArrayList<Hand> handList = new ArrayList<Hand>();

        try(BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            String line = br.readLine();
            while(line!=null){
                String letters = line.split(" ")[0];
                long bid = Long.parseLong(line.split(" ")[1]);
                handList.add(new Hand(letters, bid));
                line = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Collections.sort(handList);
        int rank = 1;
        long totalWinnings = 0;
        for (Hand hand : handList){
            totalWinnings += hand.getBid() * rank;
            rank+=1;
        }
        System.out.println(totalWinnings);
    }
}

class Hand implements Comparable<Hand>{
    private String letters;
    private long bid;
    private int category;
    private static HashMap<Character, Integer> charStrength;
    static {
        charStrength = new HashMap<>();
        charStrength.put('2', 1);
        charStrength.put('3', 2);
        charStrength.put('4', 3);
        charStrength.put('5', 4);
        charStrength.put('6', 5);
        charStrength.put('7', 6);
        charStrength.put('8', 7);
        charStrength.put('9', 8);
        charStrength.put('T', 9);
        charStrength.put('J', 0);
//        charStrength.put('J', 10);
        charStrength.put('Q', 11);
        charStrength.put('K', 12);
        charStrength.put('A', 13);
    }
    public Hand(String hand, long bid){
        this.letters = hand;
        this.bid = bid;
        this.category = calculateCategoryJoker();
    }

    private int calculateCategory(){
        // calculates the category of the card (e.g. full house, three of a kind)
        // and it ranks them based on an arbitrary integer number
        // it doesn't matter what the integer is, just that we can compare the integers
        // and find the "stronger" category.

        // array tracks the amount of times an alphabet shows up in the hand
        // it is indexed by the char number of the alphabet itself, hence the size of 100.
        int[] freqArray = new int[100];
        int max = Integer.MIN_VALUE;
        for (char x : letters.toCharArray()){
            freqArray[x] += 1;
            if (freqArray[x] > max){
                max = freqArray[x];
            }
        }

        // find the number of pairs.
        // this is required for 2 kinds of decks, where additionally
        // having a pair of cards means it is a stronger hand.
        int pairCount = 0;
        for (int x : freqArray){
            if (x==2){
                pairCount+=1;
            }
        }

        // to get the category, we take the max value multiplied by 2.
        // it is multiplied by 2 so that there can be space to add in the stronger hands
        // with a pair in them (they are +1'd)
        if (pairCount==2){
            // a two pair is stronger than a one pair.
            return max * 2 + 1;
        } else if (pairCount==1 && max==3) {
            // a three of a kind is stronger if it has another pair in it
            return max * 2 + 1;
        }else {
            return max * 2;
        }
    }

    private int calculateCategoryJoker(){
        int max_points = Integer.MIN_VALUE;
        // we brute force the points
        // by replacing every joker character
        // with every other character possible
        // we then take the hand with the max points.
        for (char y : charStrength.keySet()){
            if (y=='J'){
                continue;
            }
            String copy = letters;
            copy = copy.replace('J', y);
            int[] freqArray = new int[100];
            int max = Integer.MIN_VALUE;
            for (char x : copy.toCharArray()){
                freqArray[x] += 1;
                if (freqArray[x] > max){
                    max = freqArray[x];
                }
            }

            int pairCount = 0;
            for (int x : freqArray){
                if (x==2){
                    pairCount+=1;
                }
            }

            int points;
            if (pairCount==2){
                points = max * 2 + 1;
            } else if (pairCount==1 && max==3) {
                points = max * 2 + 1;
            }else {
                points = max * 2;
            }
            if (points > max_points){
                max_points = points;
            }
        }
        return max_points;
    }

    public long getBid(){
        return this.bid;
    }

    @Override
    public int compareTo(Hand o) {
        // this allows the sorting of the hand.
        // firstly sorts using category, if the same category
        // breaks ties by checking the strength of the character
        if (this.category > o.category){
            return 1;
        } else if (this.category < o.category){
            return -1;
        } else {
            int index = 0;
            while (index < letters.length()){
                int thisCharStrength = charStrength.get(letters.charAt(index));
                int otherCharStrength = charStrength.get(o.letters.charAt(index));
                if (thisCharStrength > otherCharStrength){
                    return 1;
                } else if (thisCharStrength < otherCharStrength){
                    return -1;
                }
                index++;
            }
            return 0;
        }
    }
}