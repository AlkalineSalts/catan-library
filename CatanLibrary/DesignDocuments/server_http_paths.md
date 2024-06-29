HTTP Specification
==================

/game_info
----------
Sends a Json string containing infomatation about the game. It has these fields:

* name - user selected server name

* description - user selected server description

* max_players - maximum number of players allowed

* time_limit - time in milliseconds that each player has to complete their turn. 0 if infinite

* players_joined (nullable) - number of players that have joined the game. null if game has started


/join_game
----------
If a game is open to new players, will give them an authorization token

* 403 - if game has the maximum number of players or game is underway

/send_command
-------------
This is where commands will be submitted. It requires a valid authorization token. It returns 200 regardless whether or not the command is accepted. If it is, it will be sent in the event stream.

* 401 if no authorization header

* 403 if incorrect authorization header

/event_stream
-------------

This is where the client will connect to recieve server side events. Events will come as json formatted strings.

command_type - string which indicates the kind of event being sent

command - a json object containing type-specific command information

Upon connection, events will be sent to the client in order to allow synchronize with the game state.

* 401 if no authorization header

* 403 if incorrect authorization header

