package pmn.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        private List<List<BigInteger>> pascalsTriangle = new ArrayList<>();
        private BigInteger mod = BigInteger.valueOf(2);

        int width = ComponentCreator.DEFAULT_WIDTH;
        int height = ComponentCreator.DEFAULT_HEIGHT;

        @Override
        public void drawOn(Graphics2D graphics) {
            graphics.setColor(ComponentCreator.backgroundColor());
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(ComponentCreator.foregroundColor());
            BigInteger modTemp = mod;
            graphics.drawString("n: " + modTemp.toString(), 10, 20);
            for (int y = 0; y < pascalsTriangle.size(); ++y) {
                List<BigInteger> row = pascalsTriangle.get(y);
                int rowSize = row.size();
                for (int x = 0; x < rowSize; ++x) {
                    BigInteger n = row.get(x);
                    BigInteger nMod = n.mod(modTemp);
                    int xCoord = (width - rowSize) / 2 + x;
                    graphics.setColor(getColor(nMod.intValue()));
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
            // 0 1 0
            //  \|\|
            // 0 1 1 0
            //  \|\|\|
            // 0 1 2 1 0
            // ...
            List<List<BigInteger>> pascalsTriangleNew = new ArrayList<>();
            pascalsTriangleNew.add(Collections.singletonList(BigInteger.ONE));
            for (int n = 1; n < height; ++n) {
                List<BigInteger> row = new ArrayList<>();
                pascalsTriangleNew.add(row);
                List<BigInteger> prevRow = pascalsTriangleNew.get(n - 1);
                for (int k = 0; k < prevRow.size() + 1; ++k) {
                    BigInteger left = k == 0 ? BigInteger.ZERO : prevRow.get(k - 1);
                    BigInteger right = k == prevRow.size() ? BigInteger.ZERO : prevRow.get(k);
                    row.add(left.add(right));
                }
            }
            pascalsTriangle = pascalsTriangleNew;
        }

        @Override
        public void handleUserInput(UserInput input) {
            if (input == UserInput.LEFT_BUTTON_RELEASED) {
                mod = mod.add(BigInteger.ONE);
            } else if (input == UserInput.RIGHT_BUTTON_RELEASED && !mod.equals(BigInteger.valueOf(2))) {
                mod = mod.subtract(BigInteger.ONE);
            }
        }
    }
}
