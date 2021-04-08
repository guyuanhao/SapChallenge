package com.sap.cic.code.challenge;

import com.sap.cic.code.challenge.util.HandValueChecker;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Game {
    private List<Card> cardList;
    private static Integer DECK_SIZE = 7;
    private HandValueChecker handValueChecker = HandValueChecker.getInstance();

    public static Game create() {
        return new Game();
    }

    private Game() {
        this.cardList = new ArrayList<>();
    }

    public void add(List<Card> cards) {
        cardList.addAll(cards);
    }

    //EDIT ME PLEASE!
    public Hand showHand() throws GameException {
        if (cardList == null || cardList.size()!=DECK_SIZE) {
            throw new GameException("cardList unknown error");
        }
        if (handValueChecker.checkRoyalFlush(cardList)) {
            return Hand.ROYAL_FLUSH;
        }
        if (handValueChecker.checkStraightFlush(cardList)) {
            return Hand.FLUSH;
        }
        if (handValueChecker.checkFourOfKind(cardList)) {
            return Hand.FOUR_OF_A_KIND;
        }
        if (handValueChecker.checkFullHouse(cardList)) {
            return Hand.FULL_HOUSE;
        }
        if (handValueChecker.checkFlush(cardList)) {
            return Hand.FLUSH;
        }
        if (handValueChecker.checkStraight(cardList)) {
            return Hand.STRAIGHT;
        }
        if (handValueChecker.checkThreeOfKind(cardList)) {
            return Hand.THREE_OF_A_KIND;
        }
        if (handValueChecker.checkTwoPairs(cardList)) {
            return Hand.TWO_PAIRS;
        }
        if (handValueChecker.checkPair(cardList)) {
            return Hand.PAIR;
        }
        if (handValueChecker.checkHighcard(cardList)) {
            return Hand.HIGHCARD;
        }
        // this is not possible to happen at the current stage
        log.error("Unknown error, cannot show hand");
        return Hand.UNKNOWN;
    }
}
