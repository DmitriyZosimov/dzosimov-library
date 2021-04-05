package com.epam.brest.service;

public interface ILoginService {

    /**
     * Check if exist a card.
     * @param card id of the reader.
     * @return true if it`s exist, false if it`s not exist.
     */
    Boolean isExistCard(Integer card);
    /**
     * Check if removed a card.
     * @param card id of the reader.
     * @return true if it`s removed, false if it`s not removed.
     */
    Boolean isRemovedCard(Integer card);
}
