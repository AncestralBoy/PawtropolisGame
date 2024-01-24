package it.alten.doublechargg.pawtropolis.game.controller;

import it.alten.doublechargg.pawtropolis.game.RoomFactory;
import it.alten.doublechargg.pawtropolis.game.enums.CardinalPoints;
import it.alten.doublechargg.pawtropolis.game.model.Item;
import it.alten.doublechargg.pawtropolis.game.model.Room;
import lombok.Getter;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Getter
@Component
public class MapController {
    private static final Random RANDOMIZER = new Random();
    private final List<Room> roomList;
    private final RoomFactory roomFactory;
    private final Item[] keys = {
            new Item(4L, "Penny", "a penny", 1),
            new Item(5L, "Key", "a key", 1),
            new Item(6L, "Bomb", "a bomb", 3),
            new Item(7L, "Golden Key", "a golden key", 10)
    };
    private final Map<Item, Room> lockedRoomMap;

    @Autowired
    private MapController(RoomFactory roomFactory) {
        this.roomFactory = roomFactory;
        roomList = new ArrayList<>();
        lockedRoomMap = new HashMap<>();
    }

    public void createMap() {
        val MINIMUM_ROOMS = 1;
        val MAXIMUM_ROOMS = 8;
        final int roomNumber = RANDOMIZER.nextInt(MINIMUM_ROOMS, MAXIMUM_ROOMS + 1);
        for (int i = 0; i < roomNumber; i++) {
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

        List<Integer> lockedRoomIndexes = new ArrayList<>();
        val MAXIMUM_LOCKED_ROOMS = 4;
        final int lockedRoomNumber = RANDOMIZER.nextInt(MAXIMUM_LOCKED_ROOMS);
        for (int i = 0; i < lockedRoomNumber; i++) {
            int lockedRoomIndex = RANDOMIZER.nextInt(roomNumber);
            lockedRoomIndexes.add(lockedRoomIndex);
            roomList.get(lockedRoomIndex).setLockedDoor(true);
            int keyRoomIndex = RANDOMIZER.nextInt(lockedRoomIndex);
            roomList.get(keyRoomIndex).addItem(keys[i]);
            lockedRoomMap.put(keys[i], roomList.get(lockedRoomIndex));
        }
    }

    public void connectRooms(CardinalPoints cardinalPoint, Room room1, Room room2) {
        room1.addAdjacentRoom(cardinalPoint, room2);
        room2.addAdjacentRoom(CardinalPoints.getOppositeCardinalPoint(cardinalPoint), room1);
    }
}

