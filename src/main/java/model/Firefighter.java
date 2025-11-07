package model;

import util.Position;
import util.TargetStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Firefighter {
    private Position position;
    private final TargetStrategy targetStrategy = new TargetStrategy();

    public Firefighter(Position startPosition) {
        this.position = startPosition;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position newPosition) {
        this.position = newPosition;
    }

    public List<Position> act(Fire fire, Map<Position, List<Position>> neighbors) {
        List<Position> modifiedPositions = new ArrayList<>();

        // Déplacement vers le feu le plus proche
        Position newPosition = targetStrategy.neighborClosestToFire(position, fire.getFirePositions(), neighbors);
        modifiedPositions.add(position);
        modifiedPositions.add(newPosition);

        // Mise à jour de la position
        position = newPosition;

        // Éteindre le feu sur sa position
        fire.extinguish(position);

        // Éteindre les feux autour
        List<Position> adjacentFires = neighbors.get(position).stream()
                .filter(fire.getFirePositions()::contains)
                .toList();

        for (Position p : adjacentFires) {
            fire.extinguish(p);
            modifiedPositions.add(p);
        }

        return modifiedPositions;
    }
}
