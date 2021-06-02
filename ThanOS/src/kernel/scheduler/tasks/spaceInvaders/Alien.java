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
    public boolean movementDirection = false;
    public final float xSpeed = 0.2f;
    Bullet[] bulletPool;
    int bulletIterator = 0;
    int fireTolerance = 5;
    private final int _fireCoolDown = 100;
    private int _currentFireCoolDown = 0;

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
        if(isActive) {
            // Right
            updateBullets();
            if(movementDirection) {
                positionX += xSpeed;
            }
            else {
                positionX -= xSpeed;
            }

            if(_currentFireCoolDown > 0) {
                --_currentFireCoolDown;
            }
            if(_currentFireCoolDown <= 0
                    && positionX < DataManager.player.positionX + fireTolerance
                    && positionX > DataManager.player.positionX - fireTolerance) {
                fire();
                _currentFireCoolDown = _fireCoolDown;
            }
        }

    }

    @Override
    public void draw() {
        if(isActive) {
            drawBullets();
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

    private void updateBullets() {
        for(int i = 0; i < bulletPool.length; i++) {
            // Only update active bullets
            if(bulletPool[i].isActive) {
                bulletPool[i].update();
            }
        }
    }

    private void drawBullets() {
        for(int i = 0; i < bulletPool.length; i++) {
            // Only update active bullets
            if(bulletPool[i].isActive) {
                bulletPool[i].draw();
            }
        }
    }
}
