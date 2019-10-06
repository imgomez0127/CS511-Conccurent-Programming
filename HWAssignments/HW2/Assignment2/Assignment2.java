package Assignment2;

import java.io.*;
import java.util.*;
import Assignment2.Gym;

public class Assignment2 {
    public static void main(String[] Args){
        Thread thread = new Thread(new Gym());
    }
}
