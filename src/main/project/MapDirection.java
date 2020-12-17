package project;

public enum MapDirection {
    NORTH(0,"\u2191", new Vector2d(0, -1)),
    NORTH_EAST(1,"\u2197", new Vector2d(1, -1)),
    EAST(2,"\u2192", new Vector2d(1, 0)),
    SOUTH_EAST(3, "\u2198", new Vector2d(1, 1)),
    SOUTH(4, "\u2193", new Vector2d(0, 1)),
    SOUTH_WEST(5,"\u2199", new Vector2d(-1, 1)),
    WEST(6,"\u2190", new Vector2d(-1, 0)),
    NORTH_WEST(7,"\u2196", new Vector2d(-1, -1));

    private String shortName;
    private final Vector2d vector;
    private final int index;

    private static final MapDirection[] directions = {NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST};


    MapDirection(int index, String shortName, Vector2d vector){
        this.index = index;
        this.shortName = shortName;
        this.shortName = shortName;
        this.vector = vector;
    }

    public String toString(){
        return shortName;
    }

    public MapDirection turn(int turnValue){
        return directions[(index + turnValue)%8];
    }

    public Vector2d toVector(){
        return vector;
    }
    public int getIndex(){
        return index;
    }
}
