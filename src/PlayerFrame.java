import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class PlayerFrame extends JFrame {
    private int width, height;
    private Container contentPane;
    private PlayerSprite thisPlayer;
    private PlayerSprite anotherPlayer;
    private DrawingComponent drawingComponent;
    private Timer animationTimer;
    private boolean up, down, left, right;
    private Socket socket;
    private int playerID;
    private ReadFromServer readFromServer;
    private WriteToServer writeToServer;

    private Rectangle leftWall;
    private Rectangle rightWall;
    private Rectangle topWall;
    private Rectangle bottomWall;
    private Rectangle centerSquare;

    private BufferedImage player1Image;
    private BufferedImage player2Image;
    private BufferedImage centerImage;

    private int collisionCount;
    private int collisionLimit;

    public PlayerFrame(int width, int height) {
        this.width = width;
        this.height = height;
        up = false;
        down = false;
        left = false;
        right = false;
        collisionCount = 0;
        collisionCount = 0;
        collisionLimit = 5; // Ustawienie limitu kolizji na 5
    }

    public void setUpGUI() {
        contentPane = this.getContentPane();
        this.setTitle("Player #" + playerID);
        contentPane.setPreferredSize(new Dimension(width, height));
        createSprite();
        drawingComponent = new DrawingComponent();
        contentPane.add(drawingComponent);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);

        setUpAnimationTimer();
        setUpKeyListener();
    }

    private BufferedImage loadImage(String filename) {
        try {
            return ImageIO.read(new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void createSprite() {
        if (playerID == 1) {
            thisPlayer = new PlayerSprite(100, 400, 50, Color.RED);
            anotherPlayer = new PlayerSprite(490, 400, 50, Color.BLUE);
        } else {
            thisPlayer = new PlayerSprite(490, 400, 50, Color.BLUE);
            anotherPlayer = new PlayerSprite(100, 400, 50, Color.RED);
        }

        int squareSize = 130;
        int centerX = width / 2 - squareSize / 2;
        int centerY = height / 2 - squareSize / 2;
        player1Image = loadImage("1.png");
        player2Image = loadImage("2.png");
        centerImage = loadImage("3.png");
        centerSquare = new Rectangle(centerX, centerY, squareSize, squareSize);
        leftWall = new Rectangle(0, 0, 10, height);
        rightWall = new Rectangle(width - 10, 0, 10, height);
        topWall = new Rectangle(0, 0, width, 10);
        bottomWall = new Rectangle(0, height - 10, width, 10);
    }

    private void setUpAnimationTimer() {
        int interval = 10;
        final double[] verticalVelocity = {0};
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                double speed = 5;
                double newX = thisPlayer.getX();
                double newY = thisPlayer.getY();

                if (up) {
                    newY -= speed;
                } else if (down) {
                    newY += speed;
                } else if (left) {
                    newX -= speed;
                } else if (right) {
                    newX += speed;
                }
                Rectangle newBounds = new Rectangle((int) newX, (int) newY, (int) thisPlayer.getSize(), (int) thisPlayer.getSize());

                //Tutaj kolizje jak chcesz to dorób michał jakies
                //lewa
                if (newBounds.intersects(leftWall)) {
                    newX = leftWall.getMaxX();
                    
                    collisionCount++;
                }

                // prawa
                if (newBounds.intersects(rightWall)) {
                    newX = rightWall.getX() - thisPlayer.getSize();
                    
                    collisionCount++;
                }

                //gora
                if (newBounds.intersects(topWall)) {
                    newY = topWall.getMaxY();
                   
                    collisionCount++;
                }

                //dol
                if (newBounds.intersects(bottomWall)) {
                    newY = bottomWall.getY() - thisPlayer.getSize();
                  
                    collisionCount++;
                }

                if (newBounds.intersects(centerSquare)) {
                   
                    collisionCount++;
                } else {
                    thisPlayer.setX(newX);
                    thisPlayer.setY(newY);
                }

                if (newX < 0) {
                    newX = 0;
                } else if (newX > width - thisPlayer.getSize()) {
                    newX = width - thisPlayer.getSize();
                }
                if (newY < 0) {
                    newY = 0;
                } else if (newY > height - thisPlayer.getSize()) {
                    newY = height - thisPlayer.getSize();
                }

                Rectangle anotherBounds = anotherPlayer.getBounds();
                if (newBounds.intersects(anotherBounds)) {
                    
                    collisionCount++;
                } else {
                    thisPlayer.setX(newX);
                    thisPlayer.setY(newY);
                }

               if (collisionCount >= collisionLimit) {
                endGame();
                }

                drawingComponent.repaint();
            }
        };
        animationTimer = new Timer(interval, al);
        animationTimer.start();
    }

    private void setUpKeyListener() {
        KeyListener keyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();

                switch (keyCode) {
                    case KeyEvent.VK_UP:
                        up = true;
                        break;
                    case KeyEvent.VK_DOWN:
                        down = true;
                        break;
                    case KeyEvent.VK_LEFT:
                        left = true;
                        break;
                    case KeyEvent.VK_RIGHT:
                        right = true;
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();

                switch (keyCode) {
                    case KeyEvent.VK_UP:
                        up = false;
                        break;
                    case KeyEvent.VK_DOWN:
                        down = false;
                        break;
                    case KeyEvent.VK_LEFT:
                        left = false;
                        break;
                    case KeyEvent.VK_RIGHT:
                        right = false;
                        break;
                }
            }
        };
        contentPane.addKeyListener(keyListener);
        contentPane.setFocusable(true);
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", 45371);
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            playerID = inputStream.readInt();
            System.out.println("You are player#" + playerID);
            if (playerID == 1) {
                System.out.println("Waiting for more players to connect...");
            }
            readFromServer = new ReadFromServer(inputStream);
            writeToServer = new WriteToServer(outputStream);
            readFromServer.waitForStartMsg();
        } catch (IOException e) {
            System.out.println("IOException from connectToServer()");
        }
    }

    private void endGame() {
    if (collisionCount > collisionLimit) {
        String message = "Player #" + playerID + " reached the collision limit. Game over!";
        JOptionPane.showMessageDialog(this, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        int option = JOptionPane.showOptionDialog(this, "Click OK to close the application", "Message", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
        if (option == JOptionPane.OK_OPTION) {
            System.exit(0);
        }
    }
}


    private class DrawingComponent extends JComponent {
        protected void paintComponent(Graphics g) {
            Graphics2D graphics2D = (Graphics2D) g;
            
            graphics2D.setColor(Color.GRAY);
            graphics2D.fill(leftWall);
            graphics2D.fill(rightWall);
            graphics2D.fill(topWall);
            graphics2D.fill(bottomWall);
            graphics2D.setColor(Color.GREEN);
            graphics2D.drawImage(centerImage, centerSquare.x, centerSquare.y, centerSquare.width, centerSquare.height, null);
            anotherPlayer.drawSprite(graphics2D, player2Image);
            thisPlayer.drawSprite(graphics2D, player1Image);
        }
    }

    private class ReadFromServer implements Runnable {
        private DataInputStream dataInputStream;

        public ReadFromServer(DataInputStream dataInputStream) {
            this.dataInputStream = dataInputStream;
            System.out.println("ReadFromServer Runnable created");
        }

        @Override
        public void run() {
            try {
                while (true) {
                    if (anotherPlayer != null) {
                        anotherPlayer.setX(dataInputStream.readDouble());
                        anotherPlayer.setY(dataInputStream.readDouble());
                    }
                }
            } catch (IOException e) {
                System.out.println("IOException from ReadFromServer run()");
            }
        }

        public void waitForStartMsg() {
            try {
                String startMsg = dataInputStream.readUTF();
                System.out.println("Message from server: " + startMsg);
                Thread readThread = new Thread(readFromServer);
                Thread writeThread = new Thread(writeToServer);
                readThread.start();
                writeThread.start();
            } catch (IOException e) {
                System.out.println("IOException from waitForStartMsg()");
            }
        }
    }

    private class WriteToServer implements Runnable {
        private DataOutputStream dataOutputStream;

        public WriteToServer(DataOutputStream dataOutputStream) {
            this.dataOutputStream = dataOutputStream;
            System.out.println("WriteToServer Runnable created");
        }

        @Override
        public void run() {
            try {
                while (true) {
                    if (thisPlayer != null) {
                        dataOutputStream.writeDouble(thisPlayer.getX());
                        dataOutputStream.writeDouble(thisPlayer.getY());
                        dataOutputStream.flush();
                    }
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        System.out.println("InterruptedException from WriteToServer run()");
                    }
                }
            } catch (IOException e) {
                System.out.println("IOException from WriteToServer()");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PlayerFrame pf = new PlayerFrame(640, 480);
            pf.connectToServer();
            pf.setUpGUI();
        });
    }

 
}
    
    


