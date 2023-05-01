import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public class Tetris extends JPanel {

    private static final int BlockSize = 15;
    private static final int BlockWidth = 24;
    private static final int BlockHeight = 39;
    private static final int TimeDelay = 1000;

    private static final String[] next = {
            "Next"
    };
    static boolean[][][] Shape = BlockV4.Shape;
    private final boolean[][] BlockMap = new boolean[BlockHeight][BlockWidth];
    private final Timer timer;
    private int Score = 0;
    private boolean IsPause = false;
    private Point NowBlockPos;
    private boolean[][] NowBlockMap;
    java.awt.event.KeyListener KeyListener = new java.awt.event.KeyListener() {

        @Override
        public void keyPressed(KeyEvent e) {
            if (!IsPause) {
                Point DesPoint;
				switch (e.getKeyCode()) {
					case KeyEvent.VK_DOWN -> {
						DesPoint = new Point(Tetris.this.NowBlockPos.x, Tetris.this.NowBlockPos.y + 1);
						if (!Tetris.this.IsTouch(Tetris.this.NowBlockMap, DesPoint)) {
							Tetris.this.NowBlockPos = DesPoint;
						}
					}
					case KeyEvent.VK_UP -> {
						boolean[][] TurnBlock = Tetris.this.RotateBlock(Tetris.this.NowBlockMap, 1);
						if (!Tetris.this.IsTouch(TurnBlock, Tetris.this.NowBlockPos)) {
							Tetris.this.NowBlockMap = TurnBlock;
						}
					}
					case KeyEvent.VK_RIGHT -> {
						DesPoint = new Point(Tetris.this.NowBlockPos.x + 1, Tetris.this.NowBlockPos.y);
						if (!Tetris.this.IsTouch(Tetris.this.NowBlockMap, DesPoint)) {
							Tetris.this.NowBlockPos = DesPoint;
						}
					}
					case KeyEvent.VK_LEFT -> {
						DesPoint = new Point(Tetris.this.NowBlockPos.x - 1, Tetris.this.NowBlockPos.y);
						if (!Tetris.this.IsTouch(Tetris.this.NowBlockMap, DesPoint)) {
							Tetris.this.NowBlockPos = DesPoint;
						}
					}
				}

                repaint();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

    };
    private boolean[][] NextBlockMap;
    private int NextBlockState;
    private int NowBlockState;
    ActionListener TimerListener = arg0 -> {
        if (Tetris.this.IsTouch(Tetris.this.NowBlockMap, new Point(Tetris.this.NowBlockPos.x, Tetris.this.NowBlockPos.y + 1))) {
            if (Tetris.this.FixBlock()) {
                Tetris.this.Score += Tetris.this.ClearLines() * 10;
                Tetris.this.getNextBlock();
            } else {
                JOptionPane.showMessageDialog(Tetris.this.getParent(), "GAME OVER");
                Tetris.this.Initial();
            }
        } else {
            Tetris.this.NowBlockPos.y++;
        }
        Tetris.this.repaint();
    };

    public Tetris() {
        this.Initial();
        timer = new Timer(Tetris.TimeDelay, this.TimerListener);
        timer.start();
        this.addKeyListener(this.KeyListener);
    }

    static public void main(String... args) {
        boolean[][] SrcMap = Tetris.Shape[3];
        Tetris.ShowMap(SrcMap);


        Tetris tetris = new Tetris();
        boolean[][] result = tetris.RotateBlock(SrcMap, 1);
        Tetris.ShowMap(result);

    }

    static private void ShowMap(boolean[][] SrcMap) {
        System.out.println("-----");
        for (boolean[] booleans : SrcMap) {
            for (boolean aBoolean : booleans) {
                if (aBoolean)
                    System.out.print("*");
                else
                    System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println("-----");
    }

    public void SetMode(String mode) {
        if (mode.equals("v6")) {
            Tetris.Shape = BlockV6.Shape;
        } else {
            Tetris.Shape = BlockV4.Shape;
        }
        this.Initial();
        this.repaint();
    }

    private void getNextBlock() {
        this.NowBlockState = this.NextBlockState;
        this.NowBlockMap = this.NextBlockMap;
        this.NextBlockState = this.CreateNewBlockState();
        this.NextBlockMap = this.getBlockMap(NextBlockState);
        this.NowBlockPos = this.CalNewBlockInitPos();
    }

    private boolean IsTouch(boolean[][] SrcNextBlockMap, Point SrcNextBlockPos) {
        for (int i = 0; i < SrcNextBlockMap.length; i++) {
            for (int j = 0; j < SrcNextBlockMap[i].length; j++) {
                if (SrcNextBlockMap[i][j]) {
                    if (SrcNextBlockPos.y + i >= Tetris.BlockHeight || SrcNextBlockPos.x + j < 0 || SrcNextBlockPos.x + j >= Tetris.BlockWidth) {
                        return true;
                    } else {
                        if (SrcNextBlockPos.y + i >= 0) {
							if (this.BlockMap[SrcNextBlockPos.y + i][SrcNextBlockPos.x + j]) {
                                return true;
                            }
						} else {

                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean FixBlock() {
        for (int i = 0; i < this.NowBlockMap.length; i++) {
            for (int j = 0; j < this.NowBlockMap[i].length; j++) {
                if (this.NowBlockMap[i][j])
                    if (this.NowBlockPos.y + i < 0)
                        return false;
                    else
                        this.BlockMap[this.NowBlockPos.y + i][this.NowBlockPos.x + j] = this.NowBlockMap[i][j];
            }
        }
        return true;
    }

    private Point CalNewBlockInitPos() {
        return new Point(Tetris.BlockWidth / 2 - this.NowBlockMap[0].length / 2, -this.NowBlockMap.length);
    }

    public void Initial() {
        for (int i = 0; i < this.BlockMap.length; i++) {
            Arrays.fill(this.BlockMap[i], false);
        }
        this.Score = 0;
        this.NowBlockState = this.CreateNewBlockState();
        this.NowBlockMap = this.getBlockMap(this.NowBlockState);
        this.NextBlockState = this.CreateNewBlockState();
        this.NextBlockMap = this.getBlockMap(this.NextBlockState);
        this.NowBlockPos = this.CalNewBlockInitPos();
        this.repaint();
    }

    public void SetPause(boolean value) {
        this.IsPause = value;
        if (this.IsPause) {
            this.timer.stop();
        } else {
            this.timer.restart();
        }
        this.repaint();
    }

    private int CreateNewBlockState() {
        int Sum = Tetris.Shape.length * 4;
        return (int) (Math.random() * 1000) % Sum;
    }

    private boolean[][] getBlockMap(int BlockState) {
        int Shape = BlockState / 4;
        int Arc = BlockState % 4;
        System.out.println(BlockState + "," + Shape + "," + Arc);
        return this.RotateBlock(Tetris.Shape[Shape], Arc);
    }

    private boolean[][] RotateBlock(boolean[][] shape, int time) {
        if (time == 0) {
            return shape;
        }
        int height = shape.length;
        int width = shape[0].length;
        boolean[][] ResultMap = new boolean[height][width];
        int tmpH = height - 1, tmpW = 0;
        for (int i = 0; i < height && tmpW < width; i++) {
            for (int j = 0; j < width && tmpH > -1; j++) {
                ResultMap[i][j] = shape[tmpH][tmpW];
                tmpH--;
            }
            tmpH = height - 1;
            tmpW++;
        }
        for (int i = 1; i < time; i++) {
            ResultMap = RotateBlock(ResultMap, 0);
        }
        return ResultMap;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < Tetris.BlockHeight + 1; i++) {
            g.drawRect(0, i * Tetris.BlockSize, Tetris.BlockSize, Tetris.BlockSize);
            g.drawRect((Tetris.BlockWidth + 1) * Tetris.BlockSize, i * Tetris.BlockSize, Tetris.BlockSize,
                    Tetris.BlockSize);
        }
        for (int i = 0; i < Tetris.BlockWidth; i++) {
            g.drawRect((1 + i) * Tetris.BlockSize, Tetris.BlockHeight * Tetris.BlockSize, Tetris.BlockSize,
                    Tetris.BlockSize);
        }

        for (int i = 0; i < this.NowBlockMap.length; i++) {
            for (int j = 0; j < this.NowBlockMap[i].length; j++) {
                if (this.NowBlockMap[i][j])
                    g.fillRect((1 + this.NowBlockPos.x + j) * Tetris.BlockSize, (this.NowBlockPos.y + i) * Tetris.BlockSize,
                            Tetris.BlockSize, Tetris.BlockSize);
            }
        }

        for (int i = 0; i < Tetris.BlockHeight; i++) {
            for (int j = 0; j < Tetris.BlockWidth; j++) {
                if (this.BlockMap[i][j])
                    g.fillRect(Tetris.BlockSize + j * Tetris.BlockSize, i * Tetris.BlockSize, Tetris.BlockSize,
                            Tetris.BlockSize);
            }
        }

        for (int i = 0; i < this.NextBlockMap.length; i++) {
            for (int j = 0; j < this.NextBlockMap[i].length; j++) {
                if (this.NextBlockMap[i][j])
                    g.fillRect(410 + j * Tetris.BlockSize, 30 + i * Tetris.BlockSize, Tetris.BlockSize, Tetris.BlockSize);
            }
        }

        g.drawString("Score : " + this.Score, 410, 20);
        for (int i = 0; i < Tetris.next.length; i++) {
            g.drawString(Tetris.next[i], 410, 100 + i * 20);
        }

        if (this.IsPause) {
            g.setColor(Color.white);
            g.fillRect(70, 100, 50, 20);
            g.setColor(Color.black);
            g.drawRect(70, 100, 50, 20);
            g.drawString("PAUSE", 75, 113);
        }
    }

    private int ClearLines() {
        int lines = 0;
        for (int i = 0; i < this.BlockMap.length; i++) {
            boolean IsLine = true;
            for (int j = 0; j < this.BlockMap[i].length; j++) {
                if (!this.BlockMap[i][j]) {
                    IsLine = false;
                    break;
                }
            }
            if (IsLine) {
                for (int k = i; k > 0; k--) {
                    this.BlockMap[k] = this.BlockMap[k - 1];
                }
                this.BlockMap[0] = new boolean[Tetris.BlockWidth];
                lines++;
            }
        }
        return lines;
    }
}
