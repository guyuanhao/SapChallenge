package com.sap.cic.code.challenge;

import com.sap.cic.code.challenge.util.ErrorCode;
import com.sap.cic.code.challenge.util.HandValueChecker;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Game {
    private List<Card> cardList;
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
        ErrorCode errorCode = handValueChecker.checkLegality(cardList);
        switch (errorCode) {
            case SUCC:
                break;
            case CARD_LIST_ERROR:
                throw new GameException("cardList unknown error");
            case CARD_VALUE_ERROR:
                throw new GameException("cardList contains duplicate card");
            default:
                throw new GameException("unknown error");
        }
        if (handValueChecker.checkRoyalFlush(cardList)) {
            return Hand.ROYAL_FLUSH;
        }
        if (handValueChecker.checkStraightFlush(cardList)) {
            return Hand.STRAIGHT_FLUSH;
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
