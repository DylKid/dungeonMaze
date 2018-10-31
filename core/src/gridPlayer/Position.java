package gridPlayer;

public class Position implements Location{

    private int x;
    private int y;

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Position(Location location){
        this.x = location.getX();
        this.y = location.getY();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString(){
        return String.format("PosX:%s PosY:%s\n",x,y);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Position){
            Position otherPosition = (Position) o;
            if(this.x == otherPosition.getX() && this.y == otherPosition.getY()){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode(){
        int a = 17;
        a += x;
        a *= y;
        return a;
    }
}
