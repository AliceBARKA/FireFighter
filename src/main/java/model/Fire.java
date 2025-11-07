package model;

import util.Position;
import java.util.*;

public class Fire {
    private final Set<Position> firePositions;

    public Fire(Set<Position> initialPositions) {
        this.firePositions = new HashSet<>(initialPositions);
    }

    public Set<Position> getFirePositions() {
        return firePositions;
    }

    public List<Position> spread(int step, Map<Position, List<Position>> neighbors) {
        List<Position> newFirePositions = new ArrayList<>();
        if (step % 2 == 0) { // Le feu s’étend tous les deux tours
            for (Position fire : firePositions)
                newFirePositions.addAll(neighbors.get(fire));
            firePositions.addAll(newFirePositions);
        }
        return newFirePositions;
    }

    public void extinguish(Position position) {
        firePositions.remove(position);
    }

    public boolean isOnFire(Position pos) {
        return firePositions.contains(pos);
    }
}
