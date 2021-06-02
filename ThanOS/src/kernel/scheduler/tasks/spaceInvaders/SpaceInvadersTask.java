package kernel.scheduler.tasks.spaceInvaders;

import devices.KeyCode;
import devices.StaticV24;
import devices.VESAGraphics;
import io.Console;
import kernel.scheduler.Scheduler;
import kernel.scheduler.Task;
import kernel.scheduler.TaskState;

public class SpaceInvadersTask extends Task {
    private boolean _isInitialized = false;
    private boolean _isWinInitialized = false;
    private boolean _isLooseInitialized = false;
    private byte gameState = GameState.PLAYING;
    private final VESAGraphics _graphics = new VESAGraphics();
    private final DrawPlane[] _drawPlanes = new DrawPlane[2];
    private DrawPlane _currentDrawPlane;
    private DrawPlane _oldDrawPlane;

    @Override
    public void run() {
        if (!_isInitialized) {
            _t_fullScreen = true;
            _graphics.setGraphics320x200Mode();
            Scheduler.redirectKeyboardInput = true;
            _drawPlanes[0] = new DrawPlane(_graphics);
            _drawPlanes[1] = new DrawPlane(_graphics);
            _drawPlanes[0].otherPlane = _drawPlanes[1];
            _drawPlanes[1].otherPlane = _drawPlanes[0];
            _currentDrawPlane = _drawPlanes[0];
            DataManager.drawPlane = _currentDrawPlane;

            _isInitialized = true;
            DataManager.player = new Player();
            DataManager.alienManager = new AlienManager();
            DataManager.obstacleManager = new ObstacleManager();
        }

        switch (gameState) {
            case GameState.READY:
                // TODO
                break;
            case GameState.PLAYING:
                // Frame preparation

                switchDrawPlanes();
                DataManager.setDrawPlane(_currentDrawPlane);

                // Update step

                DataManager.alienManager.update();
                DataManager.player.update();
                DataManager.obstacleManager.update();

                // Draw step

                DataManager.alienManager.draw();
                DataManager.player.draw();
                DataManager.obstacleManager.draw();
                displayHealth();

                // Flush to screen

                _currentDrawPlane.clearDelta(_oldDrawPlane);
                _currentDrawPlane.draw();
                if (DataManager.alienManager.getActiveAlienCount() == 0) {
                    gameState = GameState.WIN;
                }
                if(!DataManager.player.isActive) {
                    gameState = GameState.DEAD;
                }
                break;
            case GameState.DEAD:
                if(!_isLooseInitialized) {
                    _graphics.setTextMode();
                    Console.disableCursor();
                    Console.setCaret((Console.SCREEN_WIDTH / 2) - 5, (Console.SCREEN_HEIGHT / 2) - 1);
                    Console.setColor(Console.ConsoleColor.Red);
                    Console.println("!You loose!");
                    Console.resetColor();
                    Console.setCaret((Console.SCREEN_WIDTH / 2) - 20, (Console.SCREEN_HEIGHT / 2));
                    Console.println("Press ENTER to play again or ESC to quit.");
                    _isLooseInitialized = true;
                }
                break;
            case GameState.WIN:
                if(!_isWinInitialized) {
                    _graphics.setTextMode();
                    Console.disableCursor();
                    Console.setCaret((Console.SCREEN_WIDTH / 2) - 4, (Console.SCREEN_HEIGHT / 2) - 1);
                    Console.setColor(Console.ConsoleColor.Green);
                    Console.println("!You win!");
                    Console.resetColor();
                    Console.setCaret((Console.SCREEN_WIDTH / 2) - 20, (Console.SCREEN_HEIGHT / 2));
                    Console.println("Press ENTER to play again or ESC to quit.");
                    _isWinInitialized = true;
                }
                break;
        }
    }

    /**
     * Clears the screen.
     */
    public void clear() {
        for (int y = 0; y < 200; y++) {
            for (int x = 0; x < 320; x++) {
                _graphics.setPixel(x, y, 0);
            }
        }
    }

    @Override
    public void takeKeyCode(int keyCode) {
        int offset = 3;
        if (keyCode == KeyCode.Escape) {
            gameState = GameState.OFF;
            _t_state = TaskState.COMPLETED;
            _graphics.setTextMode();
        }

        switch (gameState) {
            case GameState.PLAYING:

                if (keyCode == KeyCode.ArrowLeft) {
                    DataManager.player.updatePosition(-offset);
                }
                if (keyCode == KeyCode.ArrowRight) {
                    DataManager.player.updatePosition(offset);
                }
                if (keyCode == KeyCode.Space) {
                    DataManager.player.fire();
                }
                if (keyCode == KeyCode.K + 32) {
                    for(int i = 0; i < DataManager.alienManager.alienList.length; i++) {
                        DataManager.alienManager.alienList[i].isActive = false;
                    }
                }
                break;

            case GameState.DEAD:
            case GameState.WIN:
                if (keyCode == KeyCode.Enter) {
                    gameState = GameState.PLAYING;
                    reset();
                    _isWinInitialized = false;
                    _isLooseInitialized = false;
                }
                break;
        }
    }

    @Override
    public void setState(byte state) {

    }


    private void switchDrawPlanes() {
        if (_currentDrawPlane == _drawPlanes[0]) {
            _currentDrawPlane = _drawPlanes[1];
            _oldDrawPlane = _drawPlanes[0];
        } else {
            _currentDrawPlane = _drawPlanes[0];
            _oldDrawPlane = _drawPlanes[1];
        }
        // Only the new plane shall be cleared, otherwise no delta is possible
        _currentDrawPlane.clear();
    }


    private void reset() {
        StaticV24.println("Resetting...");
        _graphics.setGraphics320x200Mode();
        DataManager.alienManager.reset();
        DataManager.player.reset();
        DataManager.obstacleManager.reset();
    }

    private void displayHealth() {
        int maxLength = DataManager.screenWidth;
        float actualLength = ((float)maxLength / (float)Player.baseHealth) * (float)DataManager.player.health;
        for(int i = 0; i < (int)actualLength; i++) {
            _currentDrawPlane.setPixel(i, DataManager.screenHeight -1, 4);
        }
    }
}
