package project.engine;

import java.util.concurrent.ThreadLocalRandom;

public class Animal extends MapElement {
    private MapDirection orientation;
    private final EvolutionMap map;
    private final Genome genes;
    private int lifespan;
    private int childrenCount;

    public Animal(EvolutionMap map, Vector2d position, int energy){
        this(map, position, energy, new Genome());
    }

    public Animal(EvolutionMap map, Vector2d position, int energy, Genome genes){
        this.map = map;
        this.position = position;
        this.energy = energy;
        this.orientation = MapDirection.NORTH.turn(ThreadLocalRandom.current().nextInt(0, 8));
        this.genes = genes;
        map.place(this);
    }

    @Override
    public String toString() {
        return orientation.toString();
    }

    public void move(){
        Vector2d newPosition = position.add(orientation.toVector());
        newPosition = map.wrapPosition(newPosition);
        Vector2d oldPosition = position;
        position = newPosition;
        positionChanged(oldPosition);
        lifespan++;
    }

    public void turn(int turnValue){
        orientation = orientation.turn(turnValue);
    }

    public int getRandomTurnValue(){
        return genes.getRandomGene();
    }

    private void positionChanged(Vector2d oldPosition){
        map.positionChanged(oldPosition, this);
    }

    public boolean isDead(){
        return energy == 0;
    }

    public void decreaseEnergy(){
        energy--;
    }

    public void energyLost(){
        energy *= 0.75;
    }

    public void increaseEnergy(int nutrition){
        energy += nutrition;
    }

    public int getEnergy(){
        return energy;
    }

    public int getLifespan(){
        return lifespan;
    }

    public int getChildrenCount(){
        return childrenCount;
    }

    public MapDirection getOrientation(){
        return orientation;
    }

    public byte[] getGenes() {
        return genes.getGenome();
    }

    public void newChild(){ childrenCount++;}

    public String getGenomeString(){
        return genes.toString();
    }
}
