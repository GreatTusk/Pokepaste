/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package guipractice.dragPanel;

import javax.swing.JFrame;

/**
 *
 * @author F776
 */
public class MyFrame extends JFrame {
    DragPanel dragPanel = new DragPanel();
    MyFrame() {
        this.add(dragPanel);
        this.setTitle("Drag & Drop demo");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setSize(600,600);
        this.setVisible(true);
    }
}
