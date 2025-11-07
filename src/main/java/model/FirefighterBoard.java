package model;

import util.Position;
import java.util.*;

public class FirefighterBoard implements Board<List<ModelElement>> {
  private final int columnCount;
  private final int rowCount;
  private final int initialFireCount;
  private final int initialFirefighterCount;
  private final Position[][] positions;
  private final Random randomGenerator = new Random();
  private int step = 0;

  private List<Firefighter> firefighters;
  private Fire fire;
  private Map<Position, List<Position>> neighbors = new HashMap<>();

  public FirefighterBoard(int columnCount, int rowCount, int initialFireCount, int initialFirefighterCount) {
    this.columnCount = columnCount;
    this.rowCount = rowCount;
    this.initialFireCount = initialFireCount;
    this.initialFirefighterCount = initialFirefighterCount;
    this.positions = new Position[rowCount][columnCount];

    // Initialisation des positions de la grille
    for (int column = 0; column < columnCount; column++)
      for (int row = 0; row < rowCount; row++)
        positions[row][column] = new Position(row, column);

    // Initialisation des voisins
    for (int column = 0; column < columnCount; column++)
      for (int row = 0; row < rowCount; row++) {
        List<Position> list = new ArrayList<>();
        if (row > 0) list.add(positions[row - 1][column]);
        if (column > 0) list.add(positions[row][column - 1]);
        if (row < rowCount - 1) list.add(positions[row + 1][column]);
        if (column < columnCount - 1) list.add(positions[row][column + 1]);
        neighbors.put(positions[row][column], list);
      }

    initializeElements();
  }

  public void initializeElements() {
    Set<Position> firePositions = new HashSet<>();
    List<Firefighter> firefighterList = new ArrayList<>();

    for (int i = 0; i < initialFireCount; i++)
      firePositions.add(randomPosition());
    for (int i = 0; i < initialFirefighterCount; i++)
      firefighterList.add(new Firefighter(randomPosition()));

    this.fire = new Fire(firePositions);
    this.firefighters = firefighterList;
    this.step = 0;
  }

  private Position randomPosition() {
    return new Position(randomGenerator.nextInt(rowCount), randomGenerator.nextInt(columnCount));
  }

  @Override
  public List<ModelElement> getState(Position position) {
    List<ModelElement> result = new ArrayList<>();
    if (fire.isOnFire(position))
      result.add(ModelElement.FIRE);
    for (Firefighter f : firefighters)
      if (f.getPosition().equals(position))
        result.add(ModelElement.FIREFIGHTER);
    return result;
  }

  @Override
  public int rowCount() {
    return rowCount;
  }

  @Override
  public int columnCount() {
    return columnCount;
  }

  @Override
  public int stepNumber() {
    return step;
  }

  @Override
  public void reset() {
    initializeElements();
  }

  @Override
  public void setState(List<ModelElement> state, Position position) {
    fire.getFirePositions().remove(position);
    for (Firefighter f : firefighters)
      if (f.getPosition().equals(position))
        firefighters.remove(f);

    for (ModelElement element : state) {
      switch (element) {
        case FIRE -> fire.getFirePositions().add(position);
        case FIREFIGHTER -> firefighters.add(new Firefighter(position));
      }
    }
  }

  public List<Position> updateToNextGeneration() {
    List<Position> modified = new ArrayList<>();

    // Les pompiers agissent
    for (Firefighter f : firefighters)
      modified.addAll(f.act(fire, neighbors));

    // Le feu se propage
    modified.addAll(fire.spread(step, neighbors));

    step++;
    return modified;
  }
}