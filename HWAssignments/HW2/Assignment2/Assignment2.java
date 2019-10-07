/**
* Assignment 2: Modeling a gym using Semaphores
* "I pledge my honor that I have abided by the Stevens honor system" - igomez1 Ian Gomez
* Partner: Gary Ung
*/
package Assignment2;

import java.io.*;
import java.util.*;
import Assignment2.Gym;

public class Assignment2 {
    public static void main(String[] Args){
        Thread thread = new Thread(new Gym());
        try{
            thread.start();
            thread.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
