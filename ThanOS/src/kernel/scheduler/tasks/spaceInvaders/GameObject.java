package kernel.scheduler.tasks.spaceInvaders;

public abstract class GameObject {
    public float positionX;
    public float  positionY;
    public int width;
    public int height;
    public boolean isActive = true;

    public abstract void update();
    public abstract void draw();
}
