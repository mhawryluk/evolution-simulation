package project.engine;

public class MapDimensions {

    public final int width;
    public final int height;
    public final Vector2d lowerLeft = new Vector2d(0,0);
    public final Vector2d upperRight;
    public final Vector2d jungleLowerLeft;
    public final Vector2d jungleUpperRight;

    public MapDimensions(int width, int height, int jungleRatio){
        this.width = width;
        this.height = height;

        upperRight = new Vector2d(width-1, height-1);

        int jungleWidth = (int)(Math.sqrt(jungleRatio/100.0)*width);
        int jungleHeight = (int)(Math.sqrt(jungleRatio/100.0)*height);

        jungleLowerLeft = new Vector2d((width-jungleWidth)/2, (height - jungleHeight)/2);
        jungleUpperRight = new Vector2d((width+jungleWidth)/2, (height + jungleHeight)/2);
    }

    public boolean isWithinMap(Vector2d position){
        return position.follows(lowerLeft) && position.precedes(upperRight);
    }

    public boolean isWithinJungle(Vector2d position){
        return position.follows(jungleLowerLeft) && position.precedes(jungleUpperRight);
    }

    public int allPositionsCount(){
        return width * height;
    }
}
