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
    private final List<Integer> lockedRoomIndexes;
    private final Map<Item, Room> lockedRoomMap;

    @Autowired
    private MapController(RoomFactory roomFactory) {
        this.roomFactory = roomFactory;
        roomList = new ArrayList<>();
        lockedRoomMap = new HashMap<>();
        lockedRoomIndexes = new ArrayList<>();
    }

    public void createMap() {
        val MINIMUM_ROOMS = 4;
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
        setLockedRooms(roomNumber);
        addKeyObjectsInRooms();

    }

    public void connectRooms(CardinalPoints cardinalPoint, Room room1, Room room2) {
        room1.addAdjacentRoom(cardinalPoint, room2);
        room2.addAdjacentRoom(CardinalPoints.getOppositeCardinalPoint(cardinalPoint), room1);
    }

    public void setLockedRooms(int roomNumber){
        val MAXIMUM_LOCKED_ROOMS = 4;
        final int lockedRoomNumber = RANDOMIZER.nextInt(1,MAXIMUM_LOCKED_ROOMS);
        for (int i = 0; i < lockedRoomNumber; i++) {
            int lockedRoomIndex = RANDOMIZER.nextInt(1, roomNumber);
            lockedRoomIndexes.add(lockedRoomIndex);
            roomList.get(lockedRoomIndex).setLockedDoor(true);
        }
    }

    public void addKeyObjectsInRooms(){
        Item[] keyObjects = {
                new Item(4L, "Penny", "a penny", 1),
                new Item(5L, "Key", "a key", 1),
                new Item(6L, "Bomb", "a bomb", 3),
                new Item(7L, "Picket", "a picket", 10)
        };

        for(int i = 0; i < lockedRoomIndexes.size(); i ++){
            int currentKeyObjectRoomIndex = RANDOMIZER.nextInt(lockedRoomIndexes.get(i));
            Item currentRandomizedKeyObject = keyObjects[RANDOMIZER.nextInt(keyObjects.length)];
            roomList.get(currentKeyObjectRoomIndex).addItem(currentRandomizedKeyObject);
            lockedRoomMap.put(currentRandomizedKeyObject, roomList.get(lockedRoomIndexes.get(i)));
        }
    }

    public Room getRoomFromItem(Item item){
        return lockedRoomMap.get(item);
    }


}

