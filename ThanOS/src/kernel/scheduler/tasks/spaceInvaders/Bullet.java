package kernel.scheduler.tasks.spaceInvaders;

import devices.StaticV24;

public class Bullet extends GameObject{
    // If true, it is actively used; reserve otherwise
    public boolean isActive;
    private int speed;
    private static final int[][] map = {
            {3},
            {3},
            {3}
    };

    public Bullet(int speed) {
        isActive = false;
        this.speed = speed;
        width = 1;
        height = 3;
    }

    @Override
    public void update() {
        positionY += speed;
        if(positionY <= 0 || positionY >= DataManager.screenHeight) {
            isActive = false;
            return;
        }
        checkCollision();
    }

    @Override
    public void draw() {
        // Only draw if on screen
        if(isActive) {
            ColorMap.draw(map, positionX, positionY, width, height);
        }
    }

    public boolean checkCollision() {
        // TODO
        return false;
    }
}
