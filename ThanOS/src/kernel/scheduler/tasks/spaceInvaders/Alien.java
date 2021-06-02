package kernel.scheduler.tasks.spaceInvaders;

public class Alien extends GameObject {
    private static final int[][] map = {
            {0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0},
            {0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1},
            {0, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0},
    };
    // false: left, true: right
    private boolean _movementDir = false;
    private static final float xSpeed = 0.2f;
    Bullet[] bulletPool;
    int bulletIterator = 0;

    public Alien() {
        width = 11;
        height = 8;
        positionX = 50;
        positionY = 50;

        bulletPool = new Bullet[20];
        for(int i = 0; i < bulletPool.length; i++) {
            bulletPool[i] = new Bullet(5);
        }
    }

    @Override
    public void update() {
        if(positionX - xSpeed <= 1 || positionX + width + xSpeed >= DataManager.screenWidth - 1) {
            _movementDir =! _movementDir;
        }
        if(isActive) {
            // Right
            if(_movementDir) {
                positionX += xSpeed;
            }
            else {
                positionX -= xSpeed;
            }
        }
    }

    @Override
    public void draw() {
        if(isActive) {
            ColorMap.draw(map, positionX, positionY, width, height);
        }
    }

    /**
     * Fires the primary weapon.
     */
    public void fire() {
        bulletPool[bulletIterator].isActive = true;
        bulletPool[bulletIterator].positionX = positionX + (int)(width / 2) - (int)(bulletPool[bulletIterator].width / 2);
        bulletPool[bulletIterator++].positionY = positionY;
        if(bulletIterator == bulletPool.length) {
            bulletIterator = 0;
        }
    }
}
