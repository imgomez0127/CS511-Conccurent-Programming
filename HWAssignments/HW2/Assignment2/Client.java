package Assignment2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Semaphore;
import Assignment2.Exercise;
import Assignment2.WeightPlateSize;
import Assignment2.ApparatusType;

public class Client{
    private int id;
    private ArrayList<Exercise> routine;

    Client(int id){
        this.id = id;
    }

    public void addExercise(Exercise e){
        this.routine.add(e);
    }

    public static Client generateRandom(int id){
        Client newClient = new Client(id);
        newClient.generateRoutine();
        return newClient; 
    }

    public void generateRoutine() {
        for (int i = 0; i < 15; i++) {
            this.routine.addExercise(Exercise.generateRandom());
        }
    }

    public void executeRoutine(
          HashMap<ApparatusType,Semaphore> availableApparatuses,                                          
          HashMap<WeightPlateSize,Semaphore> availableWeights, 
          Semaphore tryToGrabWeights 
    ) {
        for (Exercise i : routine) {
            i.performExercise(availableApparatuses, availableWeights, tryToGrabWeights);
        }
        // This function executes all the exercises in the routine
    }
}
