package project.engine;

import org.junit.Assert;
import org.junit.Test;

public class MapDimensionsTest {

    @Test
    public void jungleDimensionsTest(){

        int jungleRatio = 50;
        MapDimensions dims = new MapDimensions(10, 10, jungleRatio);
        int jungleWidth = dims.jungleUpperRight.x - dims.jungleLowerLeft.x;
        int jungleHeight = dims.jungleUpperRight.y - dims.jungleLowerLeft.y;

        Assert.assertEquals( 7, jungleWidth);
        Assert.assertEquals( 7, jungleHeight);

        jungleRatio = 16;
        dims = new MapDimensions(60, 20, jungleRatio);
        jungleWidth = dims.jungleUpperRight.x - dims.jungleLowerLeft.x;
        jungleHeight = dims.jungleUpperRight.y - dims.jungleLowerLeft.y;

        Assert.assertEquals( 23, jungleWidth);
        Assert.assertEquals( 7, jungleHeight);
    }
}
