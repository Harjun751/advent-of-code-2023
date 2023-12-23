import jdk.jshell.EvalException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("input.txt"));
        MirrorPattern p = null;
        int total = 0;
        ArrayList<MirrorPattern> mirrorPatterns = new ArrayList<>();

        while (sc.hasNext()){
            String line = sc.nextLine();
            if (!line.isEmpty()){
                if (p != null) {
                    p.addLine(line);
                } else {
                    p = new MirrorPattern(line);
                }
            } else {
                total+= p.getPatternValue();
                mirrorPatterns.add(p);
                // clear pattern
                p = null;
            }
        }
        if (p!=null){
            total += p.getPatternValue();
            mirrorPatterns.add(p);
        }
        System.out.println(total);


        // Part 2
        int pt2Total = 0;
        for (MirrorPattern ptn : mirrorPatterns){
            pt2Total += ptn.getSmudgedPatternValue();
        }
        System.out.println(pt2Total);
    }
}

class MirrorPattern{
    ArrayList<String> lines;
    String top;
    int LOR;
    boolean HOR;

    public MirrorPattern(String first){
        this.lines = new ArrayList<>();
        this.top = first;
        lines.add(first);
    }

    public void addLine(String line){
        lines.add(line);
    }

    public int getPatternValue(){
        int longest = getVertical(this.lines);
        if (longest == -1){
            // rotate and pass to getVertical
            ArrayList<String> rotated = getRotated();
            int amnt = getVertical(rotated);
            longest = 100 * amnt;
            this.HOR = true;
        }
        return longest;
    }

    public int getSmudgedPatternValue(){
        int longest = getSmudgedVertical(this.lines);
        if (longest == -1){
            // rotate and pass to getVertical
            ArrayList<String> rotated = getRotated();
            int amnt = getSmudgedVertical(rotated);
            longest = 100 * amnt;
            this.HOR = true;
        }
        return longest;
    }

    private ArrayList<String> getRotated(){
        StringBuilder[] builders = new StringBuilder[this.top.length()];
        for (String s : this.lines){
            for (int x = 0; x < s.length(); x++){
                if (builders[x]==null){
                    builders[x] = new StringBuilder();
                }
                builders[x].append(s.charAt(x));
            }
        }

        ArrayList<String> rotated = new ArrayList<>();
        for (StringBuilder b : builders){
            rotated.add(b.toString());
        }
        return rotated;
    }

    public int getVertical(ArrayList<String> patternList){
        String first = patternList.get(0);

        ConcurrentHashMap<Integer, Integer> mirrorMap = new ConcurrentHashMap<>();
        for (int i = 1; i < first.length(); i++){
            for (int x = 1; x <= Math.min((first.length()-i), i); x++){
                char x1 = first.charAt(i-x);
                char x2 = first.charAt(i+(x-1));
                if (x1!=x2){
                    mirrorMap.remove(i);
                    break;
                } else {
                    // x represents the size of the mirroring
                    mirrorMap.put(i, x);
                }
            }
        }

        for (int i = 1; i < patternList.size(); i++){
            String current = patternList.get(i);
            for (int k : mirrorMap.keySet()){
                for (int x = 1; x <= mirrorMap.get(k); x++){
                    char x1 = current.charAt(k-x);
                    char x2 = current.charAt(k+(x-1));
                    if (x1!=x2){
                        // strict -> everything must match up to the mirrorLength length
                        mirrorMap.remove(k);
                        break;
                    }
                }
            }
        }

        int longest = -1;
        for (int k : mirrorMap.keySet()){
            longest = Math.max(longest, k);
        }
        this.LOR = longest;
        this.HOR = false;
        return longest;
    }

    public int getSmudgedVertical(ArrayList<String> patternList){
        ConcurrentHashMap<Integer, String> possibleDivisions = new ConcurrentHashMap<>();
        for (String line : patternList){
            for (int start = 1; start < line.length(); start++){
                int numWrong = 0;
                for (int x = 1; x <= Math.min((line.length()-start), start); x++){
                    char x1 = line.charAt(start-x);
                    char x2 = line.charAt(start+(x-1));
                    if (x1!=x2){
                        numWrong++;
                        if (numWrong>1){
                            // we don't need to go any further.
                            break;
                        }
                    }
                }
                if (numWrong==1){
                    possibleDivisions.put(start, line);
                }
            }
        }

        for (Integer y : possibleDivisions.keySet()){
            String ignore = possibleDivisions.get(y);

            int numWrong = 0;
            for (int i = 0; i < patternList.size(); i++){
                int currWrong = 0;
                String current = patternList.get(i);

                for (int x = 1; x <= Math.min((current.length()-y), y); x++){
                    char x1 = current.charAt(y-x);
                    char x2 = current.charAt(y+(x-1));
                    if (x1!=x2){
                        // strict -> everything must match up to the mirrorLength length
                        currWrong++;
                        if (currWrong==2){
                            break;
                        }
                    }
                }

                // can only be one wrong
                if (currWrong==1){
                    numWrong++;
                } else if (currWrong>1){
                    // set the upper loop to fail; the partition is invalid
                    numWrong = 2;
                    break;
                }
            }
            // valid
            if (numWrong==1){

            } else if (numWrong>1){
                // invalid, remove division
                possibleDivisions.remove(y);
            }

        }

        int longest = -1;
        for (int k : possibleDivisions.keySet()){
            longest = Math.max(longest, k);
        }
        return longest;
    }
}