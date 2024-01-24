package it.alten.doublechargg.pawtropolis.game.controller;

import it.alten.doublechargg.pawtropolis.game.CommandFactory;
import it.alten.doublechargg.pawtropolis.game.command.interfaces.CommandWithoutParam;
import it.alten.doublechargg.pawtropolis.game.exceptions.CommandFailedException;
import it.alten.doublechargg.pawtropolis.game.model.Item;
import it.alten.doublechargg.pawtropolis.game.model.Player;
import it.alten.doublechargg.pawtropolis.game.model.Room;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Log
@Component
public class GameController implements CommandLineRunner {

    private static final Scanner scanner = new Scanner(System.in);
    @Getter
    private final MapController mapController;
    private final InputController inputController;
    private final CommandFactory commandFactory;
    @Setter
    @Getter
    private boolean gameEnded;
    @Setter
    @Getter
    private Player player;
    @Setter
    @Getter
    private Room currentRoom;

    @Autowired
    public GameController(InputController inputController, MapController mapController, CommandFactory commandFactory) {
        this.gameEnded = false;
        this.mapController = mapController;
        this.inputController = inputController;
        this.commandFactory = commandFactory;
    }

    public Player createPlayer() {
        log.info("Choose your name");
        return new Player(scanner.nextLine());
    }

    public void startGame() {
        mapController.createMap();
        setPlayer(createPlayer());
        setCurrentRoom(mapController.getRoomList().getFirst());
        try {
            log.info(((CommandWithoutParam) commandFactory.createCommand("Help")).execute());
        } catch (CommandFailedException e) {
            e.printStackTrace();
        }
        while (!gameEnded) {
            log.info("Write your action");
            try {
                inputController.handleInput(scanner.nextLine());
            } catch (CommandFailedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run(String... args) {
        startGame();
    }
}

