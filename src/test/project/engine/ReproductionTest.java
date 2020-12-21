package project.engine;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedList;

public class ReproductionTest {

    @Test
    public void reproductionTest() {
        EvolutionMap map = new EvolutionMap(new MapDimensions(10, 10, 10));
        ReproductionEngine reproductionEngine = new ReproductionEngine(map, 20);

        Animal animal1 = new Animal(map, new Vector2d(2, 2), 10);
        Animal animal2 = new Animal(map, new Vector2d(2, 2), 20);
        Animal animal3 = new Animal(map, new Vector2d(2, 2), 30);

        Animal animal4 = new Animal(map, new Vector2d(3, 3), 10);

        Animal animal5 = new Animal(map, new Vector2d(1, 0), 5);
        Animal animal6 = new Animal(map, new Vector2d(1, 0), 15);

        HashSet<Vector2d> takenPositions = new HashSet<>();
        takenPositions.add(new Vector2d(2, 2));
        takenPositions.add(new Vector2d(3, 3));
        takenPositions.add(new Vector2d(1, 0));

        LinkedList<Animal> offsprings = reproductionEngine.reproduction(takenPositions);
        Assert.assertEquals(1, offsprings.size());
        Animal offspring1 = offsprings.removeFirst();

        Assert.assertEquals(12, offspring1.getEnergy());
        Assert.assertEquals(10, animal1.getEnergy());
        Assert.assertEquals(15, animal2.getEnergy());
        Assert.assertEquals(22, animal3.getEnergy());
        Assert.assertEquals(10, animal4.getEnergy());
        Assert.assertEquals(5, animal5.getEnergy());
        Assert.assertEquals(15, animal6.getEnergy());
    }

    @Test
    public void offspringPositionTest() {
        EvolutionMap map = new EvolutionMap(new MapDimensions(10, 10, 10));
        ReproductionEngine reproductionEngine = new ReproductionEngine(map, 10);
        HashSet<Vector2d> takenPositions = new HashSet<>();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == 0 && j == 0) {
                    new Animal(map, new Vector2d(1, 1), 30);
                } else {
                    new Animal(map, new Vector2d(i, j), 30);
                    takenPositions.add(new Vector2d(i, j));
                }
            }
        }

        LinkedList<Animal> offsprings = reproductionEngine.reproduction(takenPositions);
        Assert.assertEquals(1, offsprings.size());
        Assert.assertEquals(new Vector2d(0, 0), offsprings.getFirst().getPosition());

        takenPositions.add(new Vector2d(0, 0));
        offsprings = reproductionEngine.reproduction(takenPositions);
        Assert.assertEquals(1, offsprings.size());
        Vector2d position = offsprings.getFirst().getPosition();
        Assert.assertTrue(position.follows(new Vector2d(0, 0))
                && position.precedes(new Vector2d(2, 2))
                && !position.equals(new Vector2d(1, 1)));
    }
}
