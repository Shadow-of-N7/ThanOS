package kernel.scheduler.tasks.spaceInvaders;

import devices.VESAGraphics;

public class Player extends GameObject{
    private ColorMap _colorMap;
    DrawPlane _graphics;
    Bullet[] bulletPool;
    int bulletIterator = 0;

    public Player(DrawPlane graphics) {
        _graphics = graphics;
        bulletPool = new Bullet[20];

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
        bulletPool[bulletIterator].positionX = positionX + (width / 2) - (bulletPool[bulletIterator].width / 2);
        bulletPool[bulletIterator++].positionY = positionY;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw() {

    }

    private void updateBullets() {
        for(int i = 0; i < bulletPool.length; i++) {
            // Only update active bullets
            if(bulletPool[i].isActive) {
                bulletPool[i].update();
            }
        }
    }
}
