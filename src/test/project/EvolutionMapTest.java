package project;
import org.junit.Assert;
import org.junit.Test;

public class EvolutionMapTest {

    @Test
    public void getFreePositionsTest(){
        EvolutionMap map = new EvolutionMap(100, 10, 50);
        map.getFreeJunglePosition();
        System.out.println(map.getFreePosition());
    }

    @Test
    public void jungleTest(){
        EvolutionMap map = new EvolutionMap(10, 10, 50);
        Vector2d junglePosition = map.getFreeJunglePosition();
        Assert.assertTrue(junglePosition.follows(new Vector2d(4,4)) && junglePosition.precedes(new Vector2d(5,5)));
    }
}