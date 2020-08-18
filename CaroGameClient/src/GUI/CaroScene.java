/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import algorithm.CaroGame;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 *
 * @author conqu
 */
public class CaroScene extends JPanel {

    CaroGame caroGame;

    public CaroScene(int size) {
        this.setSize(getPanelSize());
        this.setLayout(new GridLayout(size, size, 10, 10));
        this.caroGame = new CaroGame(size);
        this.addBlock(size);
        this.setBorder(new LineBorder(Color.yellow));
    }

    /**
     *
     * @return Half Screen Size
     */
    private Dimension getPanelSize() {
        Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();

        int height = screenDimension.height;
        int width = screenDimension.width;

        return new Dimension(width, height);
    }

    /**
     * Add {@code JLabel} to screen
     *
     * @param size
     */
    private void addBlock(int size) {
        JLabel[][] matrix = caroGame.getMatrix();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                this.add(matrix[i][j]);
                System.out.println(matrix[i][j].getFont());
            }
        }
    }

    public static void main(String[] args) {
        CaroScene caroScene = new CaroScene(10);
        caroScene.caroGame.printMatrix();
    }
}
