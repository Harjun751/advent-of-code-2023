import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        EnginePart test = new EnginePart(2, 3, 2, 58);
        Symbol sTest = new Symbol(3, 1);
        test.isAdjacent(sTest.col(), sTest.row());

        int row = 0;

        Pattern engineParts = Pattern.compile("([0-9]+)");
        Pattern symbols = Pattern.compile("([^.\\d])");

        ArrayList<EnginePart> epList = new ArrayList<>();
        ArrayList<Symbol> symList = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            String line = br.readLine();

            while (line != null) {
                int col = 0;
                Matcher matcherEngineParts = engineParts.matcher(line);
                Matcher matcherSymbol = symbols.matcher(line);

                while (matcherEngineParts.find()){
                    int start = matcherEngineParts.start(1);
                    int end = matcherEngineParts.end(1) - 1;
                    int number = Integer.parseInt(matcherEngineParts.group(1));
                    EnginePart ep = new EnginePart(start, end, row, number);
                    epList.add(ep);
                }

                while (matcherSymbol.find()){
                    int start = matcherSymbol.start(1);
                    symList.add(new Symbol(start, row));
                }

                line = br.readLine();
                row +=1;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int total = 0;
        int gearRatios = 0;
        for (Symbol s : symList){
            ArrayList<EnginePart> toRemove = new ArrayList<>();
            for (EnginePart ep : epList){
                if (ep.isAdjacent(s.col(), s.row())){
                    total+=ep.getNumber();
                    toRemove.add(ep);
                }
            }
            // Only addition for part 2 is the below if statement
            if (toRemove.size() == 2){
                gearRatios+=toRemove.get(0).getNumber() * toRemove.get(1).getNumber();
            }
            for (EnginePart ep : toRemove){
                epList.remove(ep);
            }
        }
        System.out.println(total);
        System.out.println(gearRatios);
    }
}

record Symbol (int col, int row){}

class EnginePart {
    private int start;
    private int end;

    private int number;

    private int y;

    public EnginePart(int start, int end, int y, int number){
        this.start = start;
        this.end = end;
        this.y = y;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public boolean isAdjacent(int col, int row){
        if (this.y + 1 != row && this.y -1 != row && this.y != row) {
            return false;
        }
        if (this.start - 1 == col || this.start + 1 == col || this.start == col){
            return true;
        } else {
            if (this.end + 1 == col || this.end - 1 == col || this.end == col){
                return true;
            } else {
                return false;
            }
        }
    }
}