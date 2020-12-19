package project.engine;

public class Grass extends MapElement {

    public Grass(Vector2d position, int energy){
        this.position = position;
        this.energy = energy;
    }

    @Override
    public String toString(){
        return "*";
    }
}
