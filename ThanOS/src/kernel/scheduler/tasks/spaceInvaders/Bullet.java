package kernel.scheduler.tasks.spaceInvaders;

public class Bullet extends GameObject {
    // If true, it is actively used; reserve otherwise
    public GameObject _owner;
    public boolean isActive;
    private final int speed;
    private static final int[][] map = {
            {3},
            {3},
            {3}
    };

    public Bullet(int speed, GameObject owner) {
        _owner = owner;
        isActive = false;
        this.speed = speed;
        width = 1;
        height = 3;
    }

    @Override
    public void update() {
        positionY += speed;
        if (positionY <= 0 || positionY >= DataManager.screenHeight) {
            isActive = false;
            return;
        }
        checkCollision();
    }

    @Override
    public void draw() {
        // Only draw if on screen
        if (isActive) {
            ColorMap.draw(map, positionX, positionY, width, height);
        }
    }

    /**
     * Checks for collisions.
     * This method may look somewhat ugly, but out of missing time, I simply don't care ¯\_(ツ)_/¯
     * @return Whether a collision was detected.
     */
    public boolean checkCollision() {
        // Target: Aliens
        if (_owner instanceof Player) {
            for (int i = 0; i < DataManager.alienManager.alienList.length; i++) {
                if (DataManager.alienManager.alienList[i].isActive
                        && positionX >= DataManager.alienManager.alienList[i].positionX
                        && positionX <= DataManager.alienManager.alienList[i].positionX + DataManager.alienManager.alienList[i].width
                        && positionY >= DataManager.alienManager.alienList[i].positionY
                        && positionY <= DataManager.alienManager.alienList[i].positionY + DataManager.alienManager.alienList[i].height) {
                    DataManager.alienManager.alienList[i].isActive = false;
                    this.isActive = false;
                    return true;
                }
            }
            // Obstacles
            for (int i = 0; i < DataManager.obstacleManager.obstacles.length; i++) {
                if (DataManager.obstacleManager.obstacles[i].isActive
                        && positionX >= DataManager.obstacleManager.obstacles[i].positionX
                        && positionX <= DataManager.obstacleManager.obstacles[i].positionX + Obstacle.width
                        && positionY >= DataManager.obstacleManager.obstacles[i].positionY
                        && positionY <= DataManager.obstacleManager.obstacles[i].positionY + Obstacle.height) {
                    if(DataManager.obstacleManager.obstacles[i].isHit((int)positionX, (int)positionY, speed)) {
                        this.isActive = false;
                    }
                    return true;
                }
            }
        }
        // Target: Player
        if (_owner instanceof Alien) {
            if (positionX >= DataManager.player.positionX
                    && positionX <= DataManager.player.positionX + DataManager.player.width
                    && positionY >= DataManager.player.positionY
                    && positionY <= DataManager.player.positionY + DataManager.player.height) {
                DataManager.player.decreaseHealth(10);
                this.isActive = false;
                return true;
            }
            // Obstacles - 3 Y offset as the lowest point of the bullet counts in this case
            for (int i = 0; i < DataManager.obstacleManager.obstacles.length; i++) {
                if (DataManager.obstacleManager.obstacles[i].isActive
                        && positionX >= DataManager.obstacleManager.obstacles[i].positionX
                        && positionX <= DataManager.obstacleManager.obstacles[i].positionX + Obstacle.width
                        && positionY + 3 >= DataManager.obstacleManager.obstacles[i].positionY
                        && positionY + 3 <= DataManager.obstacleManager.obstacles[i].positionY + Obstacle.height) {
                    if(DataManager.obstacleManager.obstacles[i].isHit((int)positionX, (int)positionY + 3, speed)) {
                        this.isActive = false;
                    }
                    return true;
                }
            }
        }

        return false;
    }
}
