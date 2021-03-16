/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minotaurMaze;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 *
 * @author DNS
 */
class MinotaurMaze extends JFrame {

    /**
     * @param args the command line arguments
     */
    private TestMaze testMaze;
    private JPanel panel;

    private final int COLS = 5;
    private final int ROWS = 5;
    private final int IMAGES_SIZE = 50;

    public static void main(String[] args) {
        new MinotaurMaze().setVisible(true);
    }

    private MinotaurMaze() {
        testMaze = new TestMaze(COLS, ROWS);
        testMaze.startNewMaze();
        setImages();
        initPanel();
        initFrame();
    }

    private void initPanel() {
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Coord coord : Ranges.getAllCoords()) {
                    g.drawImage((Image) testMaze.getBox(coord).image, coord.x * IMAGES_SIZE, coord.y * IMAGES_SIZE, this);
                }
            }
        };
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / IMAGES_SIZE;
                int y = e.getY() / IMAGES_SIZE;
                Coord coord = new Coord(x, y);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    testMaze.pressLeftButton(coord);
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    testMaze.pressRightButton(coord);
                }
                if (e.getButton() == MouseEvent.BUTTON2) {
                    testMaze.startNewMaze();
                }
                panel.repaint();
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    System.out.println("Нажат энтер");
                    testMaze.startNewMaze();
                }
                panel.repaint();
            }
        });

        panel.setPreferredSize(new Dimension(
                Ranges.getSize().x * IMAGES_SIZE,
                Ranges.getSize().y * IMAGES_SIZE));
        add(panel);
    }

    private void initFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Minotaur Maze");
        setResizable(false);
        setVisible(true);
        pack();
        setLocationRelativeTo(null);
        setIconImage(getImage("mino"));
    }

    private void setImages() {
        for (Box box : Box.values()) {
            box.image = getImage(box.name().toLowerCase());
        }
    }

    private Image getImage(String name) {
        String filename = "/img/" + name.toLowerCase() + ".png";
        ImageIcon icon = new ImageIcon(getClass().getResource(filename));
        return icon.getImage();
    }
}
