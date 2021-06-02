package kernel.scheduler.tasks.spaceInvaders;

public class ColorMap {
    private int[][] _colors;
    private final int _width;
    private final int _height;

    public ColorMap( int width, int height) {
        _colors = new int[width][height];
        _width = width;
        _height = height;
    }


    public void setColors(int[][] colors) {
        _colors = colors;
    }

    /**
     * Draws the color map.
     * @param xPosition Top left position, X component.
     * @param yPosition Top left position, Y component.
     */
    public void draw(int xPosition, int yPosition) {
        for(int x = 0; x < _width; x++) {
            for(int y = 0; y < _height; y++) {
                DataManager.drawPlane.setPixel(xPosition + x, yPosition + y, _colors[y][x]);
            }
        }
        DataManager.drawPlane.setPixel(xPosition, yPosition, 5);
    }


    public void draw(float xPosition, float yPosition) {
        draw((int) xPosition, (int) yPosition);
    }
}
