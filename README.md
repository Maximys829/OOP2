# OOP2
Project has a class Game that allows playing Mafia with interfaces for bot players.
Class Player is a class designed to make choices about the game.
It does not have an access to hidden information.
There is class Role with many subclasses depicting different roles in Mafia.
They also have access to PlayerGameStatus, which has a full data of characteristics of a player, even hidden.
Role uses this status to solve any conflicts.
Role and Player are wrapped in a PlayerWrapper that is used by the Game to call on any decisions.
Also there are RecordsObj and RecordObj classes that have information that Player class have access to.
Also there is supporting class Cond to help Game track phase of the game.
Also there is a class GameLog that made specifically for a master of the game. It is logging every meaningful activation of ability.
Directory config has 2 files for list of quantity and types of all roles in the game and list of awakening order in the night
BotRandom is a simple subclass of a Player that acts purely randomly.

