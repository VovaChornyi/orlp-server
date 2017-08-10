package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.controller.FolderController;
import com.softserve.academy.spaced.repetition.domain.Deck;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class DeckLinkByFolderDTO extends DTO<Deck> {

    public DeckLinkByFolderDTO(Deck deck, Link link) {
        super(deck, link);

        Link linkCards = linkTo(methodOn(FolderController.class).getCardsByFolderAndDeck((long) -1, getEntity().getId())).withRel("cards");
        add(getLinkWithReplacedParentPart(linkCards).withRel("cards"));
    }

    public String getName() {
        return getEntity().getName();
    }

    public String getDescription() {
        return getEntity().getDescription();
    }

    public Double getRating() {
        return getEntity().getRating();
    }
}