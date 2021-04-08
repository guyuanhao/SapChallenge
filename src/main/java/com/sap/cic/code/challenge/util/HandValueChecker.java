package com.sap.cic.code.challenge.util;

import com.sap.cic.code.challenge.Card;
import com.sap.cic.code.challenge.GameException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Check specific value
 * Singleton pattern
 */
public class HandValueChecker {

    private static HandValueChecker handValueCheckerInstance = null;

    private HandValueChecker() {
    }

    public static HandValueChecker getInstance() {
        if (handValueCheckerInstance == null) {
            return new HandValueChecker();
        } else {
            return handValueCheckerInstance;
        }
    }

    public boolean checkRoyalFlush(List<Card> cardList) {
        if (!checkFlush(cardList)) {
            return false;
        }
        List<Card.Kind> kinds = new ArrayList<>();
        cardList.forEach(card -> kinds.add(card.getKind()));
        return kinds.contains(Card.Kind.ACE)
                && kinds.contains(Card.Kind.KING)
                && kinds.contains(Card.Kind.QUEEN)
                && kinds.contains(Card.Kind.JACK)
                && kinds.contains(Card.Kind.TEN);
    }

    public boolean checkStraightFlush(List<Card> cardList) {
        return checkStraight(cardList) && checkFlush(cardList);
    }

    public boolean checkFourOfKind(List<Card> cardList) throws GameException {
        return getMaxKinds(cardList) == 4;
    }

    public boolean checkFullHouse(List<Card> cardList) {
        // todo
        return false;
    }

    public boolean checkFlush(List<Card> cardList) {
        // todo
        return false;
    }

    public boolean checkStraight(List<Card> cardList) {
        // todo
        return false;
    }

    public boolean checkThreeOfKind(List<Card> cardList) throws GameException {
        return getMaxKinds(cardList) == 3;
    }

    public boolean checkTwoPairs(List<Card> cardList) {
        // todo
        return false;
    }

    public boolean checkPair(List<Card> cardList) throws GameException {
        return getMaxKinds(cardList) == 2;
    }

    public boolean checkHighcard(List<Card> cardList) {
        return true;
    }

    private int getMaxKinds(List<Card> cardList) throws GameException {
        Map<Card.Kind, Integer> kindIntegerMap = new HashMap<>();
        cardList.forEach(card -> {
            int existNumber = kindIntegerMap.getOrDefault(card.getKind(), 0);
            existNumber++;
            kindIntegerMap.put(card.getKind(), existNumber);
        });
        Integer maxKind = 1;
        for (Map.Entry<Card.Kind, Integer> entrySet: kindIntegerMap.entrySet()) {
            if (entrySet.getValue() > maxKind) {
                maxKind = entrySet.getValue();
            }
        }
        if (maxKind > 4) {
            throw new GameException("Someone is cheating cause got maxKind: " + maxKind);
        }
        return maxKind;
    }
}
