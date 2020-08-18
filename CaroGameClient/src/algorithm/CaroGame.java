/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import model.Coordinate;

/**
 *
 * @author conqu
 */
public class CaroGame {
    private JLabel[][] matrix;
    private final int[] xMove = new int[]{1, 1, 1, 0};
    private final int[] yMove = new int[]{-1, 0, 1, 1};

    public CaroGame(int size) {
        matrix = new JLabel[size][size];
        createMatrix();
    }

    private void createMatrix() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                matrix[i][j] = createNewJLabel();
            }
        }
    }

    private JLabel createNewJLabel() {
        JLabel label = new JLabel();
        label.setBorder(new LineBorder(Color.black));
        label.setFont(new Font("Arial", Font.BOLD, 35));
        label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        return label;
    }
    
    public void renewMatrix() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                matrix[i][j].setText("");
            }
        }
    }
    
    public void printMatrix() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(matrix[i][j].getText() + " ");
            }
            System.out.println("");
        }
    }

    public JLabel[][] getMatrix() {
        return matrix;
    }

    public void playerMove(Coordinate coordinate) {
        int x = coordinate.getX();
        int y = coordinate.getY();
        matrix[y][x].setText(coordinate.getPlayer().getOurs());
    }

    public boolean checkWin(Coordinate coordinate) {
        String character = coordinate.getPlayer().getOurs();
        int size = matrix.length;
        for (int i = 0; i < xMove.length; i++) {
            int length = 0;
            int x = coordinate.getY();
            int y = coordinate.getX();
            // Go up
            while (x < size && y < size && x >= 0 && y >= 0
                    && matrix[x][y].getText().equals(character)) {
                length++;
                x += xMove[i];
                y += yMove[i];
            }

            length--;
            x = coordinate.getY();
            y = coordinate.getX();
            //Go down

            while (x < size && y < size && x >= 0 && y >= 0
                    && matrix[x][y].getText().equals(character)) {
                length++;
                x -= xMove[i];
                y -= yMove[i];
            }

            if (length == constant.Constant.WIN_LENGTH) {
                return true;
            }

        }

        return false;
    }
}
