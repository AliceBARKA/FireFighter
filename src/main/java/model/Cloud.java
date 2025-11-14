package model;

import util.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

  public List<Position>  act(Fire fire , Map<Position , List<Position>> neighbors) {
    List<Position> modifiedPositions = new ArrayList<>();

    List<Position> possibleMoves = neighbors.get(position);
    if(! possibleMoves.isEmpty()) {
      position = possibleMoves.get( random.nextInt(possibleMoves.size()));
    }
    modifiedPositions.add(position);

    fire.extinguish(position);

    List<Position> around = neighbors.get(position);
    for (Position p : around) {
      if (fire.isOnFire(p)) {
        fire.extinguish(p);
        modifiedPositions.add(p);
      }
    }

    return modifiedPositions;
  }
}
