package util;

import model.TerrainPolicy;
import util.Position;

import java.util.*;

public class TargetStrategy implements MovementStrategy {

  @Override
  public Position nextPosition(Position position,
                               Collection<Position> targets,
                               Map<Position, List<Position>> neighbors,
                               TerrainPolicy terrainPolicy) {

    if (targets.isEmpty()) return position;
    Set<Position> seen = new HashSet<>();
    Map<Position, Position> firstMove = new HashMap<>();
    Queue<Position> toVisit = new LinkedList<>();

    for (Position initialMove : neighbors.get(position)) {
      if (!terrainPolicy.canFirefighterMoveTo(initialMove, false)) continue;
      toVisit.add(initialMove);
      seen.add(initialMove);
      firstMove.put(initialMove, initialMove);
    }

    while (!toVisit.isEmpty()) {
      Position current = toVisit.poll();
      if (targets.contains(current)) {
        return firstMove.get(current);
      }
      for (Position adjacent : neighbors.get(current)) {
        if (seen.contains(adjacent)) continue;
        if (!terrainPolicy.canFirefighterMoveTo(adjacent, false)) continue;
        toVisit.add(adjacent);
        seen.add(adjacent);
        firstMove.put(adjacent, firstMove.get(current));
      }
    }
    return position;
  }
}
