package kernel.scheduler.tasks.spaceInvaders;

public class Bullet extends GameObject{
    // If true, it is actively used; reserve otherwise
    public boolean isActive;
    private int speed = 2;

    public Bullet() {
        isActive = false;
    }

    @Override
    public void update() {
        positionY -= speed;
        checkCollision();
    }

    @Override
    public void draw() {
        // Only draw if on screen
        if(isActive) {

        }
    }

    public boolean checkCollision() {
        // TODO
        return false;
    }
}
