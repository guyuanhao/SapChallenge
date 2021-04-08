package com.sap.cic.code.challenge;

import com.sap.cic.code.challenge.util.HandValueChecker;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Game {
    private List<Card> cardList;

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
        if (HandValueChecker.checkRoyalFlush(cardList)) {
            return Hand.ROYAL_FLUSH;
        }
        if (HandValueChecker.checkStraightFlush(cardList)) {
            return Hand.FLUSH;
        }
        if (HandValueChecker.checkFourOfKind(cardList)) {
            return Hand.FOUR_OF_A_KIND;
        }
        if (HandValueChecker.checkFullHouse(cardList)) {
            return Hand.FULL_HOUSE;
        }
        if (HandValueChecker.checkFlush(cardList)) {
            return Hand.FLUSH;
        }
        if (HandValueChecker.checkStraight(cardList)) {
            return Hand.STRAIGHT;
        }
        if (HandValueChecker.checkThreeOfKind(cardList)) {
            return Hand.THREE_OF_A_KIND;
        }
        if (HandValueChecker.checkTwoPairs(cardList)) {
            return Hand.TWO_PAIRS;
        }
        if (HandValueChecker.checkPair(cardList)) {
            return Hand.PAIR;
        }
        if (HandValueChecker.checkHighcard(cardList)) {
            return Hand.HIGHCARD;
        }
        log.error("Unknown error, cannot show hand");
        return Hand.UNKNOWN;
    }
}
