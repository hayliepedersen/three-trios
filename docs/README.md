# Overview

**Problem Statement**

This codebase implements a card game, *Three Trios*, designed to be played on a customizable 
rectangular grid. The game requires players to place and compete with cards across different phases,
with the goal of achieving dominance on the grid.

**High-Level Assumptions**
1. **Background Knowledge**: The code assumes familiarity with basic game design concepts such as 
turn-taking, grid-based layouts, and card games with specific rules for placing and competing.
2. **Grid and Deck Requirements**: The grid must have an odd number of cells to allow 
for a balanced setup, and the deck must be pre-populated with a sufficient number of cards 
to support the required grid cell count plus player hands.
3. **Extensibility**: The code is structured to support different grid sizes and alternative 
game phases or card types, making it possible to expand rules and gameplay mechanics 
with minimal changes to the existing architecture.
4. **Prerequisites**: Users and developers should have a basic understanding of Java 
programming to interact with the codebase, set up custom decks, or adjust gameplay settings.

# Quick Start
To get started with the ThreeTriosModel game, follow these steps:

- From the command line, navigate to the configurations folder, then run 
`java -jar ThreeTrios.jar human human `
- To play with machine players (See `Important Note` section below), arguments 'flips' and 
'corners' can be used in placed of 'human'

# Key Components

### ThreeTriosModel
The `ThreeTriosModel` class serves as the core game model, handling the main gameplay mechanics, 
including:
- **Grid management**: Setting up and enforcing valid card cells and hole placements.
- **Player turns**: Alternating turns between `Player.RED` and `Player.BLUE`.
- **Card Placing**: Adding cards to the cells of the grids, while ensuring proper gameplay.
- **Battle and combo phases**: Managing adjacent card battles and flipping cards based on 
directional values.
- **Validation checks**: Ensuring the grid structure and card ownership rules are maintained.

### Card and Cell Classes
- **Card**: Represents an individual card with properties such as owner and directional values, 
which determine battle strengths.
- **Cell**: Represents a position on the grid, which can either be a hole (non-playable) or a 
card cell (playable). Card cells may contain cards or remain empty.

### Player Enum
Defines the two players, `Player.RED` and `Player.BLUE`, and tracks which player's turn it is.

### ThreeTriosGraphicsView
The `ThreeTriosGraphicsView` class serves as the class where the GUI can be viewed from. 

### AbstractGamePanel
- **GridPanel**: draws the grid, with different colors for holes and card-cells, and an
inner mouseclick class
which determines which card the player clicked on.
- **PlayerPanel**: draws the two players hands, with different colors for RED and BLUE, and 
an inner mouseclick class
that highlights the card the player clicked on.

### TriosController
Implements the ViewFeatures and ModelObservers interfaces. Handles gameplay for two players.

# Key Subcomponents

### Card Directions
Each card has directional strengths (North, South, East, West) stored in a configuration map. 
These values are compared during the battle phase to determine the outcome.

### Battle Phase
The `battlePhase` method initiates a battle sequence between a placed card and its adjacent cards. 
If a placed card flips an opponent’s card, it triggers a "combo step" where the newly flipped card
battles its surrounding cards.

### Game Rules and Invariants
Game rules ensure that:
- The grid has an odd number of card cells.
- The deck has enough cards to fill the grid plus one additional card.
- Card ownership is validated after each turn to maintain correct player control.

# Source Organization

