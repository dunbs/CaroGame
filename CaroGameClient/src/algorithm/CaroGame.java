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

/**
 *
 * @author conqu
 */
public class CaroGame {
    JLabel[][] matrix;

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
        label.setText(" X ");
        label.setFont(new Font("Arial", Font.BOLD, 35));
        return label;
    }

    public void printMatrix() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(1 + " ");
            }
            System.out.println("");
        }
    }

    public JLabel[][] getMatrix() {
        return matrix;
    }
}
