package com.epam.brest.service.rest;

import com.epam.brest.service.ILoginService;

public class LoginServiceRest implements ILoginService {
    @Override
    public boolean isExistCard(Integer card) {
        return false;
    }

    @Override
    public boolean isRemovedCard(Integer card) {
        return false;
    }
}
