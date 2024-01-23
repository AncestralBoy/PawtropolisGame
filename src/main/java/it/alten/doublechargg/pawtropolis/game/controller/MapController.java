package it.alten.doublechargg.pawtropolis.game.controller;

import it.alten.doublechargg.pawtropolis.game.RoomFactory;
import it.alten.doublechargg.pawtropolis.game.enums.CardinalPoints;
import it.alten.doublechargg.pawtropolis.game.model.Room;
import lombok.Getter;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Component
public class MapController {
    //Prova
    private static final Random RANDOMIZER = new Random();
    private final List<Room> roomList;
    private final RoomFactory roomFactory;

    @Autowired
    private MapController(RoomFactory roomFactory) {
        this.roomFactory = roomFactory;
        roomList = new ArrayList<>();
    }

    public void createMap() {
        val MINIMUM_ROOMS = 8;
        val MAXIMUM_ROOMS = 15;
        final int roomNumber = RANDOMIZER.nextInt(MINIMUM_ROOMS, MAXIMUM_ROOMS + 1);
        for (int i = 0; i <= roomNumber; i++) {
            roomList.add(roomFactory.createRoom());
        }
        for (int i = 0; i < roomNumber - 1; i++) {
            var selectedCardinalPointIndex = RANDOMIZER.nextInt(CardinalPoints.values().length);
            Room currentRoom = roomList.get(i);
            CardinalPoints cardinalPoint = CardinalPoints.values()[selectedCardinalPointIndex];
            while (currentRoom.adjacentRoomExists(cardinalPoint)) {
                selectedCardinalPointIndex = RANDOMIZER.nextInt(CardinalPoints.values().length);
                cardinalPoint = CardinalPoints.values()[selectedCardinalPointIndex];
            }
            connectRooms(CardinalPoints.values()[selectedCardinalPointIndex], roomList.get(i), roomList.get(i + 1));
        }
    }

    public void connectRooms(CardinalPoints cardinalPoint, Room room1, Room room2) {
        room1.addAdjacentRoom(cardinalPoint, room2);
        room2.addAdjacentRoom(CardinalPoints.getOppositeCardinalPoint(cardinalPoint), room1);
    }

}

