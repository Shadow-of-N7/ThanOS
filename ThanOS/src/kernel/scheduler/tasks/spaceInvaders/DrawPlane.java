package kernel.scheduler.tasks.spaceInvaders;

import devices.VESAGraphics;

public class DrawPlane {
    int width = DataManager.screenWidth;
    int height = DataManager.screenHeight;
    VESAGraphics _graphics;

    private int[] plane = new int[width * height];

    public DrawPlane(VESAGraphics graphics) {
        _graphics = graphics;
        for(int i = 0; i < plane.length; i++) {
            plane[i] = 0;
        }
    }

    public void setPixel(int x, int y, int col) {
        plane[(width * y) + x] = col;
    }

    /**
     * Takes a delta of the frame before and the current one and clears everything not black.
     */
    public void clearDelta(DrawPlane oldPlane) {
        for(int i = 0; i < oldPlane.plane.length; i++) {
            int x = oldPlane.plane.length % oldPlane.height;
            int y = (oldPlane.plane.length - x) / width;
            _graphics.setPixel(x, y, 0);
        }
    }

    public void draw() {
        for(int i = 0; i < plane.length; i++) {
            if(plane[i] != 0) {
                int x = plane.length % height;
                int y = (plane.length - x) / width;
                _graphics.setPixel(x, y, plane[i]);
            }
        }
    }



}
