import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pillar implements Comparable<Pillar> {
    private int x;
    private int y;
    private Disk disk;
    private int cost = Integer.MAX_VALUE;
    private List<Pillar> adjacentPillars = new ArrayList<>();

    public Pillar(Disk disk, int x, int y) {
        this.setDisk(disk);
        this.setX(x);
        this.setY(y);
    }

    public double distanceToPillar(Pillar destination) {
        return Point2D.distance(this.x, this.y, destination.getX(), destination.getY());
    }

    @Override
    public int compareTo(Pillar o) {
        return this.cost - o.getCost();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pillar pillar = (Pillar) o;
        return x == pillar.x && y == pillar.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Disk getDisk() {
        return disk;
    }

    public void setDisk(Disk disk) {
        this.disk = disk;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public List<Pillar> getAdjacentPillars() {
        return adjacentPillars;
    }

    public void setAdjacentPillars(List<Pillar> adjacentPillars) {
        this.adjacentPillars = adjacentPillars;
    }
}