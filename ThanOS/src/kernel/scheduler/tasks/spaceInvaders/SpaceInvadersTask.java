package kernel.scheduler.tasks.spaceInvaders;

import devices.KeyCode;
import devices.Keyboard;
import devices.StaticV24;
import devices.VESAGraphics;
import kernel.scheduler.Scheduler;
import kernel.scheduler.Task;
import kernel.scheduler.TaskState;

public class SpaceInvadersTask extends Task {
    private boolean _isInitialized = false;
    private byte gameState = GameState.PLAYING;
    private final VESAGraphics _graphics = new VESAGraphics();
    private final DrawPlane[] _drawPlanes = new DrawPlane[2];
    private DrawPlane _currentDrawPlane;
    private DrawPlane _oldDrawPlane;
    int col = 0;
    Player player;
    AlienManager alienManager;

    // TODO:
    /*
    Bullet pool from which bullets can be taken
    Alien array
    Player control
    Score
     */

    @Override
    public void run() {
        if(!_isInitialized) {
            _t_fullScreen = true;
            _graphics.setGraphics320x200Mode();
            Scheduler.redirectKeyboardInput = true;
            _drawPlanes[0] = new DrawPlane(_graphics);
            _drawPlanes[1] = new DrawPlane(_graphics);
            _currentDrawPlane = _drawPlanes[0];
            DataManager.drawPlane = _currentDrawPlane;

            _isInitialized = true;
            player = new Player();
            alienManager = new AlienManager();

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

                alienManager.update();
                player.update();

                // Draw step

                alienManager.draw();
                player.draw();

                // Flush to screen

                _currentDrawPlane.clearDelta(_oldDrawPlane);
                _currentDrawPlane.draw();
                break;
            case GameState.DEAD:
                // TODO
                break;
        }
    }

    /**
     * Clears the screen.
     */
    public void clear() {
        for(int y = 0; y < 200; y++) {
            for(int x = 0; x < 320; x++) {
                _graphics.setPixel(x, y, 0);
            }
        }
    }

    @Override
    public void takeKeyCode(int keyCode) {
        int offset = 3;
        if(keyCode == KeyCode.Escape) {
            _graphics.setTextMode();
            _t_state = TaskState.COMPLETED;
        }

        if(gameState == GameState.PLAYING) {
            if(keyCode == KeyCode.ArrowLeft) {
                player.updatePosition(-offset);
            }
            if(keyCode == KeyCode.ArrowRight) {
                player.updatePosition(offset);
            }
            if(keyCode == KeyCode.Space) {
                player.fire();
            }
        }
    }

    @Override
    public void setState(byte state) {

    }


    private void switchDrawPlanes() {
        if(_currentDrawPlane == _drawPlanes[0]) {
            _currentDrawPlane = _drawPlanes[1];
            _oldDrawPlane = _drawPlanes[0];
        }
        else {
            _currentDrawPlane = _drawPlanes[0];
            _oldDrawPlane = _drawPlanes[1];
        }
        // Only the new plane shall be cleared, otherwise no delta is possible
        _currentDrawPlane.clear();
    }
}
