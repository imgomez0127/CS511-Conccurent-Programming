package Assignment2;

import java.util.HashMap;
import java.util.HashSet;
import Assignment2.WeightPlateSize;

public class Gym implements Runnable{
    public static final int GYM_SIZE = 30;
    public static final int GYM_REGISTERED_CLIENTS = 10000;
    private HashMap<WeightPlateSize,Integer> noOfWeightPlates;
    private HashSet<Integer> clients;
    
    public void run(){
    }
}
