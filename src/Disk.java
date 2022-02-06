import java.util.Objects;

public class Disk implements Comparable<Disk> {
    private int radius;
    private int cost;

    public Disk(int radius, int cost) {
        this.radius = radius;
        this.cost = cost;
    }

    @Override
    public int compareTo(Disk o) {
        return this.cost - o.getCost();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disk disk = (Disk) o;
        return this.radius == disk.getRadius() && this.cost == disk.getCost();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.radius, this.cost);
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}