package pmn.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import gt.component.ComponentCreator;
import gt.component.GamePanel;
import gt.component.MainFrame;
import gt.drawable.GameState;
import gt.drawable.UserInput;

public class PascalMain {
    private static final String TITLE = "Pascal's Triangle (mod n)";

    public static void main(String[] args) {
        ComponentCreator.setCrossPlatformLookAndFeel();

        GamePanel mainPanel = new GamePanel("Pascal", new PascalGameState());
        mainPanel.setPreferredSize(new Dimension(ComponentCreator.DEFAULT_WIDTH, ComponentCreator.DEFAULT_HEIGHT));

        MainFrame mainFrame = new MainFrame(TITLE, mainPanel);

        mainFrame.show(mainPanel::addToGameLoop);
    }

    private static class PascalGameState implements GameState {
        private int[][] pascalsTriangle = new int[0][];
        private int mod = 2;

        int width = ComponentCreator.DEFAULT_WIDTH;
        int height = ComponentCreator.DEFAULT_HEIGHT;

        @Override
        public void drawOn(Graphics2D graphics) {
            graphics.setColor(ComponentCreator.backgroundColor());
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(ComponentCreator.foregroundColor());
            int modTemp = mod;
            graphics.drawString("n = " + modTemp, 10, 20);
            for (int y = 0; y < pascalsTriangle.length; ++y) {
                int[] row = pascalsTriangle[y];
                int rowLength = row.length;
                for (int x = 0; x < rowLength; ++x) {
                    int n = row[x];
                    int xCoord = (width - rowLength) / 2 + x;
                    graphics.setColor(getColor(n));
                    graphics.drawLine(xCoord, y, xCoord, y);
                }
            }
        }

        private static Color getColor(int nMod) {
            if (nMod == 0) {
                return Color.GREEN;
            }
            return Color.BLACK;
        }

        @Override
        public void componentResized(int width, int height) {
            this.width = width;
            this.height = height;
            recreateTriangle();
        }

        private void recreateTriangle() {
            // 0 1 0
            //  \|\|
            // 0 1 1 0
            //  \|\|\|
            // 0 1 2 1 0
            // ...
            int[][] pascalsTriangleNew = new int[height][];
            pascalsTriangleNew[0] = new int[] { 1 };
            for (int n = 1; n < height; ++n) {
                int[] prevRow = pascalsTriangleNew[n - 1];
                int[] row = new int[n + 1];
                for (int k = 0; k < n + 1; ++k) {
                    int left = k == 0 ? 0 : prevRow[k - 1];
                    int right = k == n ? 0 : prevRow[k];
                    int element = left + right;
                    if (element >= mod) {
                        element -= mod;
                    }
                    row[k] = element;
                }
                pascalsTriangleNew[n] = row;
            }
            pascalsTriangle = pascalsTriangleNew;
        }

        @Override
        public void handleUserInput(UserInput input) {
            if (input == UserInput.LEFT_BUTTON_RELEASED) {
                ++mod;
                recreateTriangle();
            } else if (input == UserInput.RIGHT_BUTTON_RELEASED && mod > 2) {
                --mod;
                recreateTriangle();
            }
        }
    }
}
