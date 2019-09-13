import java.io.*;
import java.util.*;

public class TextSwap {

    static char[] outputBuffer;

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
        Interval[] intervals = new Interval[numChunks];
        for(int i = 0; i < numChunks*chunkSize; i += chunkSize){
            intervals[i/chunkSize] = new Interval(i,i+chunkSize);
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
        return labels;
    }

    private static Interval getContentInterval(Interval[] intervals, Character label){
       return intervals[label.charValue() - 'a'];
    }

    private static Thread[] createSwapperThreads(String content, List<Character> labels, Interval[] intervals){
        Thread[] swapperThreads = new Thread[intervals.length];
        int intervalDistance = intervals[0].distance();
        for(int i = 0; i < intervals.length; i++){
            int offset = i * intervalDistance;
            Interval contentInterval = getContentInterval(intervals,
                labels.get(i)
            );
            swapperThreads[i] = new Thread(new Swapper(contentInterval,content,outputBuffer,
                offset));
        }
        return swapperThreads;
    }

    private static void startThreads(Thread[] threads){
        for(Thread thread : threads){
            thread.start();
        }
    }
    
    private static void joinThreads(Thread[] threads){
        for(Thread thread : threads){
            try{
                thread.join();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    /**
    * Runs threads which swaps around the content of a file into an output buffer.
    * The swapping is accomplished by splitting the the file into an equal amount
    * of chunks(sections) and then each thread maps a chunk from the input file
    * to the corresponding chunk in the output buffer
    * @param content A String which represents the content of the input file
    * @param chunkSize An int which defines the length of the chunk
    * @param numChunks An int which defines the amount of chunks the file is split into
    * @return outputBuffer A character array which will be written into the output file
    */
    private static char[] runSwapper(String content, int chunkSize, int numChunks) {
        List<Character> labels = getLabels(numChunks);
        Interval[] intervals = getIntervals(numChunks, chunkSize);
        outputBuffer = new char[numChunks*chunkSize];
        Thread[] swapperThreads = createSwapperThreads(content,labels,intervals);
        startThreads(swapperThreads);
        joinThreads(swapperThreads);
        return outputBuffer;
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
