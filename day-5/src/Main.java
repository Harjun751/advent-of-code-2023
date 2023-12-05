import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) {
        RangeMap seed_soil = new RangeMap();
        RangeMap soil_fert = new RangeMap();
        RangeMap fert_water = new RangeMap();
        RangeMap water_light = new RangeMap();
        RangeMap light_temp = new RangeMap();
        RangeMap temp_hum = new RangeMap();
        RangeMap hum_loc = new RangeMap();

        int mode = -2;
        ArrayList<Long> seeds = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            String line = br.readLine();
            while (line != null) {
                if (line.contains(":")){
                    if (mode == -2){
                        String[] seedstr = line.split(":")[1].trim().split(" ");
                        for (String str : seedstr){
                            seeds.add(Long.parseLong(str));
                        }
                    }
                    mode+=1;
                    line = br.readLine();
                    continue;
                }
                if (line.equals("")){
                    line = br.readLine();
                    continue;
                }
                String[] map = line.trim().split(" ");
                Long dest = Long.parseLong(map[0]);
                Long src = Long.parseLong(map[1]);
                Long range = Long.parseLong(map[2]);
                if (mode==0){
                    seed_soil.addRange(new Range(src, dest, range));
                } else if (mode==1){
                    soil_fert.addRange(new Range(src,dest,range));
                } else if (mode==2){
                    fert_water.addRange(new Range(src,dest,range));
                } else if (mode==3){
                    water_light.addRange(new Range(src,dest,range));
                } else if (mode==4){
                    light_temp.addRange(new Range(src,dest,range));
                } else if (mode==5){
                    temp_hum.addRange(new Range(src,dest,range));
                } else if (mode==6){
                    hum_loc.addRange(new Range(src,dest,range));
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Long minloc = Long.MAX_VALUE;
        Long prev = null;
        for (Long seed : seeds){
            if (prev==null){
                prev = seed;
            } else {
                while (seed > 0){
                    // this took super long. i have no idea how to make it better
                    minloc = Math.min(minloc, hum_loc.getCorr(temp_hum.getCorr(light_temp.getCorr(water_light.getCorr(fert_water.getCorr(soil_fert.getCorr(seed_soil.getCorr(prev + seed))))))));
                    seed -= 1;
                }
                prev = null;
            }
        }
        System.out.println(minloc);
    }
}

class Range{
    private long start;
    private long otherStart;
    private long length;

    public Range(long start, long otherStart, long length){
        this.start = start;
        this.otherStart = otherStart;
        this.length = length;
    }

    public boolean inRangeOther(long x){
        if (x < otherStart+length && x>=otherStart){
            return true;
        }
        return false;
    }

    public boolean inRange(long x){
        if (x < start+length && x>=start){
            return true;
        }
        return false;
    }

    public long getOther(long x){
        long offset = x - start;
        return otherStart + offset;
    }
}

class RangeMap{
    private ArrayList<Range> ranges = new ArrayList<>();

    public void addRange(Range r){
        this.ranges.add(r);
    }

    public long getCorr(long z){
        for (Range x : ranges){
            if (x.inRange(z)){
                return x.getOther(z);
            }
        }
        return z;
    }
}