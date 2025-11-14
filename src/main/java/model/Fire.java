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

    public List<Position> spread(int step, Map<Position, List<Position>> neighbors ,
                           Set<Position> mountainPositions) {
        List<Position> modified = new ArrayList<>();
        if (step % 2 == 0) {
            List<Position> newFirePositions = new ArrayList<>();
            for (Position fire : firePositions) {
                for (Position neighbor : neighbors.get(fire)) {
                    // 🔥 Se propage uniquement si ce n’est pas une montagne
                    if (!mountainPositions.contains(neighbor)) {
                        newFirePositions.add(neighbor);
                    }
                }
            }
            firePositions.addAll(newFirePositions);
            modified.addAll(newFirePositions);
        }
        return modified;
    }

    public void extinguish(Position position) {
        firePositions.remove(position);
    }

    public boolean isOnFire(Position pos) {
        return firePositions.contains(pos);
    }
}
