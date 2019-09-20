public class Swapper implements Runnable {
    private int offset;
    private Interval interval;
    private String content;
    private char[] buffer;

    public Swapper(Interval interval, String content, char[] buffer, int offset) {
        this.offset = offset;
        this.interval = interval;
        this.content = content;
        this.buffer = buffer;
    }

    private void copyContent(int i){
        int bufferPosition = i + offset;
        int contentPosition = interval.getX() + i;
        buffer[bufferPosition] =  content.charAt(contentPosition);
    }

    @Override
    public void run() {
        int writeDistance = this.interval.distance();
        for(int i = 0; i < writeDistance; i++){
            this.copyContent(i);
        }
    }
}
