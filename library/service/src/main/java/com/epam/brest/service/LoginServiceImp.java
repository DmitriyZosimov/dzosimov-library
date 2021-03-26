package com.epam.brest.service;

import com.epam.brest.dao.ReaderDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class LoginServiceImp implements ILoginService{

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImp.class);

    private ReaderDao readerDao;

    @Autowired
    public LoginServiceImp(ReaderDao readerDao) {
        this.readerDao = readerDao;
    }

    /**
     * Check this card (reader) in DB to exist
     * @param card library card
     * @return true is exist, false if not exist
     */
    @Override
    public boolean isExistCard(Integer card) {
        return readerDao.exist(card);
    }

    /**
     * Check this card (reader) in DB to exist as a removed card
     * @param card library card
     * @return true is exist, false if not exist
     */
    @Override
    public boolean isRemovedCard(Integer card) {
        return readerDao.exist(card, false);
    }
}
