package project.engine;

import org.junit.Assert;
import org.junit.Test;

public class MapDirectionTest {

    @Test
    public void directionTurnTest() {
        Assert.assertEquals(MapDirection.NORTH.turn(7), MapDirection.NORTH_WEST);
        Assert.assertEquals(MapDirection.SOUTH.turn(2), MapDirection.WEST);
        Assert.assertEquals(MapDirection.NORTH_WEST.turn(1), MapDirection.NORTH);
        Assert.assertEquals(MapDirection.SOUTH_WEST.turn(4), MapDirection.NORTH_EAST);
        Assert.assertEquals(MapDirection.NORTH.turn(0), MapDirection.NORTH);
        Assert.assertEquals(MapDirection.SOUTH_WEST.turn(0), MapDirection.SOUTH_WEST);
        Assert.assertEquals(MapDirection.NORTH_WEST.turn(2), MapDirection.NORTH_EAST);
    }

    @Test
    public void toVectorTest() {
        Assert.assertEquals(new Vector2d(0, -1), MapDirection.NORTH.toVector());
        Assert.assertEquals(new Vector2d(1, 1), MapDirection.SOUTH_EAST.toVector());

        MapDirection orientation = MapDirection.EAST;
        orientation = orientation.turn(2);
        Assert.assertEquals(new Vector2d(0, 1), orientation.toVector());
    }

}
