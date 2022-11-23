GwentStone - 2ndYear - Poo - Rădulescu Matei


This java programm implements a basic card game with the following rules:

Two players start playing as one of the 4 implemented heroes and a deck
of cards, both given as imputs. All heroes have a special ability that
affects the table of minions in some way. 

In their deck they hold two type of cards:

Minions: The minion cards need to be placed on a game table
of 4x5 size, two rows are assigned to each player. This minions can attack
each other, or the enemy hero or the one's that do have an ability
can use it.

Environment: This cards affect the table of minions in some way similar to
the hero cards abilities.


From the input classes which have the data from the given json files in them,
information is moved to auxilaries classes used for implementing the game.

This functionalities are implemented using a abstract class called card,
which is extended into 3 diffent classes, representing the hero, the minions
and the enviornment card types.

The minion and environment cards are than stored inside several deck classes.
This deck classes stored in a class called GameInstances which is used to play
all of the games given as input.

When a game starts a new class of the type GameTable is created in which each
player's chosen deck is sent as well as the hero they will be using for the current
game. Together with those a array of Actions, a class which stores the commands of
the game, which are than executed so that the game may procced.

The table were the minion need to be placed is implemented as and ArrayList which
contains four other ArrayLists where the minions are to be played.

Some of the basic commands are implemented directly in the Action class, by sending
the information to the output json file through an auxiliary ObjectNode. The more
complex one's ( attack, use ability, place minion, end turn ...) are implemented
trrough their own methods that belong to the GameTable class.

For executing multiple games every time the decks are reparsed every time, and three
variables are used for statistic purposes such as games won by each player and total
number of games played.

More information can be found in the javadocs and comments placed in the code.