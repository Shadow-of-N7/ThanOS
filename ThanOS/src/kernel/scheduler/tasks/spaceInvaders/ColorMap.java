package kernel.scheduler.tasks.spaceInvaders;

public class ColorMap {
    /**
     * Draws the color map.
     * @param colors A 2D integer array containing the color definitions.
     * @param xPosition Top left position, X component.
     * @param yPosition Top left position, Y component.
     * @param width The width of the color array.
     * @param height The height of the color array.
     */
    public static void draw(int[][] colors, int xPosition, int yPosition, int width, int height) {
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                DataManager.drawPlane.setPixel(xPosition + x, yPosition + y, colors[y][x]);
            }
        }
    }


    /**
     * Draws the color map.
     * @param colors A 2D integer array containing the color definitions.
     * @param xPosition Top left position, X component.
     * @param yPosition Top left position, Y component.
     * @param width The width of the color array.
     * @param height The height of the color array.
     */
    public static void draw(int[][] colors, float xPosition, float yPosition, int width, int height) {
        draw(colors, (int) xPosition, (int) yPosition, width, height);
    }
}