- **docs/**: Contains project documentation, including the README, strategy transcript, 
and configuration directory. 
  - **configuration/**: Contains the configuration files and the JAR file
- **media/**: Contains screenshots of the game at four different stages:
  - At the start of the game 
  - With a card selected from the Red Player’s hand 
  - With a card selected from the Blue Player’s hand  
  - At a non-trivial intermediate point of the game, meaning cards have been played 
  and the hands of both players have decreased
- **src/**: Primary source folder with all core game logic and architecture.
  - **controller/**: Manages user inputs and interactions, coordinating between the model and view
  layers to handle gameplay logic and player commands.
  - **model/**: Core game data and logic, including classes for game state management 
  (`ThreeTriosModel`), card properties, player information, and grid setup.
  - **view/**: Handles all user-facing components and interactions, 
  responsible for rendering the game state and displaying gameplay updates, 
  either in the console or graphical interface (in the future).
- **test/**: JUnit tests that validate the functionality and integrity of the game. 
Organized by `model`, `view`, and `controller` packages to test each component 
independently and ensure they work together as expected.

# Changes for part 2

- Made changes to the original model and model interface, including:
  - Changed getGrid to getGridCopy, to avoid mutation mistakes, returns a copy of the grid rather 
than the original grid itself.
  - Changed `getBlueHand()` and `getRedHand()` to return copies of each hand instead of the actual 
  hand field
  - Added a `ReadOnlyTriosModel` interface
  - Added the following methods:
    ````java
    int getRows();
    int getCols();
    Cell getCellAt(int row, int col);
    Player getCellOwner(int row, int col);
    boolean isLegalPlay(int row, int col);
    int numOfFlippableCards(int row, int col);
    int getScore(Player player);
    public List<Card> getPlayerHand(Player player);
    
  - Updated `determineWinner()` method to use new `getScore()` method, adds clarity better
  demonstrates the rules for determining a winner in the `determinWinner()` method
  - Updated `placeCard()` method to use new `isLegalPlay()` method, better demonstrates
  when a player should or should not be able to place a card, increases readability
  - Updated `ThreeTriosView` to take in a `ReadOnlyTriosModel`, ensures that the view cannot
  modify the model in any way
- Updated the view interface to be able to handle multiple implementations by
  changing the `toString` method to be a `render()` method
- Added documentation to the `Card` and `Hole` classes
- Added methods to `Card` interface to get the attack values of specific directions

# Changes for Part 3

## Important Note
When configured with a machine player, one would have to 'act' as a machine player by selecting a 
card from the hand for that machine player, then click anywhere on the main grid. Once clicked
on the main grid, the machine player will correctly place the card to the best possible position
based on it's configured strategy. While this is not the complete gameplay for a machine player,
it's important to note that it is clear that the machine 
player is in fact selecting a cell based off of its strategy.

### Model
- added a `startGame()` method to `TriosModel`, as well as an `isStarted` field and `isStarted()` 
method in `ReadOnlyTriosModel`
- added a `isPlayerTurn()` method to `ReadOnlyTriosModel`
- added the methods, `endTurn()` and `isPlayerTurn()` to be able to better keep track of 
model events
- added an `addObservers()` method to be able to notify the controller of game events
  - Called observer methods when game events occur such as in the `determineWinner()` method

### Features
- added two features/observers interfaces:
  - **ModelObservers**: Handles model notifications, which are added to the model class and sent
  to the controller.
  - **ViewFeatures**: Handles view events, which are called in the GUI classes, and sent back to 
  the controller.

### Players
- added interface for players `AnyPlayer`
- added class `HumanPlayer`, which represents the human player
- added class `MachinePlayer`, which represents the machine player
- implemented `makeMove()` method that uses dynamic dispatch to make a move for either a human
or machine player

### TriosController
- class that implements the `ViewFeatures` and `ModelObservers` interfaces
- handles gameplay and updates the GUI as players interact with it

### TriosView
- added a `showMessage()` method to be able to display a message to the user through the GUI
- added viewFeatures fields to the GridPanel, PlayerPanel, and ThreeTriosGraphicsView classes
so that the viewFeatures can be used from the controller
- added an `addFeatures()` method to ThreeTriosGraphicsView so that viewFeatures can be added from 
the controller to the view

### `ThreeTrios` 
- updated main to have two views, two controllers, and two players that are configured based off
of the new `configurePlayer()` method which sets up players based off of command-line arguments.

# Extra Credit Notes

### Level 0

To enable hints, a player can hit the 'H' key on their turn, and press again to disable.

- To play the game without the possibility of hints, one can modify the following section of the
`ThreeTriosGraphicView` constructor, to use the typical `gridPanel` 
instead of the `hintDecoratedPanel`.
  ````java
  mainPanel.add(redPlayerPanel, FlowLayout.LEFT);
  mainPanel.add(hintDecoratedPanel, FlowLayout.CENTER);
  mainPanel.add(bluePlayerPanel, FlowLayout.RIGHT);

### Level 1 & 2

#### Which files were affected
The original model class was updated in various areas to switch private accessors to be protected. 
No other changes to the original model occurred.

There is a new model for level 1, represented by the `VariantOneModel` class. This class extends
the original `ThreeTriosModel` class and handles new battle phase logic accordingly.

Our `ThreeTrios` runner class was modified to handle new arguments, configuring either the original
`ThreeTriosModel` if no additional commands are given, or a `VariantOneModel` otherwise.