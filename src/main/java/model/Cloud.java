package model;

import util.Position;

import java.util.*;

public class Cloud implements Actor {

  private Position position;
  private final Random random = new Random();

  public Cloud(Position startPosition) {
    this.position = startPosition;
  }

  @Override
  public Position getPosition() {
    return position;
  }

  @Override
  public void setPosition(Position position) {
    this.position = position;
  }

  @Override
  public ModelElement getModelElement() {
    return ModelElement.CLOUD;
  }

  @Override
  public List<Position> act(Fire fire,
                            Map<Position, List<Position>> neighbors,
                            TerrainPolicy terrainPolicy) {
    List<Position> modified = new ArrayList<>();

    Position oldPosition = position;

    List<Position> possibleMoves = neighbors.get(position).stream()
            .filter(p -> terrainPolicy.canCloudMoveTo(p))
            .toList();

    if (!possibleMoves.isEmpty()) {
      position = possibleMoves.get(random.nextInt(possibleMoves.size()));
    }

    if (!position.equals(oldPosition)) {
      modified.add(oldPosition);
      modified.add(position);
    } else {
      modified.add(position);
    }

    fire.extinguish(position);
    for (Position adj : neighbors.get(position)) {
      if (fire.isOnFire(adj)) {
        fire.extinguish(adj);
        modified.add(adj);
      }
    }

    return modified;
  }
}
