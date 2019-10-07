package Assignment2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Semaphore;
import java.util.concurrent.*;
import Assignment2.Client;
import Assignment2.WeightPlateSize;
import Assignment2.ApparatusType;

public class Gym implements Runnable{
    public static final int GYM_SIZE = 30;
    public static final int GYM_REGISTERED_CLIENTS = 10000;
    private HashMap<WeightPlateSize,Integer> noOfWeightPlates;
    private HashSet<Integer> clients;
    private HashMap<WeightPlateSize,Semaphore> availableWeights;
    private HashMap<ApparatusType,Semaphore> availableApparatuses;
    private Semaphore tryToGrabWeights = new Semaphore(1);
    
    private void populateAvailableWeightsMap(){
        this.availableWeights = new HashMap<WeightPlateSize,Semaphore>();
        int weightSizeAmount[] = {110,90,75};
        int currentWeight = 0;
        for(WeightPlateSize weightSize : WeightPlateSize.values()){
            this.availableWeights.put(
                weightSize,
                new Semaphore(weightSizeAmount[currentWeight])
            );
            currentWeight++;
        }
    }
    private void populateAvailableApparatusesMap(){
        this.availableApparatuses = new HashMap<ApparatusType,Semaphore>();
        for(ApparatusType apparatus:ApparatusType.values()){
            this.availableApparatuses.put(apparatus,new Semaphore(5));
        }
    }
        
    Gym(){
        this.noOfWeightPlates = new HashMap<WeightPlateSize,Integer>();
        this.noOfWeightPlates.put(WeightPlateSize.SMALL_3KG, 110);
        this.noOfWeightPlates.put(WeightPlateSize.MEDIUM_5KG, 90);
        this.noOfWeightPlates.put(WeightPlateSize.LARGE_10KG, 75);
        populateAvailableWeightsMap();
        populateAvailableApparatusesMap();    
    }     

    public void run(){
        ExecutorService executorService = Executors.newFixedThreadPool(Gym.GYM_SIZE);
        for(int i = 1; i < Gym.GYM_REGISTERED_CLIENTS+1; i++){
            Client client = Client.generateRandom(i);
            executorService.execute(new Runnable() {
                public void run(){
                    client.executeRoutine(availableApparatuses,availableWeights,tryToGrabWeights);
                }
            });
            StringBuilder gymState = new StringBuilder();
            gymState.append(noOfWeightPlates.toString());
            // gymState.append(clients.toString());
            gymState.append(availableWeights.toString());
            gymState.append(availableApparatuses.toString());
            gymState.append(tryToGrabWeights.toString());
            System.out.println(gymState.toString());
        }
        executorService.shutdown();
    }
}
