package jfreerails.world.common;


/**
 * A <b>mutable</b> class that stores the coordinates of the tile on entity is standing on
 * and the direction in which the entity is facing (usually the direction the entity
 * as just been moving), it provides methods to encode and decode its field
 * values to and from a single int.
 *
 * @author Luke
 */
public final class PositionOnTrack implements FreerailsMutableSerializable {
    private static final int BITS_FOR_COORINATE = 14;
    private static final int BITS_FOR_DIRECTION = 3;
    public static final int MAX_COORINATE = (1 << BITS_FOR_COORINATE) - 1;
    public static final int MAX_DIRECTION = (1 << BITS_FOR_DIRECTION) - 1;
    private int x = 0;

    public int hashCode() {
        int result;
        result = x;
        result = 29 * result + y;
        result = 29 * result + direction.hashCode();

        return result;
    }

    private int y = 0;
    private OneTileMoveVector direction = OneTileMoveVector.NORTH;

    public PositionOnTrack(int x, int y, OneTileMoveVector direction) {
        if (x > MAX_COORINATE || x < 0) {
            throw new IllegalArgumentException("x=" + x);
        }

        if (y > MAX_COORINATE || y < 0) {
            throw new IllegalArgumentException("y=" + y);
        }

        this.x = x;
        this.y = y;

        this.direction = direction;
    }

    public PositionOnTrack(int i) {
        this.setValuesFromInt(i);
    }

    public PositionOnTrack() {
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public OneTileMoveVector getDirection() {
        return direction;
    }

    /**
     * @return an integer representing this PositionOnTrack object
     */
    public int toInt() {
        int i = x;

        int shiftedY = y << BITS_FOR_COORINATE;
        i = i | shiftedY;

        int directionAsInt = direction.getNumber();
        int shiftedDirection = (directionAsInt << (2 * BITS_FOR_COORINATE));
        i = i | shiftedDirection;

        return i;
    }

    public void setValuesFromInt(int i) {
        x = i & MAX_COORINATE;

        int shiftedY = i & (MAX_COORINATE << BITS_FOR_COORINATE);
        y = shiftedY >> BITS_FOR_COORINATE;

        int shiftedDirection = i & (MAX_DIRECTION << (2 * BITS_FOR_COORINATE));
        int directionAsInt = shiftedDirection >> (2 * BITS_FOR_COORINATE);
        direction = OneTileMoveVector.getInstance(directionAsInt);
    }

    public boolean equals(Object o) {
        if (null == o) {
            return false;
        }

        if (o instanceof PositionOnTrack) {
            PositionOnTrack other = (PositionOnTrack)o;

            if (other.getDirection() == this.getDirection() &&
                    other.getX() == this.getX() && other.getY() == this.getY()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public String toString() {
        String s = "PositionOnTrack: " + x + ", " + y + ", " +
            direction.toString();

        return s;
    }

    /**
     * Sets the direction.
     * @param direction The direction to set
     */
    public void setDirection(OneTileMoveVector direction) {
        this.direction = direction;
    }

    /**
     * Sets the x.
     * @param x The x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the y.
     * @param y The y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return the position on the track which is in the opposite direction
     * and displacement.
     */
    public PositionOnTrack getOpposite() {
        int newX = this.getX() - this.direction.deltaX;
        int newY = this.getY() - this.direction.deltaY;
        OneTileMoveVector newDirection = this.direction.getOpposite();

        return new PositionOnTrack(newX, newY, newDirection);
    }
}