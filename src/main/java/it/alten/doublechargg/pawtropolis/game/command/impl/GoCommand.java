package it.alten.doublechargg.pawtropolis.game.command.impl;

import it.alten.doublechargg.pawtropolis.game.command.interfaces.CommandWithParam;
import it.alten.doublechargg.pawtropolis.game.controller.GameController;
import it.alten.doublechargg.pawtropolis.game.enums.CardinalPoints;
import it.alten.doublechargg.pawtropolis.game.model.Player;
import it.alten.doublechargg.pawtropolis.game.model.Room;

import java.util.Objects;
public class GoCommand implements CommandWithParam {

    private GameController gameController;
    private Player player;
    private Room currentRoom;

    public GoCommand(GameController gameController) {
        this.gameController = gameController;
        player = gameController.getPlayer();
        currentRoom = gameController.getCurrentRoom();

    }

    @Override
    public String execute(String arg) {
        CardinalPoints cardinalPoint = CardinalPoints.findByName(arg);
        if (Objects.isNull(cardinalPoint)) {
            return "Not valid input";
        }
        if (currentRoom.adjacentRoomExists(cardinalPoint)) {
            gameController.setCurrentRoom(currentRoom.getAdjacentRoomByCardinalPoint(cardinalPoint));
            return String.format("%s entered the room %s%n", player.getName(), gameController.getCurrentRoom().toString());
        }
        return "Not existent room";

    }
}
