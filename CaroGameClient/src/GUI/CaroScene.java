/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import algorithm.CaroGame;
import constant.Constant;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import model.Coordinate;

/**
 *
 * @author conqu
 */
public class CaroScene extends JPanel {

    private int move;
    private CaroGame caroGame;
    private int size;
    private GameGUI gameGUI;

    public CaroScene(int size, GameGUI gameGUI) {
        this.size = size;
        this.gameGUI = gameGUI;
        this.setPreferredSize(getPanelSize());
        this.setLayout(new GridLayout(size, size, Constant.BLOCK_GAP, Constant.BLOCK_GAP));
        this.caroGame = new CaroGame(size);
        this.addBlock();
        move = 0;
    }

    /**
     *
     * @return Half Screen Size
     */
    private Dimension getPanelSize() {
        Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();

        int height = screenDimension.height / 3 * 2 + Constant.BLOCK_GAP * (size - 1);
        int width = screenDimension.width / 3 * 2 + Constant.BLOCK_GAP * (size - 1);
        height = height / constant.Constant.MAX_SIZE * size;
        width = width / constant.Constant.MAX_SIZE * size;

        int finalSize = Math.min(height, width);

        return new Dimension(finalSize, finalSize);
    }

    /**
     * Add {@code JLabel} to screen
     *
     * @param size
     */
    private void addBlock() {
        JLabel[][] matrix = caroGame.getMatrix();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.add(matrix[i][j]);
            }
        }
    }

    public void playerMove(Coordinate coordinate) {
        move++;

        caroGame.playerMove(coordinate);
        if (caroGame.checkWin(coordinate)) {
            gameOver("You win!");
            return;
        }
        
        if (move > size * size) {
            gameOver("Draw!");
            return;
        }
    }

    public void opponentMove(Coordinate coordinate) {
        move++;

        caroGame.playerMove(coordinate);
        if (caroGame.checkWin(coordinate)) {
            gameOver("You lose!");
            return;
        }
        
        if (move >= size * size) {
            gameOver("Draw!");
            return;
        }
    }

    private void gameOver(String message) {
        message += "\n Want to play again?";
        if (JOptionPane.showConfirmDialog(null, message, "Game over", JOptionPane.YES_NO_OPTION)
                == JOptionPane.NO_OPTION) {
            this.gameGUI.disconnect();
            return;
        }
        startNewGame();
    }

    private void startNewGame() {
        caroGame.renewMatrix();
        move = 0;
        this.gameGUI.rematch();
    }
}
