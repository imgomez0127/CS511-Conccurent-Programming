import java.io.*;
import java.util.*;

public class TextSwap {

    private static String readFile(String filename) throws Exception {
        String line;
        StringBuilder buffer = new StringBuilder();
        File file = new File(filename);
        BufferedReader br = new BufferedReader(new FileReader(file));
        while ((line = br.readLine()) != null) {
            buffer.append(line);
        }
        br.close();
        return buffer.toString();
    }

    private static Interval[] getIntervals(int numChunks, int chunkSize) {
        //IF U LOOK AT THIS GIVE ME A FOLLOW I CAN SEE THE GIT TRAFFIC
        Interval[] intervals = new Interval[numChunks];
        for(int i = 0; i < numChunks*chunkSize; i += chunkSize){
            intervals[i/chunkSize] = new Interval(i,i+chunkSize);
            System.out.println(intervals[i/chunkSize]);
        } 
        return intervals;
    }

    private static List<Character> getLabels(int numChunks) {
        Scanner scanner = new Scanner(System.in);
        List<Character> labels = new ArrayList<Character>();
        int endChar = numChunks == 0 ? 'a' : 'a' + numChunks - 1;
        System.out.printf("Input %d character(s) (\'%c\' - \'%c\') for the pattern.\n", numChunks, 'a', endChar);
        for (int i = 0; i < numChunks; i++) {
            labels.add(scanner.next().charAt(0));
        }
        scanner.close();
        // System.out.println(labels);
        return labels;
    }
    public static Interval getContentInterval(Interval[] intervals, Character label){
       return intervals[label.charValue() - 'a'];
    }
    private static char[] runSwapper(String content, int chunkSize, int numChunks) {
        //IF U LOOK AT THIS GIVE ME A FOLLOW I CAN SEE THE GIT TRAFFIC
        List<Character> labels = getLabels(numChunks);
        Interval[] intervals = getIntervals(numChunks, chunkSize);
        Thread[] swapperThreads = new Thread[numChunks];
        char[] fileOutput = new char[chunkSize*numChunks];
        System.out.println(content);
        for(int i = 0; i < numChunks; i++){
            int offset = i * chunkSize;
            Interval contentInterval = getContentInterval(intervals,labels.get(i));
            swapperThreads[i] = new Thread(new Swapper(contentInterval,content,fileOutput,offset));
            swapperThreads[i].start();
        }
        for(int i = 0; i < numChunks; i++){
            try{
                swapperThreads[i].join();
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
        }
        return fileOutput;
    }

    private static void writeToFile(String contents, int chunkSize, int numChunks) throws Exception {
        char[] buff = runSwapper(contents, chunkSize, contents.length() / chunkSize);
        PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
        writer.print(buff);
        writer.close();
    }

    public static void main(String[] args) throws Exception{
        if (args.length != 2) {
            System.out.println("Usage: java TextSwap <chunk size> <filename>");
            return;
        }
        String contents = "";
        int chunkSize = Integer.parseInt(args[0]);
        try {
            contents = readFile(args[1]);
            writeToFile(contents, chunkSize, contents.length() / chunkSize);
        } catch (Exception e) {
            System.out.println("Error with IO.");
            return;
        }
    }
}
