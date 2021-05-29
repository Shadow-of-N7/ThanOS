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
    private VESAGraphics _graphics = new VESAGraphics();
    int col = 0;
    Player player;
    Alien alien;

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
            _isInitialized = true;
            player = new Player(_graphics);
            alien  = new Alien(_graphics);
        }

        switch (gameState) {
            case GameState.READY:
                // TODO
                break;
            case GameState.PLAYING:

                col++;
                for(int y = 0; y < 200; y++) {
                    for(int x = 0; x < 320; x++) {
                        _graphics.setPixel(x, y, col++);
                    }
                }

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
        StaticV24.print(Keyboard.getChar(keyCode));
        if(keyCode == KeyCode.Escape) {
            _graphics.setTextMode();
            _t_state = TaskState.COMPLETED;
        }

        if(gameState == GameState.PLAYING) {
            if(keyCode == KeyCode.ArrowLeft) {
                player.updatePosition(-1);
            }
            if(keyCode == KeyCode.ArrowRight) {
                player.updatePosition(1);
            }
            if(keyCode == KeyCode.Space) {
                player.fire();
            }
        }
    }

    @Override
    public void setState(byte state) {

    }
}
