package project;

public class Grass extends MapElement{

    public Grass(Vector2d position, int energy){
        this.position = position;
        this.energy = energy;
    }

    @Override
    public String toString(){
        return "*";
    }

    public void addObserver(IPositionChangeObserver observer){
        throw new IllegalCallerException("this element cannot be observed");
    }
}
