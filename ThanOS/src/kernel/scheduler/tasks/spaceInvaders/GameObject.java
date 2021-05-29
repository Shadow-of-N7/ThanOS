package kernel.scheduler.tasks.spaceInvaders;

public abstract class GameObject {
    public int positionX;
    public int positionY;
    public int width;
    public int height;

    public abstract void update();
    public abstract void draw();
}
