package model;

import util.Position;

import java.util.List;
import java.util.Map;

public interface Actor {
    Position getPosition();
    void setPosition(Position position);
    ModelElement getModelElement();

    List<Position>act(Fire fire, Map<Position,List<Position>>neighbors,TerrainPolicy terrainPolicy);


}
