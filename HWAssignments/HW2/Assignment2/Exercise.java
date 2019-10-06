package Assignment2;

import java.util.HashMap;
import java.util.Random;
import Assignment2.WeightPlateSize;
import Assignment2.ApparatusType;

public class Exercise{
    private ApparatusType at;
    private HashMap<WeightPlateSize,Integer> weight;
    private int duration;
    
    Exercise(ApparatusType at, HashMap<WeightPlateSize,Integer> weight, int duration){
        this.at = at;
        this.weight = weight;
        this.duration = duration;
    }

    private ApparatusType pickRandomApparatus(){
        int apparatusNumber = new Random().nextInt(ApparatusType.values().length);
        return ApparatusType.values()[apparatusNumber];
    }

    public static Exercise generateRandom(){
        return null;
    }
}
