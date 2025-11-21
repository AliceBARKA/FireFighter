package model;

import util.Position;

import java.util.*;

public class Cloud {
  private Position position ;
  private Random random = new Random() ;

  public Cloud(Position startPosition){
    position = startPosition ;
  }



  public Position getPosition(){
    return position ;
  }
  public void setPosition(Position newPosition){
    position = newPosition ;
  }

  public List<Position> act(Fire fire,
    Map<Position, List<Position>> neighbors,
    Set<Position> mountainPositions,
    Set<Position> roadPositions ) {
    List<Position> modifiedPositions = new ArrayList<>();

    Position oldPosition = position;

    List<Position> possibleMoves = neighbors.get(position)
                                            .stream()
                                            .filter(p ->  !mountainPositions.contains(p)
                                                       && !roadPositions.contains(p))
                                            .toList();

    if (!possibleMoves.isEmpty()) {
      position = possibleMoves.get(random.nextInt(possibleMoves.size()));
    }

    // Ajouter l’ancienne et la nouvelle position dans la liste modifiée
    modifiedPositions.add(oldPosition);
    modifiedPositions.add(position);

    fire.extinguish(position);

    for (Position p : neighbors.get(position)) {
      if (fire.isOnFire(p)) {
        fire.extinguish(p);
        modifiedPositions.add(p);
      }
    }

    return modifiedPositions;
  }

}
