/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package guipractice;

import java.awt.Color;
import java.awt.Font;


import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 *
 * @author F776
 */
public class GUIPractice {

    /**
    
        // TODO code application logic here
        Border border = BorderFactory.createLineBorder(Color.red,3);
        
        JLabel label = new JLabel("Great Tusk");
        label.setText("Great Tusk");
        label.setHorizontalTextPosition(JLabel.CENTER); //LEFT CENTER RIGHT OF imageicon
        label.setVerticalTextPosition(JLabel.TOP); // TOP CENTER OR BOTTOM OF IMAGE ICON
        label.setForeground(Color.white);
        label.setFont(new Font("MV Boli", Font.PLAIN,40));
        label.setIconTextGap(-90); //set gap of text to image
        label.setBackground(Color.black); // set background color
        label.setOpaque(true);
        label.setBorder(border);
        label.setVerticalAlignment(JLabel.CENTER); // ICON+TEXT WITHIN LABEL vertical
        label.setHorizontalAlignment(JLabel.CENTER); //horizontal
        label.setBounds(0, 0, 950, 950); // set x,y position within frame as well as dimensions
        
        
        
        
        MyFrame frame = new MyFrame();
        //frame.add(label);
        ImageIcon image = new ImageIcon("great_tusk_icon.png");
        label.setIcon(image);
        frame.setLayout(null);
        
        
        JPanel redPanel = new JPanel();
        redPanel.setBackground(Color.red);
        //redPanel.setBounds(JPanel.CENTER_ALIGNMENT,JPanel.CENTER_ALIGNMENT , 250, 250);
        
        JPanel bluePanel = new JPanel();
        redPanel.setBackground(Color.blue);
        redPanel.setBounds(500, 0, 500, 250);
        
        frame.add(bluePanel);
        frame.add(redPanel);
        
        **/
    
    
}
