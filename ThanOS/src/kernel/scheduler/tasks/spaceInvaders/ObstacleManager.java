package kernel.scheduler.tasks.spaceInvaders;

import devices.StaticV24;

public class ObstacleManager {
    public Obstacle[] obstacles = new Obstacle[3];

    public ObstacleManager() {
        for(int i = 0; i < obstacles.length; i++) {
            obstacles[i] = new Obstacle();
        }
        placeObstacles();
    }

    private void placeObstacles() {
        int stepSize = DataManager.screenWidth / (obstacles.length + 1);
        StaticV24.println(stepSize);
        int xPlace = stepSize;
        for(int i = 0; i < obstacles.length; i++) {
            obstacles[i].positionX = xPlace - (int)(Obstacle.width / 2);
            xPlace += stepSize;
        }
    }

    public void reset() {
        for(Obstacle obstacle : obstacles) {
            obstacle.reset();
        }
    }

    public void update() {

    }

    public void draw() {
        for(Obstacle obstacle : obstacles) {
            obstacle.draw();
        }
    }
}
