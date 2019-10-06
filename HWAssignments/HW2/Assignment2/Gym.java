package Assignment2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Semaphore;
import Assignment2.WeightPlateSize;
import Assignment2.ApparatusType;
public class Gym implements Runnable{
    public static final int GYM_SIZE = 30;
    public static final int GYM_REGISTERED_CLIENTS = 10000;
    private HashMap<WeightPlateSize,Integer> noOfWeightPlates;
    private HashSet<Integer> clients;
    private HashMap<WeightPlateSize,Semaphore> availableWeights;
    private HashMap<ApparatusType,Semaphore> availableApparatuses;
    private Semaphore grabWeights = new Semaphore(1);
    
    private void populateAvailableWeightsMap(){
        this.availableWeights = new HashMap<WeightPlateSize,Semaphore>();
        int weightSizeAmount[] = {110,90,75};
        int current_weight = 0;
        for(WeightPlateSize weightSize : WeightPlateSize.values()){
            this.availableWeights.put(
                weightSize,
                new Semaphore(weightSizeAmount[current_weight])
            );
            current_weight++;
        }
    }
    private void populateAvailableApparatusesMap(){
        this.availableApparatuses = new HashMap<ApparatusType,Semaphore>();
        for(ApparatusType apparatus:ApparatusType.values()){
            this.availableApparatuses.put(apparatus,new Semaphore(5));
        }
    }
        
    Gym(){
        populateAvailableWeightsMap();
        populateAvailableApparatusesMap();    
    }     

    public void run(){
    }
}
