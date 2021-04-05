package com.epam.brest.service;

public interface ILoginService {

    Boolean isExistCard(Integer card);
    Boolean isRemovedCard(Integer card);
}
