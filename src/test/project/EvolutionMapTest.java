package project;
import org.junit.Assert;
import org.junit.Test;
import project.engine.*;

public class EvolutionMapTest {

    @Test
    public void getFreePositionTest(){
        EvolutionMap map = new EvolutionMap(new MapDimensions(100, 10, 50));
        Vector2d freePosition = map.getFreePosition();
        Assert.assertTrue(freePosition.follows(new Vector2d(0,0))
                && freePosition.precedes(new Vector2d(99,9))
                && !map.dimensions.isWithinJungle(freePosition));

        map = new EvolutionMap(new MapDimensions(3, 3, 10));
        new Animal(map, new Vector2d(0,0), 7);
        freePosition = map.getFreePosition();
        Assert.assertTrue(freePosition.follows(new Vector2d(0,0))
                && freePosition.precedes(new Vector2d(4,4))
                && !map.dimensions.isWithinJungle(freePosition)
                && !freePosition.equals(new Vector2d(0,0)));

        map = new EvolutionMap(new MapDimensions(1000, 1000, 90));
        freePosition = map.getFreePosition();
        Assert.assertTrue(freePosition.follows(new Vector2d(0,0))
                && freePosition.precedes(new Vector2d(999,999))
                && !map.dimensions.isWithinJungle(freePosition));
    }
}