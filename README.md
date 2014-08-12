turbo-tribble
=============

the Virtual Tabletop Character Position Tracker

PURPOSE
To create a program that will allow remotely located players to participate in D&D gaming sessions by providing a virtual battlemat controlled by the DM and visible to the players.

SCOPE
-Initially, the scope of the program's functionality will be limited to replicating the functionality of a tabletop battlemat: the ability to represent the battle environment (i.e., obstacles and boundaries, zones, etc.) and the ability to keep visual track of the positions of PCs and NPCs relative to one another and the aforementioned battlefield obstacles and boundaries.  In due time, the possibility of expanding functionality to incorporate combat tracking (initiative order, character status effects and HP, Player actions, etc.) may be considered.

FEATURE BREAKDOWN (in progress; still gathering requirements)
-interface
-battlemat
 -character tokens
 -terrain
 -obstacles
 -boundaries
-networking
 -must support multiple simultaneous Internet connections for a number of Spectators (at this stage Players won't be making changes, so there is no need to differentiate between Players and non-player Spectators), and one (or more?) DMs
 -will utilize the client-server model--the client set to be DM will login to the server and prep the session for the Spectator/Player clients
-control
 -DM must be able to create, save, load, view, modify, and run maps
 -DM must be able to move PC and NPC icons (or tokens) around the board freely
 -DM must be able to create and dismiss effect zones and other changes to terrain on the fly
