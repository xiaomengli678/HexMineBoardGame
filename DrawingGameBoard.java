/**
 *@version cs251 Lab002 Date: 12/05/2018
 *@author Xiaomeng Li
 **/
import java.awt.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.*;
import java.util.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.net.*;

/** this is the DrawingGameBoard class. In order to use the functions,
 I extend the Jpanel and implements both MouseListener and ActionListener**/
public class DrawingGameBoard extends JPanel implements
        MouseListener, ActionListener {

    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    /** mousePressed method to detect the mouse event. Left
     * and Right mouse clicking mainly of course.
     *@param MouseEvent e
     **/
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            Pixel tr = new Pixel((e.getX()-100)*1.0,(e.getY()-100)*1.0);
            Node_Int ndd = PixelToPointyHex(tr);
            click_x = (ndd.Intx_X());
            click_y = (ndd.Inty_Y());
            clicked = true;
            GameOn.Click(click_x, click_y);
            GameOn.BFS();
            updateBoardStatus = new HashMap<>(GameOn.gameBoardStatus);
            GameOn.PrintGameBoard();

        } else {
            Pixel tr = new Pixel((e.getX() - 100) * 1.0,
                    (e.getY() - 100) * 1.0);
            Node_Int ndd = PixelToPointyHex(tr);
            click_x = (ndd.Intx_X());
            click_y = (ndd.Inty_Y());
            clicked = true;
            GameOn.SetFlag(click_x, click_y);
            numberOfFlag = GameOn.numberOfFlags();
            labelFlag.setText("Flags Used: " + numberOfFlag+ " ");
            updateBoardStatus = new HashMap<>(GameOn.gameBoardStatus);
            GameOn.PrintGameBoard();
        }

        //revalidate();
    }

    /** actionPerformed function is used to detect the action
     * during the game. Therefore i also put the repaint() method
     * in this function.
     * @param ActionEvent ev
     **/
    public void actionPerformed(ActionEvent ev) {

        if (clicked) {
            repaint();
        }

        if(checkGameOver()){
            int te_res;
            if(!win){
                te_res = JOptionPane.showOptionDialog(null,
                        "You Loss", "Game Over", JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.INFORMATION_MESSAGE, null, null, null);
            } else {
                te_res = JOptionPane.showOptionDialog(null,
                        "You Win", "Game Over", JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.INFORMATION_MESSAGE, null, null, null);
            }
            if(te_res == JOptionPane.OK_OPTION) {
                System.exit(0);
            } else {
                System.exit(0);
            }
        }
    }

    /**
     DrawingGameBoard() method to draw the game board, set up the timer and
     import the image for the use of flag and bomb.
     **/
    public DrawingGameBoard(){

        GameOn = new HexMineManager(radius);
        numberOfMine = GameOn.mineCollection.size();
        labelLeftMine.setText("Mines Exited: " + numberOfMine);
        updateBoardStatus = new HashMap<>(GameOn.gameBoardStatus);
        if (!big) {
            setPreferredSize(new Dimension(700, 500));
        } else {
            setPreferredSize(new Dimension(1300, 800));
        }
        //setPreferredSize(new Dimension(700, 500));
        setBackground(Color.WHITE);
        Timer timer = new Timer(500, this);
        timer.setInitialDelay(0);
        timer.start();
        try {
            imageBomb = ImageIO.read(new File("bomb.jpg"));
            imageFlag = ImageIO.read(new File("flag.jpg"));
        } catch (IOException ex) {
            System.out.println("File not found dude");
        }

        Timer timer1 = new Timer(1000, this::timerOneMethod);
        timer1.setInitialDelay(0);
        timer1.start();

        Timer timer2 = new Timer(5000, this::timerTwoMethod);
        timer2.setInitialDelay(0);
        timer2.start();
    }

    /**
     * update time when timerOne is available
     * @param ActionEvent e
     */
    private void timerOneMethod(ActionEvent e) {
        seconds += 1;
        timeLasted.setText("Time Passed: " + seconds+ " ");
    }

    /**
     * update time when timerTwo is available
     * @param ActionEvent e
     */
    private void timerTwoMethod(ActionEvent e) {
        tseconds = (int) (seconds / 10);
        tenSecond.setText("Another 10 seconds Passed: " + tseconds + " ");
    }

    /**
     createAndShowGUI() is used to set up the frame of the game.
     I add the requied Labels and some useful settings
     **/
    private static void createAndShowGUI() {

        JFrame frame = new JFrame("DrawingGameBoard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new GridBagLayout());
        labelFlag = new JLabel();
        labelLeftMine = new JLabel();
        timeLasted = new JLabel();
        tenSecond = new JLabel();
        labelFlag.setText("Flags Used: " + numberOfFlag+ " ");
        panel.add(labelFlag);
        labelLeftMine.setText("Mines Left: " + numberOfMine+ " ");
        panel.add(labelLeftMine);
        panel.add(tenSecond);
        panel.add(timeLasted);
        frame.getContentPane().add(panel);
        frame.requestFocusInWindow();
        frame.getContentPane().addMouseListener(new DrawingGameBoard());
        frame.add(panel, BorderLayout.NORTH);
        frame.add(new DrawingGameBoard());
        frame.getContentPane().repaint();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    /** paintComponent method is used for the drawing on the board. Basically,
     for every hex grid, I would need to set up color and give status (for
     example, covered, flag, numbers or mine)
     @param Graphics g
     **/
    public void paintComponent(Graphics g){

        super.paintComponent(g);
        Polygon p;

        int width = radius * 2 + 1;

        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < width; ++j) {
                if (i + j >= radius && i + j <= 3 * radius) {
                    Node tempNode = new Node(i, j);
                    if (updateBoardStatus.containsKey(tempNode.HashCode()) &&
                            updateBoardStatus.get(tempNode.HashCode()) == 'C') {
                        //System.out.println("covered");
                        Pixel tempPix = PointyHexToPixel(i, j);
                        p = new Polygon();
                        for (int r = 0; r < 6; r++) {
                            p.addPoint((int) (100 + tempPix.Index_XX() +
                                            hex_radius * Math.cos
                                                    (r * 2 * Math.PI / 6 + Math.toRadians(30))),
                                    (int) (100 + tempPix.Index_YY() +
                                            hex_radius * Math.sin
                                                    (r * 2 * Math.PI / 6 + Math.toRadians(30))));
                        }
                        g.setColor(Color.BLACK);
                        g.drawPolygon(p);
                        g.setColor(Color.darkGray);
                        g.fillPolygon(p);
                    } else if (updateBoardStatus.containsKey(tempNode.HashCode()) &&
                            updateBoardStatus.get(tempNode.HashCode()) == '.'){
                        //System.out.println("empty");
                        Pixel tempPix = PointyHexToPixel(i, j);
                        p = new Polygon();
                        for (int r = 0; r < 6; r++) {
                            p.addPoint((int) (100 + tempPix.Index_XX() +
                                            hex_radius * Math.cos(
                                                    r * 2 * Math.PI / 6 +
                                                            Math.toRadians(30))),
                                    (int) (100 + tempPix.Index_YY() +
                                            hex_radius * Math.sin(
                                                    r * 2 * Math.PI / 6 +
                                                            Math.toRadians(30))));
                        }
                        g.setColor(Color.BLACK);
                        g.drawPolygon(p);
                        g.setColor(Color.GRAY);
                        g.fillPolygon(p);
                    } else if (updateBoardStatus.containsKey(tempNode.HashCode()) &&
                            updateBoardStatus.get(tempNode.HashCode()) == 'M'){
                        stepOnMine = true;
                        Pixel tempPix = PointyHexToPixel(i, j);
                        p = new Polygon();
                        for (int r = 0; r < 6; r++) {
                            p.addPoint((int) (100 + tempPix.Index_XX() +
                                            hex_radius * Math.cos(
                                                    r * 2 * Math.PI / 6 +
                                                            Math.toRadians(30))),
                                    (int) (100 + tempPix.Index_YY() +
                                            hex_radius * Math.sin(
                                                    r * 2 * Math.PI / 6 +
                                                            Math.toRadians(30))));
                        }
                        g.setColor(Color.BLACK);
                        g.drawPolygon(p);
                        int widthim = imageBomb.getWidth(this);
                        int heightim = imageBomb.getHeight(this);

                        Double scale = 0.035;
                        int w = (int) (scale * widthim);
                        int h = (int) (scale * heightim);
                        g.drawImage(imageBomb, (int)(100+tempPix.Index_XX()
                                        - hex_radius*0.55) ,
                                (int)(100 + tempPix.Index_YY()- hex_radius*0.55),
                                w, h, this);
                    } else if (updateBoardStatus.containsKey(tempNode.HashCode()) &&
                            updateBoardStatus.get(tempNode.HashCode()) == 'F') {
                        //System.out.println("flag");
                        Pixel tempPix = PointyHexToPixel(i, j);
                        p = new Polygon();
                        for (int r = 0; r < 6; r++) {
                            p.addPoint((int) (100 + tempPix.Index_XX() +
                                            hex_radius * Math.cos(
                                                    r * 2 * Math.PI / 6
                                                            + Math.toRadians(30))),
                                    (int) (100 + tempPix.Index_YY() +
                                            hex_radius * Math.sin(
                                                    r * 2 * Math.PI / 6
                                                            + Math.toRadians(30))));
                        }
                        g.setColor(Color.BLACK);
                        g.drawPolygon(p);
                        int widthim = imageFlag.getWidth(this);
                        int heightim = imageFlag.getHeight(this);

                        Double scale = 0.02;
                        int ww = (int) (scale * widthim);
                        int hh = (int) (scale * heightim);
                        g.drawImage(imageFlag, (int) (100 + tempPix.Index_XX()
                                        - hex_radius * 0.55),
                                (int) (100 + tempPix.Index_YY() -
                                        hex_radius * 0.55), ww, hh, this);
                    } else if (updateBoardStatus.containsKey(tempNode.HashCode()) &&
                            updateBoardStatus.get(tempNode.HashCode()) - '0' > 0) {
                        //System.out.println("number");
                        Pixel tempPix = PointyHexToPixel(i, j);
                        p = new Polygon();
                        for (int r = 0; r < 6; r++) {
                            p.addPoint((int) (100 + tempPix.Index_XX() +
                                            hex_radius * Math.cos(
                                                    r * 2 * Math.PI / 6 +
                                                            Math.toRadians(30))),
                                    (int) (100 + tempPix.Index_YY() +
                                            hex_radius * Math.sin(
                                                    r * 2 * Math.PI / 6 +
                                                            Math.toRadians(30))));
                        }
                        g.setColor(Color.BLACK);
                        g.drawPolygon(p);
                        int fontSize = 30;
                        g.setFont(new Font("TimesRoman",
                                Font.PLAIN, fontSize));
                        g.setColor(Color.BLUE);
                        g.drawString((updateBoardStatus.get(
                                tempNode.HashCode())).toString(),
                                (int)(100+tempPix.Index_XX()) ,
                                (int)(100 + tempPix.Index_YY()));
                    }
                }
            }
        }

    }

    /**
     A Node class defined just for recognizing the axial coordinates.
     **/
    public class Node {
        public final int x;
        public final int y;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public Integer Index_X() {
            return this.x;
        }
        public Integer Index_Y() {
            return this.y;
        }
        public Integer HashCode() {
            return this.x + 37 * this.y;
        }
    }

    /**
     A pixel class defined just for giving Double coordinates
     for the pixel coordinates.
     **/
    public class Pixel {
        public final Double xx;
        public final Double yy;

        public Pixel(Double x, Double y) {
            this.xx = x;
            this.yy = y;
        }
        public Double Index_XX() {
            return this.xx;
        }
        public Double Index_YY() {
            return this.yy;
        }
    }

    /**
     Node_Double is for the axial <-> pixel coordinates.
     The use of the functions on the website need some
     level exchanging between Double and Int.
     **/
    public class Node_Double {
        public final Double x;
        public final Double y;

        public Node_Double(Double x, Double y) {
            this.x = x;
            this.y = y;
        }
        public Double Index_X() {
            return this.x;
        }
        public Double Index_Y() {
            return this.y;
        }
    }

    /**
     Node_Int, a class still created for transferring between axial and pixel.
     **/
    public class Node_Int {
        public final int x;
        public final int y;

        public Node_Int(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public int Intx_X() {
            return this.x;
        }
        public int Inty_Y() {
            return this.y;
        }
    }

    /**
     Cube_Double is used for Cube coordinates: x, y, z. Here all of them three
     are all Double
     **/
    public class Cube_Double {
        public final Double x;
        public final Double y;
        public final Double z;

        public Cube_Double(Double x, Double y, Double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        public Double In_x() {
            return this.x;
        }
        public Double In_y() {
            return this.y;
        }
        public Double In_z() {
            return this.z;
        }

    }

    /**
     CubeToAxial function to transfer Cube coordinates
     to axial coordinates. Three coordnates become two.
     @param Cube_Double c
     @return Double axial coordinates
     **/
    private Node_Double CubeToAxial(Cube_Double c) {
        Double q = c.In_x();
        Double r = c.In_z();
        Node_Double node_ca = new Node_Double(q,r);
        return node_ca;
    }

    /**
     AxialToCube function to transfer Axial corrdinates to
     Cube corrdinates. Two coordinates become three.
     @param Node_Double nn
     @return Double Cube coordinates
     **/
    private Cube_Double AxialToCube(Node_Double nn){
        Double xc = nn.Index_X();
        Double zc = nn.Index_Y();
        Double yc = -xc-zc;
        Cube_Double cube_ca = new Cube_Double(xc, yc, zc);
        return cube_ca;
    }

    /**
     CubeRound to create the function to correctly "rounding"
     to the Cube_Double in order to rounding while keeping
     the sum of three is zero.
     @param Cube_Double cr
     @return a rounded Cube_Double.
     **/
    private Cube_Double CubeRound(Cube_Double cr){
        int rx = (int)(Math.round(cr.In_x()));
        int ry = (int)(Math.round(cr.In_y()));
        int rz = (int)(Math.round(cr.In_z()));

        Double x_diff = Math.abs(rx*1.0-cr.In_x());
        Double y_diff = Math.abs(ry*1.0-cr.In_y());
        Double z_diff = Math.abs(rz*1.0-cr.In_z());

        if (x_diff > y_diff && x_diff > z_diff){
            rx = -ry-rz;
        } else if (y_diff > z_diff){
            ry = -rx-rz;
        } else {
            rz = -rx-ry;
        }
        Cube_Double cube_caf = new Cube_Double(rx*1.0, ry*1.0, rz*1.0);
        return cube_caf;
    }

    /**
     PointyHexToPixel to transfer Hex corrdinates to pixel coordinates.
     @param int q, int r
     @return Pixel
     **/
    private Pixel PointyHexToPixel(int q, int r){
        Double x = hex_radius * (Math.sqrt(3.0) * q +
                Math.sqrt(3.0) / 2 * r);
        Double y = hex_radius * (3.0/2 * r);
        Pixel pi = new Pixel(x, y);
        return pi;
    }


    /**
     PixelToPointyHex to transfer pixel coordinates to Hex grid coordinates
     @param Pixel p class instance
     @return Node_Int class instance
     **/
    private Node_Int PixelToPointyHex(Pixel p){
        Double q = (Math.sqrt(3.0)/3 * p.Index_XX() -
                1.0 / 3 * p.Index_YY())/hex_radius;
        Double r = (2.0/3 * p.Index_YY())/hex_radius;
        Node_Double nd = HexRound(q, r);
        Node_Int nds = new Node_Int(nd.Index_X().intValue(),
                nd.Index_Y().intValue());
        return nds;
    }

    /**
     HexRound function used for rounding the hex coordinates
     @param Double q, Double r
     @return CubeToAxial results.
     **/
    private Node_Double HexRound(Double q, Double r){
        Node_Double nhex = new Node_Double(q, r);
        return CubeToAxial(CubeRound(AxialToCube(nhex)));
    }

    /**
     checkGameOver function is used for detecting whether or not
     the game is over. The game is over because someone wins or
     someone steps on a mine.
     @return Boolean game results
     **/
    public Boolean checkGameOver(){
        if (stepOnMine) {
            win = false;
            java.applet.AudioClip clip = java.applet.Applet.newAudioClip(url);
            clip.play();
            return true;
        }

        int width = radius * 2 + 1;
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < width; ++j) {
                if (i + j >= radius && i + j <= 3 * radius) {
                    Node tempNode = new Node(i, j);
                    if(updateBoardStatus.containsKey
                            (tempNode.HashCode())
                            && updateBoardStatus.get
                            (tempNode.HashCode()) == 'C') {
                        return false;
                    }
                }
            }
        }

        win = true;
        return true;
    }

    private final int hex_radius = 30;
    private static int radius;
    private int click_x;
    private int click_y;
    private static HexMineManager GameOn;
    private static HashMap<Integer, Character> updateBoardStatus;
    private static Boolean clicked = false;
    private BufferedImage imageBomb;
    private BufferedImage imageFlag;
    private Boolean stepOnMine = false;
    private Boolean win = true;
    private static int numberOfFlag = 0;
    private static int numberOfMine;
    private static JLabel labelFlag;
    private static JLabel labelLeftMine;
    private static JLabel timeLasted;
    private static Double seconds = 0.0;
    private static JLabel tenSecond;
    private static int tseconds = 0;
    private static URL url;
    private static Boolean big = true;
    /**
     this is the main game. main game mainly gives player choices for
     different game configurations and sets up the final radius etc.
     **/
    public static void main(String[] args){
        JTextField xField = new JTextField(5);
        JTextField yField = new JTextField(5);

        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Small Game:"));
        myPanel.add(xField);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("Big Game:"));
        myPanel.add(yField);

        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Please Choose Game Size", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            if (xField.getText().length() > 0 && yField.getText().length() > 0){
                System.out.println("You can only choose one");
                System.exit(0);
            }else if(xField.getText().length() > 0){
                radius = 3;
                big = false;
                System.out.println("You choose the small one");
            }else {
                radius = 7;
                System.out.println("You choose the big one");
            }
        } else {
            System.exit(0);
        }

        url = null;
        try {
            File file = new File("gun_battle_sound.wav");
            if (file.canRead())
                url = file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("could not play this", e);
        }


        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

}

