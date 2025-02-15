import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

class SnakeGame extends JPanel {
    int boardWidth;
    int boardHeight;
    int tileSize = 25;
    int velocityx;
    int velocityy;
    boolean gameOver = false;

    Tile snakeHead;
    Tile food;

    ArrayList<Tile> snakeBody;

    Random random;

    Timer gameLoop;

    class Tile{
        int x;
        int y;

        Tile(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    SnakeGame(int boardWidth,int boardHeight){
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth,this.boardHeight));
        setBackground(Color.black);

        snakeHead = new Tile(5,5);
        snakeBody = new ArrayList<>();
        food = new Tile(10,10);
        random = new Random();
        placeFood();

        gameLoop = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                move();
                repaint();
                if(gameOver){
                    gameLoop.stop();
                }
            }
        });
        gameLoop.start();

        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_UP && velocityy != 1){
                    velocityx = 0;
                    velocityy = -1;
                }
                else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityy != -1){
                    velocityx = 0;
                    velocityy = 1;
                }
                else if(e.getKeyCode() == KeyEvent.VK_RIGHT && velocityx != -1){
                    velocityx = 1;
                    velocityy = 0;
                }
                else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityx != 1){
                    velocityx = -1;
                    velocityy = 0;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}

            @Override
            public void keyTyped(KeyEvent e) {}
        });
        setFocusable(true);

        velocityx = 1;
        velocityy = 0;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        for(int i=1;i<boardWidth/tileSize;i++){
            g.drawLine(i*tileSize,0,i*tileSize,boardHeight);
        }
        for(int i=1;i<boardHeight/tileSize;i++){
            g.drawLine(0,i*tileSize, boardWidth,i*tileSize);
        }

        g.setColor(Color.red);
        g.fillRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize);

        g.setColor(Color.green);
        g.fillRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize);

        for (Tile snakePart : snakeBody) {
            g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
        }

        g.setFont(new Font("Arial",Font.BOLD,16));
        if(gameOver){
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(snakeBody.size()),tileSize,tileSize);
        }
        else{
            g.setColor(Color.green);
            g.drawString("Score: " + String.valueOf(snakeBody.size()),tileSize,tileSize);
        }
    }

    public void placeFood(){
        food.x = random.nextInt(boardWidth/tileSize);
        food.y = random.nextInt(boardHeight/tileSize);
    }

    public void move(){
        if (snakeHead.x+velocityx < 0 || snakeHead.x+velocityx >= boardWidth/tileSize ||
                snakeHead.y+velocityy < 0 || snakeHead.y+velocityy >= boardHeight/tileSize) {
            gameOver = true;
            return;
        }

        if(collison(snakeHead,food)){
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        for(int i=snakeBody.size()-1;i>=0;i--){
            Tile snakePart = snakeBody.get(i);
            if(i==0){
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else{
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        snakeHead.x += velocityx;
        snakeHead.y += velocityy;

        for(Tile snakePart : snakeBody){
            if(collison(snakePart,snakeHead)){
                gameOver = true;
            }
        }
    }

    public boolean collison(Tile t1, Tile t2){
        return t1.x==t2.x && t1.y==t2.y;
    }
}

public class Main {
    public static void main(String[] args) {
        int boardWidth = 600;
        int boardHeight = 600;
        JFrame frame = new JFrame("Snake Game");

        frame.setVisible(true);
        frame.setSize(boardWidth,boardHeight);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(new ImageIcon(Main2.class.getResource("/snake.png")).getImage());

        SnakeGame s = new SnakeGame(boardWidth,boardHeight);
        frame.add(s);
        frame.pack();
        s.requestFocus();
    }
}