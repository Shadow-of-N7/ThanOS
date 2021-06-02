package kernel.scheduler.tasks.spaceInvaders;

public class AlienManager {
    private int rows = 1;
    private int columns = 2;
    private final Alien[][] aliens = new Alien[rows][columns];

    public AlienManager() {
        for(int y = 0; y < rows; y++) {
            for(int x = 0; x < columns; x++) {
                aliens[y][x] = new Alien();
            }
            // Set the aliens to appropriate positions
            placeAliens(aliens[y]);
        }
    }

    public void update() {
        for(int y = 0; y < rows; y++) {
            Alien lastInRow = getLastInRow(aliens[y]);
            if(aliens[y][0].positionX - aliens[y][0].xSpeed <= 1
                    || lastInRow.positionX + lastInRow.width + lastInRow.xSpeed >= DataManager.screenWidth - 1) {
                changeRowDirection(aliens[y]);
            }
            for(int x = 0; x < columns; x++) {
                aliens[y][x].update();
            }
        }
    }

    public void draw() {
        for (Alien[] alienColumns : aliens) {
            for(Alien alien : alienColumns) {
                alien.draw();
            }
        }
    }

    /**
     * Gets the last alien in a row.
     * @param aliens The row of aliens.
     * @return THe last (most right) alien.
     */
    private Alien getLastInRow(Alien[] aliens) {
        Alien currentLastAlien = aliens[0];
        for(Alien alien : aliens) {
            if(alien.isActive) {
                currentLastAlien = alien;
            }
        }
        return currentLastAlien;
    }


    /**
     * Changes the movement direction of an entire alien row.
     * @param aliens The alien row.
     */
    private void changeRowDirection(Alien[] aliens) {
        for(Alien alien : aliens) {
            if(alien.isActive) {
                alien.movementDirection =! alien.movementDirection;
            }
        }
    }


    private void placeAliens(Alien[] aliens) {
        int stepSize = DataManager.screenWidth / (columns + 1);
        int xPlace = stepSize;
        for(int i = 0; i < columns; i++) {
            aliens[i].positionX = xPlace;
            xPlace += stepSize;
        }
    }
}
