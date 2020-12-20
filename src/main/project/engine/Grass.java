package project.engine;

public class Grass extends MapElement {

    public Grass(Vector2d position){
        this.position = position;
    }

    @Override
    public String toString(){
        return "*";
    }
}
