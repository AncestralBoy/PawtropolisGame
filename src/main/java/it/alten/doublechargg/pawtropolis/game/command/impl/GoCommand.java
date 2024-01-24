package it.alten.doublechargg.pawtropolis.game.command.impl;

import it.alten.doublechargg.pawtropolis.game.command.interfaces.CommandWithParam;
import it.alten.doublechargg.pawtropolis.game.controller.GameController;
import it.alten.doublechargg.pawtropolis.game.controller.MapController;
import it.alten.doublechargg.pawtropolis.game.enums.CardinalPoints;
import it.alten.doublechargg.pawtropolis.game.model.Item;
import it.alten.doublechargg.pawtropolis.game.model.Player;
import it.alten.doublechargg.pawtropolis.game.model.Room;

import java.util.Objects;
import java.util.Scanner;

public class GoCommand implements CommandWithParam {

    private final GameController gameController;
    private final Player player;
    private final Room currentRoom;
    private final MapController mapController;
    private static final Scanner scanner = new Scanner(System.in);

    public GoCommand(GameController gameController) {
        this.gameController = gameController;
        mapController = gameController.getMapController();
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
            if(currentRoom.getAdjacentRoomByCardinalPoint(cardinalPoint).isLockedDoor()){
                System.out.println("The door is locked: would you like to use an item to unlock it?");
                String answer = scanner.nextLine();
                switch (answer.toLowerCase()){
                    case "y":
                        System.out.println("Type the name of the chosen item");
                        String itemName = scanner.nextLine();
                        Item item =  player.getItemFromBag(itemName);
                        if(item != null ){
                            if(mapController.getRoomFromItem(item) != null){
                                if(mapController.getRoomFromItem(item).equals(currentRoom.getAdjacentRoomByCardinalPoint(cardinalPoint))){
                                    currentRoom.getAdjacentRoomByCardinalPoint(cardinalPoint).setLockedDoor(false);
                                    System.out.println("You unlocked the door!");
                                    player.removeItem(item);
                                    gameController.setCurrentRoom(currentRoom.getAdjacentRoomByCardinalPoint(cardinalPoint));
                                    return String.format("%s entered the room %s%n", player.getName(), gameController.getCurrentRoom().toString());
                                }
                            }
                            else{
                                return "This is not the right item";
                            }
                        }
                        else{
                            return "You don't have this Item";
                        }
                    case "n":
                        return null;
                    default:
                        return "Incorrect choice";
                }
            }
            else{
                gameController.setCurrentRoom(currentRoom.getAdjacentRoomByCardinalPoint(cardinalPoint));
                return String.format("%s entered the room %s%n", player.getName(), gameController.getCurrentRoom().toString());
            }
        }
        return "Not existent room";
    }
}
