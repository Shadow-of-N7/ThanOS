package kernel.scheduler.tasks.spaceInvaders;

public class Player extends GameObject{
    Bullet[] bulletPool;
    int bulletIterator = 0;
    private static final int[][] map = {
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
            {1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0},
    };

    public Player() {
        width = 11;
        height = 8;
        positionX = (int)(DataManager.screenWidth / 2);
        positionY = DataManager.screenHeight - 20;
        bulletPool = new Bullet[20];
        for(int i = 0; i < bulletPool.length; i++) {
            bulletPool[i] = new Bullet(-5);
        }
        // TODO: Colormap
    }

    /**
     * Offsets the player position by the given amount. Negative means offset to the left.
     * @param offset The offset to apply to the player.
     */
    public void updatePosition(int offset) {
        positionX += offset;
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

    @Override
    public void update() {
        updateBullets();
    }

    @Override
    public void draw() {
        drawBullets();
        ColorMap.draw(map, positionX, positionY, width, height);
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