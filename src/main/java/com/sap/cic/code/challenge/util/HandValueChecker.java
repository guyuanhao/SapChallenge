package com.sap.cic.code.challenge.util;

import com.sap.cic.code.challenge.Card;
import com.sap.cic.code.challenge.GameException;

import java.util.*;

/**
 * Check specific value
 * Singleton pattern
 */
public class HandValueChecker {
    // todo 修复bug最优解的5张牌而不是7张牌一起考虑

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

    public boolean checkFullHouse(List<Card> cardList) throws GameException {
        return checkThreeOfKind(cardList) && checkPair(cardList);
    }

    public boolean checkFlush(List<Card> cardList) {
        for (int i=0; i<cardList.size()-1; i++) {
            if (!cardList.get(i).suit.equals(cardList.get(i+1).suit)) {
                return false;
            }
        }
        return true;
    }

    public boolean checkStraight(List<Card> cardList) {
        List<Integer> cardKindValueList = new ArrayList<>();
        cardList.forEach(card -> cardKindValueList.add(card.getKind().rank));
        Collections.sort(cardKindValueList);
        for (int i=0; i<cardKindValueList.size()-1; i++) {
            if (cardKindValueList.get(i+1) - cardKindValueList.get(i) != 1) {
                // FIXME 这里应该要考虑A作为2的情况，可是题目定义已经有2这个点数了
                /*
                如果考虑上述情况应该再多加条件如下
                    && !(cardKindValueList.get(i) == Card.Kind.ACE.value && cardKindValueList.get(i+1) == Card.Kind.THREE.value)
                 */
                // Ace can precede 2 and follow up King, so A3456 should be ignore
                return false;
            }
        }
        return true;
    }

    public boolean checkThreeOfKind(List<Card> cardList) throws GameException {
        return getMaxKinds(cardList) == 3;
    }

    public boolean checkTwoPairs(List<Card> cardList) {
        Map<Card.Kind, Integer> kindIntegerMap = transferToKindMap(cardList);
        return kindIntegerMap.entrySet().stream()
                .filter(kindIntegerEntry -> kindIntegerEntry.getValue() == 2)
                .count() == 2;
    }

    public boolean checkPair(List<Card> cardList) throws GameException {
        return getMaxKinds(cardList) == 2;
    }

    public boolean checkHighcard(List<Card> cardList) {
        return true;
    }

    private int getMaxKinds(List<Card> cardList) throws GameException {
        Map<Card.Kind, Integer> kindIntegerMap = transferToKindMap(cardList);
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

    private Map<Card.Kind, Integer> transferToKindMap(List<Card> cardList) {
        Map<Card.Kind, Integer> kindIntegerMap = new HashMap<>();
        cardList.forEach(card -> {
            int existNumber = kindIntegerMap.getOrDefault(card.getKind(), 0);
            existNumber++;
            kindIntegerMap.put(card.getKind(), existNumber);
        });
        return kindIntegerMap;
    }

}
