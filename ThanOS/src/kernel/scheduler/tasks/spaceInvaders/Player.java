package kernel.scheduler.tasks.spaceInvaders;

public class Player extends GameObject{
    private Bullet[] bulletPool;
    private int bulletIterator = 0;
    private static final int[][] map = {
            {0, 0, 0, 0, 0, 7, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 7, 3, 7, 0, 0, 0, 0},
            {7, 0, 0, 7, 7, 3, 7, 7, 0, 0, 7},
            {7, 0, 7, 7, 7, 7, 7, 7, 7, 0, 7},
            {7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7},
            {7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7},
            {0, 8, 7, 7, 8, 8, 8, 7, 7, 8, 0},
            {0, 8, 8, 8, 8, 0, 8, 8, 8, 8, 0},
    };
    public static final int baseHealth = 100;
    public int health = 100;
    public boolean isGodMode = false;

    public Player() {
        width = 11;
        height = 8;
        positionX = (int)(DataManager.screenWidth / 2);
        positionY = DataManager.screenHeight - 20;
        bulletPool = new Bullet[20];
        for(int i = 0; i < bulletPool.length; i++) {
            bulletPool[i] = new Bullet(-5, this);
        }
        // TODO: Colormap
    }

    /**
     * Offsets the player position by the given amount. Negative means offset to the left.
     * @param offset The offset to apply to the player.
     */
    public void updatePosition(int offset) {
        if(positionX + offset > 0 && positionX + offset + width < DataManager.screenWidth) {
            positionX += offset;
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
        isActive = true;
        positionX = (int)(DataManager.screenWidth / 2);
        for(Bullet bullet : bulletPool) {
            bullet.isActive = false;
        }
        health = Player.baseHealth;
    }

    public void decreaseHealth(int amount) {
        if (isGodMode) return;
        health -= amount;
        if(health <= 0) {
            isActive = false;
        }
    }
}
