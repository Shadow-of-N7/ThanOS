package kernel.scheduler.tasks.spaceInvaders;

import devices.StaticV24;
import devices.VESAGraphics;

public class ColorMap {
    DrawPlane _graphics;
    private int[][] _colors;
    private final int _width;
    private final int _height;

    public ColorMap(DrawPlane graphics, int width, int height) {
        _graphics = graphics;
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
                _graphics.setPixel(xPosition + x, yPosition + y, _colors[y][x]);
            }
        }
        _graphics.setPixel(xPosition, yPosition, 5);
    }


    public void draw(float xPosition, float yPosition) {
        draw((int) xPosition, (int) yPosition);
    }
}
