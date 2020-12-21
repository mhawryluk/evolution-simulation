package project.engine;

import org.junit.Assert;
import org.junit.Test;

public class AnimalObservationTest {

    @Test
    public void animalObservationTest(){
        EvolutionMap map = new EvolutionMap(new MapDimensions(10,10,5));
        Animal animal1 = new Animal(map, new Vector2d(0,0), 5);
        Animal animal2 = new Animal(map, new Vector2d(0,1), 5);
        Animal animal3 = new Animal(map, new Vector2d(1,0), 4);

        AnimalObservation animalObservation = new AnimalObservation(animal1, 5);
        animalObservation.newChild(animal2);
        animalObservation.newOffspring(animal3);
        animalObservation.died(7);

        Assert.assertEquals("children count: 1\ntotal offspring count: 2\nday of death: 2",
                animalObservation.getObservedAnimalInfo());
    }
}
