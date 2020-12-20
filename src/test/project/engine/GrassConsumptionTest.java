package project.engine;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;

public class GrassConsumptionTest {

    @Test
    public void grassConsumptionTest(){

        EvolutionMap map = new EvolutionMap(new MapDimensions(10,10,10));
        map.place(new Grass(new Vector2d(5,5)));

        Animal animal1 = new Animal(map, new Vector2d(5,5), 10);
        Animal animal2 = new Animal(map, new Vector2d(5,5), 10);

        GrassConsumptionEngine grassConsumptionEngine = new GrassConsumptionEngine(map, 5);
        HashSet<Vector2d> takenPositions = new HashSet<>();
        takenPositions.add(new Vector2d(5,5));

        int grassEaten = grassConsumptionEngine.consumption(takenPositions);

        Assert.assertEquals(1, grassEaten);
        Assert.assertEquals(12, animal1.getEnergy());
        Assert.assertEquals(12, animal2.getEnergy());

        animal1.move();
        animal2.move();

        Assert.assertFalse(map.isOccupied(new Vector2d(5,5)));
    }
}
