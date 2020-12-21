package project.engine;

public class Grass extends MapElement {

    public Grass(Vector2d position, EvolutionMap map){
        this.position = position;
        map.place(this);
    }
}
