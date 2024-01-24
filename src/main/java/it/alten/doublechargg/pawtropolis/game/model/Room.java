package it.alten.doublechargg.pawtropolis.game.model;

import it.alten.doublechargg.pawtropolis.animals.model.abstracts.Animal;
import it.alten.doublechargg.pawtropolis.game.enums.CardinalPoints;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class Room {

    private final List<Item> items;
    private final List<Animal> animals;
    private final EnumMap<CardinalPoints, Room> adjacentRooms;
    @Setter
    private boolean lockedDoor;
    @Getter
    @Setter
    private String name;

    public Room(String name) {
        this.name = name;
        items = new ArrayList<>();
        animals = new ArrayList<>();
        adjacentRooms = new EnumMap<>(CardinalPoints.class);
        lockedDoor = false;
    }

    public int getItemsNumber() {
        return items.size();
    }

    public boolean addItem(Item item) {
        return items.add(item);
    }

    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    public boolean containsItem(Item item) {
        return items.contains(item);
    }

    public void addAdjacentRoom(CardinalPoints cardinalPoint, Room room) {
        adjacentRooms.put(cardinalPoint, room);
    }

    public void removeAdjacentRoom(CardinalPoints cardinalPoint) {
        adjacentRooms.remove(cardinalPoint);
    }

    public boolean adjacentRoomExists(CardinalPoints cardinalPoint) {
        return Objects.nonNull(adjacentRooms.get(cardinalPoint));
    }

    public int getAnimalNumber() {
        return animals.size();
    }

    public boolean addAnimal(Animal animal) {
        return animals.add(animal);
    }

    public boolean removeItem(Animal animal) {
        return animals.remove(animal);
    }

    public Room getAdjacentRoomByCardinalPoint(CardinalPoints cardinalPoint) {
        return adjacentRooms.get(cardinalPoint);
    }


    public Item getItemByName(String name) {
        return items.stream()
                .filter(item -> item.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    private String getItemsListAsString() {
        return items.stream()
                .map(Item::name)
                .collect(Collectors.joining(", "));
    }

    private String getAnimalsListAsString() {
        return animals.stream()
                .map(animal -> String.format("%s(%s)", animal.getName(), animal.getClass().getSimpleName()))
                .collect(Collectors.joining(", "));

    }

    private String getAdjacentRoomListAsString() {
        return adjacentRooms.keySet()
                .stream()
                .map(CardinalPoints::getName)
                .collect(Collectors.joining(", "));

    }

    @Override
    public String toString() {
        return String.format("\n" +
                        "You are in room %s%n" +
                        "Items: %s%n" +
                        "NPC: %s%n" +
                        "Doors: %s",
                name, getItemsListAsString(), getAnimalsListAsString(), getAdjacentRoomListAsString());
    }
}
