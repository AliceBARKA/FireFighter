package model;

import util.Position;

import java.util.*;

public class Fire {

    private final Set<Position> firePositions;

    public Fire(Set<Position> initialPositions) {
        this.firePositions = new HashSet<>(initialPositions);
    }

    public Set<Position> getFirePositions() {
        return Collections.unmodifiableSet(firePositions);
    }

    public boolean isOnFire(Position position) {
        return firePositions.contains(position);
    }

    public void extinguish(Position position) {
        firePositions.remove(position);
    }

    public List<Position> spread(int step,
                                 Map<Position, List<Position>> neighbors,
                                 TerrainPolicy terrainPolicy) {
        List<Position> newFirePositions = new ArrayList<>();

        for (Position fire : new HashSet<>(firePositions)) {
            for (Position neighbor : neighbors.get(fire)) {
                if (firePositions.contains(neighbor)) continue;
                int period = terrainPolicy.fireSpreadPeriodFor(neighbor);
                if (period == Integer.MAX_VALUE) continue;
                if (step % period != 0) continue;
                newFirePositions.add(neighbor);
            }
        }

        firePositions.addAll(newFirePositions);
        return newFirePositions;
    }
}
