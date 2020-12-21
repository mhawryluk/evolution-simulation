package project.engine;

import org.junit.Assert;
import org.junit.Test;

public class StatisticsTest {

    @Test
    public void statisticsTest() {
        Statistics stats = new Statistics();

        EvolutionMap map = new EvolutionMap(new MapDimensions(5, 5, 5));
        Animal animal1 = new Animal(map, new Vector2d(0, 0), 10);
        stats.firstGenAnimalPlaced(animal1);
        Animal animal2 = new Animal(map, new Vector2d(0, 0), 10);
        stats.firstGenAnimalPlaced(animal2);

        stats.animalBorn(new Animal(map, new Vector2d(1, 1), 5));
        animal1.newChild();
        animal2.newChild();

        stats.newGrass();
        stats.newGrass();

        stats.animalDead(animal2);

        Assert.assertEquals(2, stats.countGrass());
        Assert.assertEquals(2, stats.countAnimals());

        Assert.assertEquals(0.5, stats.getAverageChildrenCount(), 0.01);
    }
}
