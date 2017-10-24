package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.LearningRegime;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.exceptions.CardContainsEmptyFieldsException;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.CardRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CardService {

    private final static int CARDS_NUMBER = 10;

    private final CardRepository cardRepository;

    private final DeckRepository deckRepository;

    private final UserService userService;

    @Autowired
    public CardService(CardRepository cardRepository, DeckRepository deckRepository, UserService userService) {
        this.cardRepository = cardRepository;
        this.deckRepository = deckRepository;
        this.userService = userService;
    }

    public List<Card> getLearningCards(Long deckId) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        String email = user.getAccount().getEmail();
        List<Card> learningCards = new ArrayList<>();
        if (user.getAccount().getLearningRegime().equals(LearningRegime.BAD_NORMAL_GOOD_STATUS_DEPENDING)) {
            learningCards = cardRepository.cardsForLearningWithOutStatus(email, deckId, CARDS_NUMBER);
            if (learningCards.size() < CARDS_NUMBER) {
                learningCards.addAll(cardRepository.cardsQueueForLearningWithStatus(email, deckId,
                        CARDS_NUMBER - learningCards.size()));
            }
        } else if (user.getAccount().getLearningRegime().equals(LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION)) {
            learningCards = cardRepository.getCardsThatNeedRepeating(deckId, new Date(), email, CARDS_NUMBER);
            if (learningCards.size() < CARDS_NUMBER) {
                learningCards.addAll(cardRepository.getNewCards(deckId, email, CARDS_NUMBER - learningCards.size()));
            }
        }
        return learningCards;
    }

    public Card getCard(Long id) {
        return cardRepository.findOne(id);
    }

    public void addCard(Card card, Long deckId) throws CardContainsEmptyFieldsException {
        if (card.getTitle().trim().isEmpty() || card.getAnswer().trim().isEmpty() || card.getQuestion().trim().isEmpty()) {
            throw new CardContainsEmptyFieldsException();
        }
        Deck deck = deckRepository.findOne(deckId);
        card.setDeck(deck);
        deck.getCards().add(cardRepository.save(card));
    }

    public void updateCard(Long id, Card card) throws CardContainsEmptyFieldsException {
        if (card.getTitle().trim().isEmpty() || card.getAnswer().trim().isEmpty() || card.getQuestion().trim().isEmpty()) {
            throw new CardContainsEmptyFieldsException();
        }
        card.setId(id);
        card.setDeck(cardRepository.findOne(id).getDeck());
        cardRepository.save(card);
    }

    public List<Card> getCardsQueue(long deckId) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        String email = user.getAccount().getEmail();
        List<Card> cardsQueue = cardRepository.cardsForLearningWithOutStatus(email, deckId, CARDS_NUMBER);

        if (cardsQueue.size() < CARDS_NUMBER) {
            cardsQueue.addAll(cardRepository.cardsQueueForLearningWithStatus(email, deckId, CARDS_NUMBER).subList(0, CARDS_NUMBER - cardsQueue.size()));
        }
        return cardsQueue;
    }


    @Transactional
    public void deleteCard(Long cardId) {
        cardRepository.deleteCardById(cardId);
    }
}
