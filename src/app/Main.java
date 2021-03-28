/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import minotaurMaze.Box;
import minotaurMaze.Coord;
import minotaurMaze.MaxFlow;
import minotaurMaze.Ranges;

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
 * @author DNS
 */
class Main extends JFrame {

    /**
     * @param args the command line arguments
     */
    //стандартные объекты для прорисовки интерфейса
    private MaxFlow maxFlow;
    private JPanel panel;
    //задание размеров поля
    private final int COLS = 6;
    private final int ROWS = 6;
    private final int IMAGES_SIZE = 50;

    //Вход в программу
    public static void main(String[] args) {
        new Main().setVisible(true);
    }

    //Построение лабиринта
    private Main() {
        maxFlow = new MaxFlow(COLS, ROWS);
        maxFlow.startNewMaze();
        //метод установки изображений
        setImages();
        //метод установки панели
        initPanel();
        //метод установки фрейма, интерфейса приложения
        initFrame();
    }

    private void initPanel() {
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Coord coord : Ranges.getAllCoords()) {
                    g.drawImage((Image) maxFlow.getBox(coord).image, coord.x * IMAGES_SIZE, coord.y * IMAGES_SIZE, this);
                }
            }
        };
        //добавление слушателей для обработки нажатий мыши
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / IMAGES_SIZE;
                int y = e.getY() / IMAGES_SIZE;
                Coord coord = new Coord(x, y);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    maxFlow.pressLeftButton(coord);
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    maxFlow.pressRightButton(coord);
                }
                if (e.getButton() == MouseEvent.BUTTON2) {
                    //метод, заново рисующий лабиринт
                    maxFlow.startNewMaze();
                }
                //метод, заново рисующий панель с лабиринтом
                panel.repaint();
            }
        });
        //добавление слушателей для обработки нажатия клавиши энтер
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    maxFlow.startMaze();
                }
                panel.repaint();
            }
        });
        //установка размеров поля для панели
        panel.setPreferredSize(new Dimension(
                Ranges.getSize().x * IMAGES_SIZE,
                Ranges.getSize().y * IMAGES_SIZE));
        add(panel);
    }

    //прорисовка интерфейса
    private void initFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //установка заголовка
        setTitle("Minotaur Maze");
        setResizable(false);
        //установка видимости для окна
        setVisible(true);
        pack();
        setLocationRelativeTo(null);
        //установка иконки
        setIconImage(getImage("mino"));
    }
    //прорисовка изображений для ячеек поля
    private void setImages() {
        for (Box box : Box.values()) {
            box.image = getImage(box.name().toLowerCase());
        }
    }
    //получение изображений из папки ресурсов
    private Image getImage(String name) {
        String filename = "/img/" + name.toLowerCase() + ".png";
        ImageIcon icon = new ImageIcon(getClass().getResource(filename));
        return icon.getImage();
    }
}
