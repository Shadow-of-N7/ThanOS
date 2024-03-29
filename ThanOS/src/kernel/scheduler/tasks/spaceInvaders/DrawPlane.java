package kernel.scheduler.tasks.spaceInvaders;

import devices.StaticV24;
import devices.VESAGraphics;

public class DrawPlane {
    int width = DataManager.screenWidth;
    int height = DataManager.screenHeight;
    private final VESAGraphics _graphics;
    DrawPlane otherPlane;

    private final int[][] plane = new int[height][width];

    public DrawPlane(VESAGraphics graphics) {
        _graphics = graphics;
        clear();
    }

    public void setPixel(int x, int y, int col) {
        if(otherPlane.plane[y][x] != col)
            plane[y][x] = col;
    }

    /**
     * Takes a delta of the frame before and the current one and clears everything not black.
     */
    public void clearDelta(DrawPlane oldPlane) {
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                // Delete everything that was not 0 in the old frame and that is 0 in this frame
                if(plane[y][x] == 0 && oldPlane.plane[y][x] == 0 ) { // The second should be != 0, but this works better - no idea why
                    _graphics.setPixel(x, y, 0);
                }
            }
        }
    }

    public void draw() {
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                if (plane[y][x] != 0) {
                    _graphics.setPixel(x, y, plane[y][x]);
                }
            }
        }
    }

    public void clear() {
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                plane[y][x] = 0;
            }
        }
    }
}
