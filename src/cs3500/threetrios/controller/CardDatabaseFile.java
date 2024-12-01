package cs3500.threetrios.controller;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.CardModel;

/**
 * To convert a text file of cards to a deck of cards for the model.
 */
public class CardDatabaseFile {
  private final FileReader readFile;

  /**
   * Constructor for CardDatabaseFile.
   *
   * @param file a textfile to be passed in
   */
  CardDatabaseFile(File file) {
    Objects.requireNonNull(file);
    try {
      readFile = new FileReader(file);
    }
    catch (FileNotFoundException e) {
      throw new IllegalArgumentException("File path or type is invalid");
    }
  }

  /**
   * Constructs a deck to be passed into the ThreeTrios models.
   *
   * @return List of Cards that represents the deck
   */
  public List<Card> makeDeck() {
    Scanner scan = new Scanner(readFile);
    List<Card> deck = new ArrayList<>();


    while (scan.hasNext()) {
      Card card = makeCard(scan);
      deck.add(card);
    }

    if (checkUnique(deck)) {
      throw new IllegalArgumentException("cards do not have unique names");
    }
    else {
      return deck;
    }
  }


  /**
   * Makes a card based on the scanners input.
   *
   * @param scan input scanner
   * @return a Card based on the next 5 scanner inputs
   */
  private static Card makeCard(Scanner scan) {
    String info = "";
    String name = "";
    String north = "";
    String south = "";
    String east = "";
    String west = "";

    for (int lineLen = 1; lineLen <= 5; lineLen++) {

      try {
        info = scan.next();
      }
      catch (NoSuchElementException e) {
        throw new IllegalArgumentException("File is not formatted properly");
      }

      if (lineLen == 1) {
        name = info;
      }
      if (lineLen == 2) {
        north = info;
      }
      if (lineLen == 3) {
        south = info;
      }
      if (lineLen == 4) {
        east = info;
      }
      if (lineLen == 5) {
        west = info;
      }
    }

    return new CardModel(name, north, south, east, west);
  }

  /**
   * Checks that all the card names are unique.
   *
   * @param deck the deck full of cards
   * @return boolean of whether the cards are unique or not
   */
  private static boolean checkUnique(List<Card> deck) {
    List<String> names = new ArrayList<>();

    for (Card card : deck) {
      String name = card.getName();

      if (names.contains(name)) {
        return true;
      } else {
        names.add(name);
      }
    }

    return false;
  }
}

