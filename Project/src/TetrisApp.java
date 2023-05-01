import javax.swing.*;
import java.awt.event.ActionListener;

public class TetrisApp extends JFrame {


    Tetris tetris = new Tetris();
    ActionListener NewGameAction = e -> {
        TetrisApp.this.tetris.Initial();
    };
    ActionListener PauseAction = e -> {
        TetrisApp.this.tetris.SetPause(true);
    };
    ActionListener ContinueAction = e -> {
        TetrisApp.this.tetris.SetPause(false);
    };
    ActionListener ExitAction = e -> {
        System.exit(0);
    };
    ActionListener v4Action = e -> {
        TetrisApp.this.tetris.SetMode("v4");
    };
    ActionListener v6Action = e -> {
        TetrisApp.this.tetris.SetMode("v6");
    };
    public TetrisApp() {
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 660);
        this.setTitle("Tetris");
        this.setResizable(false);

        JMenuBar menu = new JMenuBar();
        this.setJMenuBar(menu);
        JMenu gameMenu = new JMenu("Game");
        JMenuItem newGameItem = gameMenu.add("New");
        newGameItem.addActionListener(this.NewGameAction);
        JMenuItem pauseItem = gameMenu.add("Pause");
        pauseItem.addActionListener(this.PauseAction);
        JMenuItem continueItem = gameMenu.add("Resume");
        continueItem.addActionListener(this.ContinueAction);
        JMenuItem exitItem = gameMenu.add("Exit");
        exitItem.addActionListener(this.ExitAction);
        JMenu modeMenu = new JMenu("Size");
        JMenuItem v4Item = modeMenu.add("4 units");
        v4Item.addActionListener(this.v4Action);
        JMenuItem v6Item = modeMenu.add("6 units");
        v6Item.addActionListener(this.v6Action);

        menu.add(gameMenu);
        menu.add(modeMenu);

        this.add(this.tetris);
        this.tetris.setFocusable(true);
    }

    static public void main(String... args) {
        TetrisApp tetrisApp = new TetrisApp();
        tetrisApp.setVisible(true);
    }
}
