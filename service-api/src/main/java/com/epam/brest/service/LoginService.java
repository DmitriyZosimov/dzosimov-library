package com.epam.brest.service;

public interface LoginService {

  /**
   * Check by identification if the reader is exist
   *
   * @param card identification of the reader.
   * @return true if it`s exist, false if it`s not exist.
   */
  Boolean isExistCard(Integer card);

  /**
   * Check by identification if the reader is exist
   *
   * @param card identification of the reader.
   * @return true if it`s removed, false if it`s not removed.
   */
  Boolean isRemovedCard(Integer card);
}
