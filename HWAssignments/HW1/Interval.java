public class Interval {
    private int x;
    private int y;
    
    public Interval(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
         return this.y;
    }

    public int distance(){
        return this.y - this.x;
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}
