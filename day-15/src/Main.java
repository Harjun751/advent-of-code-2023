import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    public static void main(String[] args) throws IOException {
        String input = Files.readAllLines(Path.of("input.txt")).get(0);
        int total = 0;
        Boxes boxes = new Boxes();
        for (String section : input.split(",")){
//            total += hash(section);
            if (section.contains("=")){
                String label = section.split("=")[0];
                Integer focalLength = Integer.parseInt(section.split("=")[1]);
                int box = hash(label);
                boxes.plus(box, label, focalLength);
            } else if (section.contains("-")){
                String label = section.split("-")[0];
                int box = hash(label);
                boxes.minus(box, label);
            }
        }
        System.out.println(boxes.getFocalPower());
    }



    public static int hash(String input){
        int val = 0;
        for (char c : input.toCharArray()){
            val += c;
            val *= 17;
            val = val%256;
        }


        return val;
    }
}

class Boxes{
    class Lens{
        private int focalLength;
        private String label;

        public Lens(String label, int focalLength){
            this.focalLength = focalLength;
            this.label = label;
        }
    }
    private HashMap<Integer, ArrayList<Lens>> boxes;
    public Boxes(){
        boxes = new HashMap<>();
    }
    public void minus(int box, String lensLabel){
        if (boxes.containsKey(box)){
            ArrayList<Lens> lenses = boxes.get(box);
            Lens toRemove = null;
            for (Lens lens : lenses){
                if (lens.label.equals(lensLabel)){
                    toRemove = lens;
                    break;
                }
            }
            lenses.remove(toRemove);
        }

    }
    public void plus(int box, String label, int focalLength){
        Lens lens = new Lens(label, focalLength);
        if (boxes.containsKey(box)){
            ArrayList<Lens> lenses = boxes.get(box);

            int replaceIndex = -1;
            for (int i = 0; i < lenses.size(); i++){
                Lens oldLens = lenses.get(i);
                if (oldLens.label.equals(label)){
                    replaceIndex = i;
                    break;
                }
            }
            if (replaceIndex !=-1){
                lenses.set(replaceIndex, lens);
            } else {
                lenses.add(lens);
            }
        } else {
            ArrayList<Lens> lenses = new ArrayList<>();
            lenses.add(lens);
            boxes.put(box, lenses);
        }

    }

    public int getFocalPower(){
        int total = 0;
        for (Integer box : boxes.keySet()){
            int subTotal = 0;
            ArrayList<Lens> lenses = boxes.get(box);
            for (int i = 0; i < lenses.size(); i++){
                subTotal += (box+1) * (i+1) * (lenses.get(i).focalLength);
            }
            total+=subTotal;
        }
        return total;
    }
}