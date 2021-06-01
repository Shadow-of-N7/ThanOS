package kernel.scheduler.tasks.spaceInvaders;

public class Alien extends GameObject {
    private final ColorMap _colorMap;
    DrawPlane _graphics;
    private static final int[][] map = {
            {0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0},
            {0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1},
            {0, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0},
    };
    // false: left, true: right
    private boolean _movementDir = false;
    private static final float xSpeed = 0.01f;

    public Alien(DrawPlane graphics) {
        width = 11;
        height = 8;
        positionX = 50;
        positionY = 50;
        _graphics = graphics;
        _colorMap = new ColorMap(graphics, width, height);
        _colorMap.setColors(map);
    }

    @Override
    public void update() {
        if(positionX - xSpeed <= 1 || positionX + width + xSpeed >= DataManager.screenWidth - 1) {
            _movementDir =! _movementDir;
        }
        if(isActive) {
            // Right
            if(_movementDir) {
                positionX += xSpeed;
            }
            else {
                positionX -= xSpeed;
            }
        }
    }

    @Override
    public void draw() {
        if(isActive) {
            _colorMap.draw(positionX, positionY);
        }
    }
}
