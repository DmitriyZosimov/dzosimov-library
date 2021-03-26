package com.epam.brest.service;

public interface ILoginService {

    boolean isExistCard(Integer card);
    boolean isRemovedCard(Integer card);
}
