# OOP2
Project has a class Game that allows playing Mafia with interfaces for bot players.
Class main.Player is a class designed to make choices about the game.
It does not have an access to hidden information.
There is class main.Role with many subclasses depicting different roles in Mafia.
They also have access to main.PlayerGameStatus, which has a full data of characteristics of a player, even hidden.
main.Role uses this status to solve any conflicts.
main.Role and main.Player wrapped in a main.PlayerWrapper that is used by the Game to call on any decisions.
Also there are main.RecordsObj and main.RecordObj classes that have information main.Player class have access to.

