/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package guipractice;

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 *
 * @author F776
 */
public class MyFrame extends JFrame {
    
    MyFrame(){
    
        this.setTitle("New App");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setSize(1920,1080);
        this.setVisible(true);
        
        ImageIcon image = new ImageIcon("great_tusk_icon.png");
        this.setIconImage(image.getImage()); // change icon of frame
        //this.getContentPane().setBackground(Color.green); //change color of background
        this.getContentPane().setBackground(new Color(123,50,250));
    }
}
