package kernel.scheduler.tasks.spaceInvaders;

import devices.VESAGraphics;

public class Alien extends GameObject {
    private ColorMap _colorMap;
    VESAGraphics _graphics;

    public Alien(VESAGraphics graphics) {
        width = 11;
        height = 8;
        _graphics = graphics;
        _colorMap = new ColorMap(graphics, width, height);
        /*
        int[][] map = {
                {0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0},
                {0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1},
                {1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1},
                {0, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0},
        };

         */
        //_colorMap.setColors(map);
    }

    @Override
    public void update() {

    }

    @Override
    public void draw() {

    }
}
