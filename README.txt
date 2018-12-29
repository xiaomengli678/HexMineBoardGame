#####################################################
##    cs251 Lab10 Hexagonal Minesweeper README     ##
##                   Xiaomeng Li                   ##
#####################################################

- How to play the game
  In the JAVA file, make sure you have two files: DrawingGameBoard.java and
  HexMineManager.java open. Both of the java files as well as two images
  representing the custom images for bombs and flags and one WAV file representing
  the background music should be in the same directory.
  
  Click Run, you should see a dialog asking: Big game or Small game? By default
  the game starts with big game. You can put anything in the choosing area. After
  game mode choosing, you are in the official game. Of course, if you put anything
  in the small game, the game will start with small game.

  Notice that I also upload the Jar file. Therefore the command to run the Jar
  File would be: "java -jar HexMines.jar". You will see the same dialog.

  By default, you should be able to see three labels.
  1. Number of flags used
  2. Number of mines existed
  3. How many 10 seconds have passed since the start of the game, attention:
  this will update slower than option 4.
  4. Time Passed from the start of the game

  Timer Use:
  I have three timers
  1. timer, with 500 delay, used to update/repaint the game.
  2. timer1, with 1000 delay, used to record the time length of the game.
  3. timer2, with 5000 delay, used to record how many ten seconds have passed
  during the game.

  Rules:
  Game will end as soon as you lose by stepping on a mine or you win by making
  the whole play board display "uncovered" (flag, numbers etc except for bombs)
  If you loose, the board will show all the bombs' locations. --> Well, if you
  really want to secure the champion, just populate the whole board with flag:)
  Reminder: Deep grey color means "covered". Light grey color means "uncovered".


- Game logic
  There are a certain number of bombs covered in the board. The player uncovers
  them by clicking the grid without actually stepping on a bomb. If the grid has
  a bomb, the players loses. If the grid has no number displayed, it shows its
  neighbors situation. from 1 to 6, meaning number of bombs near the neighbor. If
  the grid has nothing to show, it will be color light grey. If it is even not
  flipped, it should be color deep grey. You can put flag to make possible bombs
  position. Of course if the grid has a number to show, it will just uncover 
  this grid. Remember: Dark Grey means "covered". Light Grey means "uncovered,
  also no number or bomb in there".

- Game Data Structure
  In this game, I use Node class to stand for my hex grid with two integers.
  Since I choose the axial coordinates, two integers will stand for my hex position.
  Then in order to store the information for each piece of my hex grid, I used
  two HashMaps: namely gameBoardNumber and gameBoardStatus. Though they are created
  in HexMineManager class during Lab 9, they are dealing with the basic logic
  of the game. gameBoardNumber gives number to show, gameBoardStatus updating
  all the time to show "c" as covered, "." as empty, "F" as flags and "M" as bomb.

- Game algorithms
  1. Hex Coordinate Logic
  I use axial coordinates as described on the website, in which case two integers
  are enough for describing the position. Though until I'm writing this README,
  choosing axial coordinates for lab10 seems not a wise decision. I will mention this
  again later in the section "Known bugs and feature requests". So two things to say
  right now: when you are left/right clicking the hex grid, if nothing shows up, 
  please try other parts of this same hex grid. Maybe in the center, maybe upper 
  right part, maybe lower left part. Thanks so much for your patience to test the game :)

  2. Placing random mines:
  I put a random seed in the HexMineManager. Then I loop through the whole game
  board to populate the bombs with the random seed. For the same size of game,
  the positions of mine will be the same. Of course the number of mines would be
  different since I have two (big and small) configurations.

  3. Uncovering cells and neighbors --> Breath First Search (BFS)
  This is the part with much fun. Basically using BFS, we uncover all the neighbors
  until either we see a gameBoardNumber element bigger than zero or we are at
  the edge of the game board. Of course, if gameBoardStatus shows already that
  an number which is above zero is in the click position or it is a "M", then
  BFS will not implement.

  4. Detecting end of game
  The detection of end of game includes 1. you step on a boob (I just realize I
  say bomb and mine from time to time. In this game they mean the very same
  thing) or 2. You uncovered everything. Nothing Deep grey exists on the board
  in other word. In this game, I write a function returning a Boolean to do these
  two things. I think every time I click I will check if the game has ended. case
  one you lose, case two you win.

- Extra points
  A. I used two images 1. bomb and 2. flag to make my game more fun. Be sure to
  check them out. When you click the game, right click to find the flag. You
  will see the bomb picture when you step on them :)

  B. Please increase you music volume to the top to check out my music:) I put a small
  part of music into the game so that when you step on a bomb, you should hear
  several seconds long music. It should be about gun battle sound from a WAV file.

  C. I made some changes to resize the game window so that when you choose two
  configurations, no matter you choose big game or small game, you don't have
  to drag the window to fit its size.

- Known bugs and feature requests
  1. I tried to implement repaint in actionPerformed() function as well as
  mousePressed() function. However, it does not update anything until maybe
  2 hours later. Having no solution to deal with this thing, I build a timer. The
  timer will refresh the game every 0.5 sec so almost the game repainting speed
  finally catches up with the player. Though I kind of feeling this is not
  professional.

  2. I tried to follow the instructions on the website about the transformation
  between hex coordinate and pixel grid. However, I think something not correct
  with either his website sudo codes or my transformation functions. Until now I have
  not found anything wrong with on my side. So if you click the board, at some
  cases the board reacts in a good and quick mode. Sometimes you might need to click
  different parts of this same hex grid on the board because in the background of the
  game, HexMineManager thinks you are clicking in a already covered / out of
  boundary position, which is quite weird. So please please be patient and try the
  differet parts of the same grid if it does not work. You will get there :) Though 
  wired things happen, the whole experience should be OK.

  3. The game was completed on a 15th MacBook Pro therefore I am not one hundred
  percent sure the "right click function" is doing exactly what it was supposed
  to do. It works most of time but sometimes if you "flag" a pixel it might "flag"
  its neighbor. "Two fingers Mode" on Apple OSX stands for "right click". So if 
  you flag a grid, it does not do it ot it does it on its neighbor, please try again.
  Thanks :)

  4. If I have more time I will probably come up with my very own coordinate system
  and add more fancy features like cartoon and music video. Really enjoyed this 
  game design Lab.
