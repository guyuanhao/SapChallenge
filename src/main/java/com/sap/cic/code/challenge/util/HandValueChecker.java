package com.sap.cic.code.challenge.util;

import com.sap.cic.code.challenge.Card;
import com.sap.cic.code.challenge.GameException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Check specific value
 * Singleton pattern
 */
public class HandValueChecker {

    private static HandValueChecker handValueCheckerInstance = null;
    private static final Integer DECK_SIZE = 7;

    private HandValueChecker() {
    }

    public static HandValueChecker getInstance() {
        if (handValueCheckerInstance == null) {
            return new HandValueChecker();
        } else {
            return handValueCheckerInstance;
        }
    }

    // FIXME 这里其实用AOP的方式实现更好，但是这个项目没必要用到spring，太重不合适
    public ErrorCode checkLegality(List<Card> cardList) {
        if (cardList == null || cardList.size()!=DECK_SIZE) {
            return ErrorCode.CARD_LIST_ERROR;
        }
        Set<Card> set = new HashSet<>(cardList);
        if(set.size() < cardList.size()){
            /* There are duplicates card */
            return ErrorCode.CARD_VALUE_ERROR;
        }
        return ErrorCode.SUCC;
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
        Card.Suit flushSuit = null;
        Map<Card.Suit, Integer> suitIntegerMap = transferToSuitMap(cardList);
        for (Map.Entry<Card.Suit, Integer> entrySet: suitIntegerMap.entrySet()) {
            if (entrySet.getValue() >= 5) {
                flushSuit = entrySet.getKey();
            }
        }
        if (flushSuit == null) {
            return false;
        }
        Card.Suit finalFlushSuit = flushSuit;
        return checkStraight(cardList
                .stream()
                .filter(card -> card.getSuit().equals(finalFlushSuit)).collect(Collectors.toList()));
    }

    public boolean checkFourOfKind(List<Card> cardList) throws GameException {
        return getMaxKinds(cardList) == 4;
    }

    public boolean checkFullHouse(List<Card> cardList) throws GameException {
        Map<Card.Kind, Integer> kindIntegerMap = transferToKindMap(cardList);
        Card.Kind threeOfKind = null;
        for (Map.Entry<Card.Kind, Integer> entrySet: kindIntegerMap.entrySet()) {
            if (entrySet.getValue() == 3) {
                threeOfKind = entrySet.getKey();
                break;
            }
        }
        if (threeOfKind == null) {
            return false;
        }
        kindIntegerMap.remove(threeOfKind);
        for (Map.Entry<Card.Kind, Integer> entrySet: kindIntegerMap.entrySet()) {
            if (entrySet.getValue() >= 2) {
                return true;
            }
        }
        return false;
    }

    public boolean checkFlush(List<Card> cardList) {
        Map<Card.Suit, Integer> suitIntegerMap = transferToSuitMap(cardList);
        for (Map.Entry<Card.Suit, Integer> entrySet: suitIntegerMap.entrySet()) {
            if (entrySet.getValue() >= 5) {
                return true;
            }
        }
        return false;
    }

    public boolean checkStraight(List<Card> cardList) {
        List<Integer> cardKindValueList = new LinkedList<>();
        cardList.forEach(card -> cardKindValueList.add(card.getKind().rank));
        if (cardKindValueList.contains(Card.Kind.ACE.rank)
                && cardKindValueList.contains(Card.Kind.TWO.rank)
                && cardKindValueList.contains(Card.Kind.THREE.rank)
                && cardKindValueList.contains(Card.Kind.FOUR.rank)
                && cardKindValueList.contains(Card.Kind.FIVE.rank)) {
            // ACE作1时特殊考虑
            return true;
        }
        Collections.sort(cardKindValueList);
        // 根据牌型决定最多能有多少冗余剔除的牌位
        int toleration = 2 - (7 - cardKindValueList.size());
        for (int i=0; i<cardKindValueList.size()-1; i++) {
            // 选取符合规则的5张牌，可以剔除其中2张
            if (cardKindValueList.get(i+1) - cardKindValueList.get(i) != 1) {
                toleration--;
                if (toleration < 0) {
                    return false;
                }
                cardKindValueList.remove(cardKindValueList.get(i));
                i--;
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
                .count() >= 2;
    }

    public boolean checkPair(List<Card> cardList) {
        Map<Card.Kind, Integer> kindIntegerMap = transferToKindMap(cardList);
        for (Map.Entry<Card.Kind, Integer> entrySet: kindIntegerMap.entrySet()) {
            if (entrySet.getValue() == 2) {
                return true;
            }
        }
        return false;
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

    private Map<Card.Suit, Integer> transferToSuitMap(List<Card> cardList) {
        Map<Card.Suit, Integer> suitIntegerHashMap = new HashMap<>();
        cardList.forEach(card -> {
            int existNumber = suitIntegerHashMap.getOrDefault(card.getSuit(), 0);
            existNumber++;
            suitIntegerHashMap.put(card.getSuit(), existNumber);
        });
        return suitIntegerHashMap;
    }

}
