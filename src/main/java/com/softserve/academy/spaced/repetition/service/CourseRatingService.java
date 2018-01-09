package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.CourseRating;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.UserStatusException;

/**
 * This interface works with courses rating.
 */
public interface CourseRatingService {
    /**
     * Adds rating to course with the given identifier.
     *
     * @param rating - value of rating with additional information.
     * @param courseId     - course's id to set rating.
     * @throws NotAuthorisedUserException
     */
    void addCourseRating(int rating, Long courseId) throws NotAuthorisedUserException, UserStatusException;

    /**
     * Gets rating of course with the given identifier.
     *
     * @param id - course's id, cardId must not be {@literal null}.
     * @return courseRating with the given identifier
     */
    CourseRating getCourseRatingById(Long id);

}
