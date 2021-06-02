package kernel.scheduler.tasks.spaceInvaders;

public class DataManager {
    public static int screenWidth = 320;
    public static int screenHeight = 200;
    public static  DrawPlane drawPlane;


    public static void setDrawPlane(DrawPlane plane) {
        drawPlane = plane;
    }
}
