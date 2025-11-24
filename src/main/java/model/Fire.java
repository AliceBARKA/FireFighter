package model;

import util.Position;
import java.util.*;

public class Fire {
    private final Set<Position> firePositions;
    private Map<Position, Integer> pendingRockFires = new HashMap<>();

    public Fire(Set<Position> initialPositions) {
        this.firePositions = new HashSet<>(initialPositions);
    }

    public Set<Position> getFirePositions() {
        return firePositions;
    }

    public List<Position> spread(int step, Map<Position, List<Position>> neighbors ,
                           Set<Position> mountainPositions , Set<Position> roadPosition,Set<Position> rockPositions) {
        List<Position> modified = new ArrayList<>();
        List<Position> rocksNowOnFire = new ArrayList<>();
        //on active les rocailles dont le délai est écoulé
        for (Map.Entry<Position, Integer> entry : pendingRockFires.entrySet()) {
            int ignitionStep = entry.getValue();
            if (ignitionStep <= step) {
                rocksNowOnFire.add(entry.getKey());
            }
        }
        // On enlève ces rocailles de la map d'attente
        for (Position p : rocksNowOnFire) {
            pendingRockFires.remove(p);
        }
        firePositions.addAll(rocksNowOnFire);
        modified.addAll(rocksNowOnFire);

        if (step % 2 == 0) {
            List<Position> newFirePositions = new ArrayList<>();
            for (Position fire : firePositions) {
                for (Position neighbor : neighbors.get(fire)) {
                    // On ne propage jamais sur montagnes ni routes
                    if (mountainPositions.contains(neighbor) || roadPosition.contains(neighbor)) {
                        continue;
                    }

                    // Deja en feu
                    if (firePositions.contains(neighbor)) {
                        continue;
                    }

                    // 🪨 CAS ROCAILLE : on programme le feu pour dans 4 tours
                    if (rockPositions.contains(neighbor)) {
                        // On ne reprogramme pas si c'est déjà dans la map
                        if (!pendingRockFires.containsKey(neighbor)) {
                            pendingRockFires.put(neighbor, step + 4);
                        }
                    } else {
                        // Terrain normal → feu immédiat (comme avant)
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
