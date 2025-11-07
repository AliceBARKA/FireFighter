package model;

import util.Position;
import util.TargetStrategy;
import java.util.*;

public class FirefighterBoard implements Board<List<ModelElement>> {
  private final int columnCount;
  private final int rowCount;
  private final int initialFireCount;
  private final int initialFirefighterCount;
  private final TargetStrategy targetStrategy = new TargetStrategy();
  private final Position[][] positions;
  private final Random randomGenerator = new Random();
  private int step = 0;

  private List<Position> firefighterPositions;
  private Fire fire;
  private Map<Position, List<Position>> neighbors = new HashMap<>();

  public FirefighterBoard(int columnCount, int rowCount, int initialFireCount, int initialFirefighterCount) {
    this.columnCount = columnCount;
    this.rowCount = rowCount;
    this.initialFireCount = initialFireCount;
    this.initialFirefighterCount = initialFirefighterCount;
    this.positions = new Position[rowCount][columnCount];

    // Initialisation des positions
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
    firefighterPositions = new ArrayList<>();
    Set<Position> firePositions = new HashSet<>();

    for (int i = 0; i < initialFireCount; i++)
      firePositions.add(randomPosition());
    for (int i = 0; i < initialFirefighterCount; i++)
      firefighterPositions.add(randomPosition());

    this.fire = new Fire(firePositions);
    this.step = 0;
  }

  private Position randomPosition() {
    return new Position(randomGenerator.nextInt(rowCount), randomGenerator.nextInt(columnCount));
  }

  @Override
  public List<ModelElement> getState(Position position) {
    List<ModelElement> result = new ArrayList<>();
    for (Position firefighterPosition : firefighterPositions)
      if (firefighterPosition.equals(position))
        result.add(ModelElement.FIREFIGHTER);

    if (fire.isOnFire(position))
      result.add(ModelElement.FIRE);

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
    firefighterPositions.remove(position);

    for (ModelElement element : state) {
      switch (element) {
        case FIRE -> fire.getFirePositions().add(position);
        case FIREFIGHTER -> firefighterPositions.add(position);
      }
    }
  }

  public List<Position> updateToNextGeneration() {
    List<Position> modifiedPositions = updateFirefighters();
    modifiedPositions.addAll(fire.spread(step, neighbors));
    step++;
    return modifiedPositions;
  }

  private List<Position> updateFirefighters() {
    List<Position> modifiedPosition = new ArrayList<>();
    List<Position> firefighterNewPositions = new ArrayList<>();

    for (Position firefighterPosition : firefighterPositions) {
      Position newFirefighterPosition =
              targetStrategy.neighborClosestToFire(firefighterPosition,
                      fire.getFirePositions(), neighbors);
      firefighterNewPositions.add(newFirefighterPosition);

      // éteindre le feu sur la position du pompier
      fire.extinguish(newFirefighterPosition);

      modifiedPosition.add(firefighterPosition);
      modifiedPosition.add(newFirefighterPosition);

      // éteindre les feux autour
      List<Position> neighborFirePositions = neighbors.get(newFirefighterPosition).stream()
              .filter(fire.getFirePositions()::contains).toList();
      for (Position firePosition : neighborFirePositions)
        fire.extinguish(firePosition);
      modifiedPosition.addAll(neighborFirePositions);
    }

    firefighterPositions = firefighterNewPositions;
    return modifiedPosition;
  }
}
