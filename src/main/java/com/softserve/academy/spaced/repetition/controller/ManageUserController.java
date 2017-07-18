package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.impl.UserPublicDTO;
import com.softserve.academy.spaced.repetition.domain.AccountStatus;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@CrossOrigin
public class ManageUserController {

    @Autowired
    private UserService userService;

    @GetMapping("/api/admin/users")
    public ResponseEntity<List<UserPublicDTO>> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        Link collectionLink = linkTo(methodOn(ManageUserController.class).getAllUsers()).withSelfRel();
        List<UserPublicDTO> usersDTOList = DTOBuilder.buildDtoListForCollection(userList,
                UserPublicDTO.class, collectionLink);
        return new ResponseEntity<>(usersDTOList, HttpStatus.OK);
    }

    @GetMapping("/api/admin/users/{id}")
    public ResponseEntity<UserPublicDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        Link collectionLink = linkTo(methodOn(ManageUserController.class).getUserById(id)).withSelfRel();
        UserPublicDTO userDTO = DTOBuilder.buildDtoForEntity(user, UserPublicDTO.class, collectionLink);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PutMapping("/api/admin/users/{id}")
    public ResponseEntity<UserPublicDTO> toggleUsersBlockStatus(@PathVariable Long id) {
        User userWithChangedStatus = userService.toggleUsersStatus(id, AccountStatus.BLOCKED);
        Link collectionLink = linkTo(methodOn(ManageUserController.class).toggleUsersBlockStatus(id)).withSelfRel();
        UserPublicDTO userPublicDTO = DTOBuilder.buildDtoForEntity(userWithChangedStatus, UserPublicDTO.class, collectionLink);

        return new ResponseEntity<>(userPublicDTO, HttpStatus.OK);
    }

    @DeleteMapping("/api/admin/users/{id}")
    public ResponseEntity<UserPublicDTO> toggleUsersDeleteStatus(@PathVariable Long id) {
        User userWithChangedStatus = userService.toggleUsersStatus(id, AccountStatus.DELETED);
        Link collectionLink = linkTo(methodOn(ManageUserController.class).toggleUsersDeleteStatus(id)).withSelfRel();
        UserPublicDTO userPublicDTO = DTOBuilder.buildDtoForEntity(userWithChangedStatus, UserPublicDTO.class, collectionLink);

        return new ResponseEntity<>(userPublicDTO, HttpStatus.OK);
    }

    @PostMapping("/api/admin/users/{userId}/deck/{deckId}")
    public ResponseEntity<UserPublicDTO> addExistingDeckToUsersFolder(@PathVariable("userId") Long userId, @PathVariable("deckId") Long deckId) {

        User user = userService.addExistingDeckToUsersFolder(userId, deckId);

        if (user != null) {
            Link collectionLink = linkTo(methodOn(ManageUserController.class).addExistingDeckToUsersFolder(userId, deckId)).withSelfRel();
            UserPublicDTO userPublicDTO = DTOBuilder.buildDtoForEntity(user, UserPublicDTO.class, collectionLink);
            return new ResponseEntity<UserPublicDTO>(userPublicDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}