package project;
import org.junit.Assert;
import org.junit.Test;


public class MapDirectionTest {

    @Test
    public void directionTest(){
        Assert.assertEquals(MapDirection.NORTH.turn(7), MapDirection.NORTH_WEST);
        Assert.assertEquals(MapDirection.SOUTH.turn(2), MapDirection.WEST);
        Assert.assertEquals(MapDirection.NORTH_WEST.turn(1), MapDirection.NORTH);
        Assert.assertEquals(MapDirection.SOUTH_WEST.turn(4), MapDirection.NORTH_EAST);
    }
}
