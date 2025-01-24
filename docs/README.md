# Three Trios

This codebase implements a card game, *Three Trios*, designed to be played on a customizable 
rectangular grid. The game requires players to place and compete with cards across different phases,
with the goal of achieving dominance on the grid.

# Quick Start
To play the game:

From the command line, navigate to the configurations folder (`cd docs` -> `cd configurations`), then run 
`java -jar ThreeTriosEC.jar human human`

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

# Video Demo

https://drive.google.com/file/d/1qXuCjDWn-egSYrk64P4RIfutI_tGHtBM/view?usp=sharing 
