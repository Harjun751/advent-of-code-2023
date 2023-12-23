import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class Main {
    public static void main(String[] args) throws IOException {
        ArrayList<String> pattern = new ArrayList<>();
        pattern = (ArrayList<String>) Files.readAllLines(Path.of("input.txt"));
        BeamFirstSearch bfs = new BeamFirstSearch(pattern);
        System.out.println(bfs.fullsimulate());
    }
}

class BeamFirstSearch{

    private boolean energized[][];
    private ArrayList<String> map;

    private int mapWidth;
    private int mapHeight;

    public BeamFirstSearch(ArrayList<String> pattern){
        this.map = pattern;
        this.mapWidth = map.get(0).length();
        this.mapHeight = map.size();
        energized = new boolean[mapHeight][mapWidth];
        this.simulate();
    }

    private void viewEnergy(){
        for (boolean[] brows : energized){
            for (boolean b : brows){
                String toPrint = b ? "#" : ".";
                System.out.print(toPrint);
            }
            System.out.println("");
        }
    }

    private int getEnergy(){
        int total = 0;
        for (boolean[] brows : energized){
            for (boolean b : brows){
                if (b){
                    total+=1;
                }
            }
        }
        return total;
    }

    private void simulate(){
        Beam start = new Beam(BeamState.RIGHTWARDS, 0, 0);
        LinkedList<Beam> queue = new LinkedList<>();
        queue.add(start);

        while (!queue.isEmpty()){
            Beam curr = queue.remove();
            queue.addAll(curr.step());
            // stop when converges
            // how do we know?
        }
        System.out.println(getEnergy());
    }

    private int simulate(BeamState state, int x, int y){
        this.energized = new boolean[mapHeight][mapWidth];
        Beam start = new Beam(state, x, y);
        LinkedList<Beam> queue = new LinkedList<>();
        queue.add(start);

        while (!queue.isEmpty()){
            Beam curr = queue.remove();
            queue.addAll(curr.step());
        }
        return getEnergy();
    }


    public int fullsimulate(){
        int mostEnergy = 0;
        for (int i = 0; i < mapWidth; i++){
            Beam.beamHistory = new HashSet<>();
            int energy = this.simulate(BeamState.DOWNWARDS, 0, i);
            mostEnergy = Math.max(mostEnergy, energy);

            Beam.beamHistory = new HashSet<>();
            energy = this.simulate(BeamState.UPWARDS, mapHeight-1, i);
            mostEnergy = Math.max(mostEnergy, energy);
        }

        for (int i = 0; i < mapHeight; i++){
            int energy = this.simulate(BeamState.RIGHTWARDS, i, 0);
            mostEnergy = Math.max(mostEnergy, energy);

            energy = this.simulate(BeamState.LEFTWARDS, i, mapWidth-1);
            mostEnergy = Math.max(mostEnergy, energy);
        }
        return mostEnergy;
    }

    public enum BeamState{
        DOWNWARDS,
        UPWARDS,
        RIGHTWARDS,
        LEFTWARDS;
    }

    class Beam{
        private static HashSet<Beam> beamHistory = new HashSet<>();
        private BeamState state;
        private int x;
        private int y;

        public Beam(BeamState state, int x, int y){
            this.state = state;
            this.x = x;
            this.y = y;
        }

        public Beam(Beam o){
            this.state = o.state;
            this.x = o.x;
            this.y = o.y;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj.getClass() != this.getClass()) {
                return false;
            }

            final Beam other = (Beam) obj;
            if (other.state==null){
                return false;
            }
            if (other.state == this.state && other.x == this.x && other.y == this.y){
                return true;
            }

            return false;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 53 * hash + this.state.hashCode();
            hash = 53 * hash + this.x + this.y;
            return hash;
        }

        public ArrayList<Beam> step(){
            ArrayList<Beam> resultantBeams = new ArrayList<>();
            if (this.x >= 0 && this.y >= 0 && this.x < mapHeight && this.y < mapWidth){
                // if a beam hits a wall, it fizzles out and dies.
                energized[this.x][this.y] = true;
            } else {
                return resultantBeams;
            }
            if (beamHistory.contains(this)){
                return resultantBeams;
            } else {
                beamHistory.add(new Beam(this));
            }

            // move the beam
            if (this.state == BeamState.RIGHTWARDS || this.state == BeamState.LEFTWARDS){
                char landed = map.get(this.x).charAt(this.y);
                if (landed == '/'){
                    if (this.state == BeamState.RIGHTWARDS){
                        this.state = BeamState.UPWARDS;
                    } else {
                        this.state = BeamState.DOWNWARDS;
                    }
                    resultantBeams.add(this);
                } else if (landed == '\\'){
                    energized[this.x][this.y] = true;
                    if (this.state == BeamState.RIGHTWARDS){
                        this.state = BeamState.DOWNWARDS;
                    } else {
                        this.state = BeamState.UPWARDS;
                    }
                    resultantBeams.add(this);
                } else if (landed == '|'){
                    energized[this.x][this.y] = true;
                    Beam up = new Beam(BeamState.UPWARDS, this.x-1, this.y);
                    Beam down = new Beam(BeamState.DOWNWARDS, this.x+1, this.y);
                    resultantBeams.add(up);
                    resultantBeams.add(down);
                } else {
                    resultantBeams.add(this);
                }
            } else {
                char landed = map.get(this.x).charAt(this.y);
                if (landed == '/'){
                    if (this.state == BeamState.UPWARDS){
                        this.state = BeamState.RIGHTWARDS;
                    } else {
                        this.state = BeamState.LEFTWARDS;
                    }
                    resultantBeams.add(this);
                } else if (landed == '\\'){
                    energized[this.x][this.y] = true;
                    if (this.state == BeamState.UPWARDS){
                        this.state = BeamState.LEFTWARDS;
                    } else {
                        this.state = BeamState.RIGHTWARDS;
                    }
                    resultantBeams.add(this);
                } else if (landed == '-'){
                    energized[this.x][this.y] = true;
                    Beam left = new Beam(BeamState.LEFTWARDS, this.x, this.y-1);
                    Beam right = new Beam(BeamState.RIGHTWARDS, this.x, this.y+1);
                    resultantBeams.add(left);
                    resultantBeams.add(right);
                } else {
                    resultantBeams.add(this);
                }
            }

            // move the beam
            if (this.state==BeamState.RIGHTWARDS){
                this.y += 1;
            } else if (this.state==BeamState.LEFTWARDS) {
                this.y -= 1;
            } else if (this.state==BeamState.UPWARDS){
                this.x -= 1;
            } else {
                this.x += 1;
            }
            return resultantBeams;
        }
    }
}