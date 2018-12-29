/**
 *@version cs251 Lab002 Date: 12/05/2018
 *@author Xiaomeng Li
 **/

import java.util.*;
/**
 * HexMineManager is in charge of managing the whole game
 **/
public class HexMineManager {
    /**
     * Node class is in charge of creating new nodes for hex grid.
     * Each node has index x, index y, unique HashCode to identify
     * each of them.
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
     * Initialize the game. Prepare the gameboard for Numbers and
     * a corresponding game board for Status. Status board tells
     * C: covered or not and we diplay the gameBoardStatus all the
     * time.
     */
    public void GameInitial() {
        int width = radius * 2 + 1;
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < width; ++j) {
                if (i + j >= radius && i + j <= 3 * radius) {
                    Node tempNode = new Node(i, j);
                    nodeCollection.add(tempNode.HashCode());
                    gameBoardNumber.put(tempNode.HashCode(), 0);
                    gameBoardStatus.put(tempNode.HashCode(), 'C');
                }
            }
        }
    }


    /**
     * Record the clicking position using Click function
     *
     * @param m and n, the coordinates
     */
    public void Click(int m, int n) {
        int width = radius * 2 + 1;
        if (m + n < radius || m + n > 3 * radius || m >= width || n>=width) {
            System.out.println("From click: Clicking out of boundary");
            return;
        }
        Node tempNodeClick = new Node(m, n);
        if (gameBoardStatus.get(tempNodeClick.HashCode()) != 'C'){
            System.out.println("From click: Clicking on an occupied position");
            return;
        }


        click_x = m;
        click_y = n;
    }

    /**
     * SetupNumber class to set up the numbers' situation
     * Of Course the number is decided according to the mines around.
     *
     */
    public void SetupNumber() {
        int width = radius * 2 + 1;
        int numberOfMinesAround;
        Node tempNodebb;
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < width; ++j) {
                if (i + j >= radius && i + j <= 3 * radius) {
                    numberOfMinesAround = 0;
                    tempNodebb = new Node(i, j);
                    for (int k = 0; k < 6; ++k) {
                        int tempx = dir[k][0] + i;
                        int tempy = dir[k][1] + j;
                        Node tempNodeww = new Node(tempx, tempy);
                        if (gameBoardNumber.containsKey(tempNodeww.HashCode()) &&
                                mineCollection.contains(tempNodeww.HashCode())) {
                            numberOfMinesAround += 1;
                        }
                    }
                    gameBoardNumber.put(tempNodebb.HashCode(), numberOfMinesAround);
                }
            }
        }
    }

    /**
     * Calculate how many flags are on the board
     **/
    public int numberOfFlags(){
        int f = 0;
        Node tempNodeff;
        int width = radius * 2 + 1;
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < width; ++j) {
                if (i + j >= radius && i + j <= 3 * radius) {
                    tempNodeff = new Node(i, j);
                    if ((gameBoardStatus.containsKey(tempNodeff.HashCode())
                            && (gameBoardStatus.get(tempNodeff.HashCode()) == 'F'))){
                        f += 1;
                    }
                }
            }
        }
        return f;
    }

    /**
     * SetFlag to let the player turn "C" to "F".
     *
     * @param m and n, the coordinates
     */
    public void SetFlag(int m, int n) {
        click_x = m;
        click_y = n;
        int width = radius * 2 + 1;
        if (m + n < radius || m + n > 3 * radius || m >= width || n>=width) {
            System.out.println("from flag: Clicking out of boundary");
            return;
        }
        Node tempNodeFlag = new Node(m, n);
        if (gameBoardStatus.get(tempNodeFlag.HashCode()) != 'C'){
            System.out.println("From Flag: Clicking on an occupied position");
            return;
        }

        Node tempNodeF = new Node(m, n);
        if (gameBoardStatus.containsKey(tempNodeF.HashCode())) {
            gameBoardStatus.put(tempNodeF.HashCode(), 'F');
        }
    }

    /**
     * Breath First Search to uncover the current position and
     * its neighbours recursively if necessary
     */
    public void BFS() {
        int x = click_x;
        int y = click_y;
        int width = radius * 2 + 1;
        HashSet<Integer> nodeVisited = new HashSet<>();
        Node tempNodeBFS = new Node(x, y);
        Node tempNodex;
        if (gameBoardStatus.get(tempNodeBFS.HashCode()) == 'F'){
            return;
        }
        if (mineCollection.contains(tempNodeBFS.HashCode())) {
            System.out.println("Oops, you step on a mine");
            for (int i = 0; i < width; ++i) {
                for (int j = 0; j < width; ++j) {
                    if (i + j >= radius && i + j <= 3 * radius) {
                        tempNodex = new Node(i, j);
                        if (mineCollection.contains(tempNodex.HashCode())) {
                            gameBoardStatus.put(tempNodex.HashCode(), 'M');
                        }
                    }
                }
            }
            return;
        } else {
            if (gameBoardNumber.containsKey(tempNodeBFS.HashCode()) &&
                    gameBoardNumber.get(tempNodeBFS.HashCode()) > 0) {
                int yy = gameBoardNumber.get(tempNodeBFS.HashCode());
                gameBoardStatus.put(tempNodeBFS.HashCode(), (char) (yy + '0'));
                return;
            } else {
                gameBoardStatus.put(tempNodeBFS.HashCode(), '.');
            }

        }
        LinkedList<Node> deque = new LinkedList<>();
        deque.add(tempNodeBFS);
        nodeVisited.add(tempNodeBFS.HashCode());
        while (deque.size() > 0) {
            LinkedList<Node> next = new LinkedList<>();
            for (Node node : deque) {
                for (int k = 0; k < 6; ++k) {
                    int tempxx = dir[k][0] + node.Index_X();
                    int tempyy = dir[k][1] + node.Index_Y();
                    Node tempNode_ = new Node(tempxx, tempyy);
                    if (gameBoardNumber.containsKey(tempNode_.HashCode()) &&
                            !nodeVisited.contains(tempNode_.HashCode()) &&
                            gameBoardStatus.get(tempNode_.HashCode()) == 'C') {
                        nodeVisited.add(tempNode_.HashCode());
                        if (gameBoardNumber.get(tempNode_.HashCode()) > 0) {
                            int ii = gameBoardNumber.get(tempNode_.HashCode());
                            char b = (char) (ii + '0');
                            gameBoardStatus.put(tempNode_.HashCode(), b);
                        } else if (gameBoardNumber.get(tempNode_.HashCode()) == 0) {
                            next.add(tempNode_);
                            gameBoardStatus.put(tempNode_.HashCode(), '.');
                        }
                    }
                }
            }
            deque = next;
        }
    }

    /**
     * SetupMine function to set up mines at the beginning of the game.
     */
    public void SetupMine() {
        int width = radius * 2 + 1;
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < width; ++j) {
                if (i + j >= radius && i + j <= 3 * radius) {
                    if (i + j >= randomNumber.nextDouble() * radius * 20) {
                        Node tempNode1 = new Node(i, j);
                        mineCollection.add(tempNode1.HashCode());
                        gameBoardNumber.put(tempNode1.HashCode(), 99);

                    }
                }
            }
        }
    }

    /**
     * PrintGameBoard function prints the gameBoardStatus board. It should be called
     * every time player clicks the board.
     */
    public void PrintGameBoard() {
        int width = radius * 2 + 1;
        int smallCounter = 1;
        for (int i = 0; i < width; ++i) {
            if (i < width / 2 + 1) {
                for (int j = 0; j < width; ++j) {
                    Node tempNode = new Node(i, j);
                    if (gameBoardStatus.containsKey(tempNode.HashCode()) && i <= radius) {
                        System.out.print(gameBoardStatus.get(tempNode.HashCode()) + " ");
                    } else if (gameBoardStatus.containsKey(tempNode.HashCode()) && i > radius) {
                        System.out.print(gameBoardStatus.get(tempNode.HashCode()) + " ");
                    } else {
                        System.out.print(" ");
                    }
                }
                System.out.print("\n");
            } else {
                int tempCounter = smallCounter;
                while (tempCounter > 0) {
                    System.out.print(" ");
                    tempCounter = tempCounter - 1;
                }
                smallCounter = smallCounter + 1;
                for (int j = 0; j < width; ++j) {
                    Node tempNode = new Node(i, j);
                    if (gameBoardStatus.containsKey(tempNode.HashCode()) && i <= radius) {
                        System.out.print(gameBoardStatus.get(tempNode.HashCode()) + " ");
                    } else if (gameBoardStatus.containsKey(tempNode.HashCode()) && i > radius) {
                        System.out.print(gameBoardStatus.get(tempNode.HashCode()) + " ");
                    } else {
                        System.out.print(" ");
                    }
                }
                System.out.print("\n");
            }
        }
    }

    private int radius;
    private int click_x;
    private int click_y;
    private HashMap<Integer, Integer> gameBoardNumber = new HashMap<>();
    public HashMap<Integer, Character> gameBoardStatus = new HashMap<>();
    private HashSet<Integer> nodeCollection = new HashSet<>();
    public HashSet<Integer> mineCollection = new HashSet<>();
    private final int[][] dir = {{1, 0}, {-1, 0}, {1, -1}, {-1, 1}, {0, -1}, {0, 1}};
    private Random randomNumber = new Random(5);

    /**
     * Default constructor of the HexMineManager java file.
     * @param integer radius = R
     */
    public HexMineManager(int R) {
        radius = R;
        GameInitial();
        SetupMine();
        SetupNumber();
    }
}

