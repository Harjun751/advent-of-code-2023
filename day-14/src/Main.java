import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("input.txt"));
        String firstLine = sc.nextLine();
        RotatedPattern pat = new RotatedPattern(firstLine);
        while (sc.hasNext()){
            pat.addLine(sc.nextLine());
        }
        pat.finish();
        pat.washingMachine();
        System.out.println(pat.getWeight());
    }


}

class RotatedPattern {
    ArrayList<String> lines;

    ArrayList<StringBuilder> northMajor;
    ArrayList<StringBuilder> westMajor;
    ArrayList<StringBuilder> southMajor;
    ArrayList<StringBuilder> eastMajor;

    public RotatedPattern(String first) {
        this.lines = new ArrayList<>();
        northMajor = new ArrayList<>();
        westMajor = new ArrayList<>();
        eastMajor = new ArrayList<>();
        southMajor = new ArrayList<>();
        for (int x = 0; x < first.length(); x++){
            southMajor.add(new StringBuilder());
            southMajor.get(x).append(first.charAt(x));
        }
        westMajor.add(new StringBuilder(first));
    }

    public void addLine(String line) {
        for (int x = 0; x < line.length(); x++){
            southMajor.get(x).append(line.charAt(x));
        }
        westMajor.add(new StringBuilder(line));
    }

    public void finish(){
        for (int x = 0; x < southMajor.size(); x++){
            StringBuilder sb = southMajor.get(x);
            StringBuilder nb = new StringBuilder(sb);
            southMajor.set(x, sb.reverse());
            northMajor.add(0, nb);
        }
        for (int x = 0; x < westMajor.size(); x++){
            StringBuilder sb = westMajor.get(x);
            StringBuilder nb = new StringBuilder(sb);
            eastMajor.add(0, nb.reverse());
        }
    }

    public void moveNorth(){
        ArrayList<String> newLines = new ArrayList<>();
        for (String s : lines){
            int nextEmptySlot = 0;
            StringBuilder sb = new StringBuilder(s);
            for (int i = 0; i < s.length(); i++){
                if (s.charAt(i)=='O'){
                    if (nextEmptySlot==i){
                        nextEmptySlot++;
                    } else {
                        sb.replace(i, i+1, ".");
                        sb.replace(nextEmptySlot, nextEmptySlot+1, "O");
                        nextEmptySlot++;
                    }
                } else if (s.charAt(i) == '#'){
                    nextEmptySlot = i+1;
                }
            }
            newLines.add(sb.toString());
        }
        this.lines = newLines;
    }

    public void updateAll(int indicator, int x, int y, int y1){
        ArrayList<StringBuilder> previous;
        if (indicator==1){
            previous = northMajor;
        } else if (indicator==2){
            previous = westMajor;
        } else if (indicator==3){
            previous = southMajor;
        } else {
            previous = eastMajor;
        }
        int xMax = previous.size() - 1;
        int x1 = x;
        for (int temp = 1; temp < 4; temp++){
            int which = indicator + temp;
            if (which > 4){
                which = which - 4;
            }
            ArrayList<StringBuilder> toEdit;
            if (which==1){
                toEdit = northMajor;
            } else if (which==2){
                toEdit = westMajor;
            } else if (which==3){
                toEdit = southMajor;
            } else {
                toEdit = eastMajor;
            }
            int tempY = y;
            y = xMax - x;
            x = tempY;
            int tempY1 = y1;
            y1 = xMax - x1;
            x1 = tempY1;
            toEdit.get(x).replace(y, y+1, ".");
            toEdit.get(x1).replace(y1, y1+1, "O");
            // update xmax
            xMax = toEdit.size() - 1;
        }
    }

    public void moveAndUpdate(int indicator){
        ArrayList<StringBuilder> current;
        if (indicator==1){
            current = northMajor;
        } else if (indicator==2){
            current = westMajor;
        } else if (indicator==3){
            current = southMajor;
        } else {
            current = eastMajor;
        }

        for (int x = 0; x < current.size(); x++){
            int nextEmptySlot = 0;
            StringBuilder sb = current.get(x);
            for (int i = 0; i < sb.length(); i++){
                if (sb.charAt(i)=='O'){
                    if (nextEmptySlot==i){
                        nextEmptySlot++;
                    } else {
                        sb.replace(i, i+1, ".");
                        sb.replace(nextEmptySlot, nextEmptySlot+1, "O");

                        updateAll(indicator, x, i, nextEmptySlot);
                        nextEmptySlot++;
                    }
                } else if (sb.charAt(i) == '#'){
                    nextEmptySlot = i+1;
                }
            }
        }
    }

    private HashMap<String, Integer> occurenceCount = new HashMap<>();

    private String toString(ArrayList<StringBuilder> list){
        StringBuilder finalStr = new StringBuilder();
        for (StringBuilder sb : list){
            finalStr.append(sb + "\n");
        }
        return finalStr.toString();
    }

    private static int num = 1000000000;

    public void washingMachine(){
        for (int i = 1; i <= num; i++){
            this.cycle();
            String cycled = toString(this.westMajor);
            if (occurenceCount.containsKey(cycled)){
                // what if a cycle doesn't start at the start?? HMM?

                // the number of transformations required to get here
                int nTransformations = i - occurenceCount.get(cycled);
                // the amount of times this transfomration can occur
                // in the remaining iterations
                int numTimes = ((num-occurenceCount.get(cycled))/nTransformations);
                // the index after repeating the transformations
                // and arriving at the same state as now
                int finalIndex = nTransformations * numTimes + occurenceCount.get(cycled);
                i = finalIndex;
            } else {
                occurenceCount.put(cycled, i);
            }
        }
    }

    public void cycle(){
        // move north-major array to the left
        this.moveAndUpdate(1);
        // update all other arrays

        // move west-major
        this.moveAndUpdate(2);

        // move south-major
        this.moveAndUpdate(3);

        // move east
        this.moveAndUpdate(4);
    }
    public int getWeight(){
        int totalLoad = 0;
        for (StringBuilder s : this.northMajor){
            int subTotalLoad = 0;
            for (int i = 0; i < s.length(); i++){
                if (s.charAt(i) == 'O'){
                    int weight = (s.length() - i);
                    subTotalLoad+=weight;
                }
            }
            totalLoad += subTotalLoad;
        }
        return totalLoad;
    }
}