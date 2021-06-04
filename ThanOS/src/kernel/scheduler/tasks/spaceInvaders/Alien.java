package kernel.scheduler.tasks.spaceInvaders;

import devices.StaticV24;

public class Alien extends GameObject {
    private static final int[][] map = {
            {0, 0, 4, 0, 0, 0, 0, 0, 4, 0, 0},
            {0, 0, 0, 4, 0, 0, 0, 4, 0, 0, 0},
            {0, 0, 4, 4, 4, 4, 4, 4, 4, 0, 0},
            {0, 4, 4, 0, 4, 4, 4, 0, 4, 4, 0},
            {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4},
            {4, 0, 4, 4, 4, 4, 4, 4, 4, 0, 4},
            {4, 0, 4, 0, 0, 0, 0, 0, 4, 0, 4},
            {0, 0, 0, 4, 4, 0, 4, 4, 0, 0, 0},
    };
    // false: left, true: right
    public boolean movementDirection = false;
    public final float baseXSpeed = 0.2f;
    public float xSpeed = baseXSpeed;
    Bullet[] bulletPool;
    int bulletIterator = 0;
    int fireTolerance = 5;
    public int fireCoolDown = 100;
    public int currentFireCoolDown = 0;

    public Alien() {
        width = 11;
        height = 8;

        bulletPool = new Bullet[20];
        for(int i = 0; i < bulletPool.length; i++) {
            bulletPool[i] = new Bullet(5, this);
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

            if(currentFireCoolDown > 0) {
                --currentFireCoolDown;
            }
            if(currentFireCoolDown <= 0
                    && positionX < DataManager.player.positionX + fireTolerance
                    && positionX > DataManager.player.positionX - fireTolerance) {
                fire();
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
        bulletPool[bulletIterator]._owner = this;
        bulletPool[bulletIterator].positionX = positionX + (int)(width / 2) - (int)(bulletPool[bulletIterator].width / 2);
        bulletPool[bulletIterator++].positionY = positionY;
        currentFireCoolDown = fireCoolDown;
        if(bulletIterator == bulletPool.length) {
            bulletIterator = 0;
        }
    }

    private void updateBullets() {
        for (Bullet bullet : bulletPool) {
            // Only update active bullets
            if (bullet.isActive) {
                bullet.update();
            }
        }
    }

    private void drawBullets() {
        for (Bullet bullet : bulletPool) {
            // Only update active bullets
            if (bullet.isActive) {
                bullet.draw();
            }
        }
    }


    public void reset() {
        for (Bullet bullet : bulletPool) {
            bullet.isActive = false;
        }
        currentFireCoolDown = 0;
        StaticV24.println(fireCoolDown);
    }
}
