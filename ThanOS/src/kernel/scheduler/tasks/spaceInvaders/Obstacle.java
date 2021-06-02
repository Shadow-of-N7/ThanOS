package kernel.scheduler.tasks.spaceInvaders;

import java.util.Random;

public class Obstacle extends GameObject{
    static int width = 40;
    static int height = 15;
    // Not static, as this should be changeable for each obstacle
    private int[][] map;
    private final Random _random = new Random();

    public Obstacle() {
        positionY = DataManager.screenHeight - 50;
        map = generateMap();
    }

    @Override
    public void update() {

    }

    @Override
    public void draw() {
        ColorMap.draw(map, positionX, positionY, width, height);
    }

    private int [][] generateMap() {
        map = new int[height][width];
        for(int y = 0; y < Obstacle.height; y++) {
            for(int x = 0; x < Obstacle.width; x++) {
                map[y][x] = 7;
            }
        }
        return map;
    }

    public void reset() {
        for(int y = 0; y < Obstacle.height; y++) {
            for(int x = 0; x < Obstacle.width; x++) {
                map[y][x] = 7;
            }
        }
        isActive = true;
    }

    public boolean isHit(int bulletX, int bulletY, int speed) {
        int offsetX = bulletX - (int)positionX;
        int offsetY = bulletY - (int)positionY;

        if(speed > 0) {
            offsetY = getLowestY(offsetX, offsetY);
        }
        else {
            offsetY = getHighestY(offsetX, offsetY);
        }

        // Prevent nullrefs
        if(offsetY < 0) {
            offsetY = 0;
        }
        if(offsetY >= height) {
            offsetY = height - 1;
        }

        if(map[offsetY][offsetX] != 0) {
            map[offsetY][offsetX] = 0;
            damage(offsetX, offsetY);
            return true;
        }
        return false;
    }

    private int getHighestY(int xPos, int yPos) {
        int highest = Obstacle.height;
        for(int y = yPos; y < Obstacle.height; y++) {
            if(map[y][xPos] != 0) {
                highest = y;
            }
        }
        return highest;
    }

    private int getLowestY(int xPos, int yPos) {
        int lowest = 0;
        for(int y = 0; y < yPos; y++) {
            if(map[y][xPos] != 0) {
                return y;
            }
        }
        return lowest;
    }

    private void damage(int x, int y) {
        if(y < Obstacle.height && y >= 0 && x >= 0 && y < Obstacle.width) {
            map[y][x] = 0;
        }
        else return;
        // Above
        if(_random.nextFloat() < 0.1) {
            damage(x, y - 1);
        }
        // Below
        if(_random.nextFloat() < 0.1) {
            damage(x, y + 1);
        }
        // Left
        if(_random.nextFloat() < 0.3) {
            damage(x - 1, y);
        }
        // Right
        if(_random.nextFloat() < 0.3) {
            damage(x + 1, y);
        }
    }
}
