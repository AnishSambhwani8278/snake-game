import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

class SnakeGame2 extends JPanel {
    int boardWidth;
    int boardHeight;
    int tileSize = 25;
    int velocityx;
    int velocityy;
    int score = 0;
    boolean gameOver = false;

    JLabel scoreLabel;
    JButton restartButton;

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

    SnakeGame2(int boardWidth,int boardHeight, JLabel scoreLabel, JButton restartButton){
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.scoreLabel = scoreLabel;
        this.restartButton = restartButton;
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
                if(gameOver){
                    gameLoop.stop();
                }
                move();
                repaint();
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

        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(gameOver){
                    snakeHead.x = 5;
                    snakeHead.y = 5;

                    snakeBody.clear();
                    placeFood();

                    score = 0;
                    velocityx = 1;
                    velocityy = 0;
                    gameOver = false;
                    gameLoop.start();
                }
            }
        });
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

        g.setColor(Color.white);
        g.drawLine(0,0,boardWidth,0);

        g.setColor(Color.red);
        g.fillRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize);

        g.setColor(Color.green);
        g.fillRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize);

        for (Tile snakePart : snakeBody) {
            g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
        }

        if(gameOver){
            scoreLabel.setForeground(Color.red);
            scoreLabel.setText("Game Over: " + score);
        }
        else{
            scoreLabel.setForeground(Color.green);
            scoreLabel.setText("Score: " + score);
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
            score++;
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

public class Main2 {
    public static void main(String[] args) {
        int frameboardWidth = 600;
        int frameboardHeight = 670;
        int boardWidth = 600;
        int boardHeight = 600;
        JFrame frame = new JFrame("Snake Game");
        JPanel scorePanel = new JPanel();;
        JLabel scoreLabel = new JLabel();
        JButton restarButton = new JButton();

        frame.setSize(frameboardWidth,frameboardHeight);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.setIconImage(new ImageIcon(Main2.class.getResource("/snake.png")).getImage());

        restarButton.setFont(new Font("Arial",Font.BOLD,17));
        restarButton.setText("Restart");
        restarButton.setForeground(Color.orange);
        restarButton.setBackground(Color.black);
        restarButton.setFocusable(false);

        scorePanel.setPreferredSize(new Dimension(boardWidth,35));
        scorePanel.setLayout(new BorderLayout());
        scorePanel.setBackground(Color.black);
        scorePanel.add(scoreLabel, BorderLayout.WEST);
        scorePanel.add(restarButton, BorderLayout.EAST);

        SnakeGame2 s = new SnakeGame2(boardWidth, boardHeight, scoreLabel, restarButton);
        scoreLabel.setFont(new Font("Arial",Font.BOLD,20));
        scoreLabel.setText("Score: " + s.score);

        frame.add(s,BorderLayout.SOUTH);
        frame.add(scorePanel,BorderLayout.NORTH);
        frame.pack();
        frame.setVisible(true);
        s.requestFocus();
    }
}