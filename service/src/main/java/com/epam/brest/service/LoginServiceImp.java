package com.epam.brest.service;

import com.epam.brest.dao.ReaderDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoginServiceImp implements LoginService{

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImp.class);

    private ReaderDao readerDao;

    @Autowired
    public LoginServiceImp(ReaderDao readerDao) {
        this.readerDao = readerDao;
    }

    @Transactional(propagation = Propagation.NEVER)
    @Override
    public Boolean isExistCard(Integer card) {
        if(card == null){
            return false;
        }
        return readerDao.exist(card);
    }

    @Transactional(propagation = Propagation.NEVER)
    @Override
    public Boolean isRemovedCard(Integer card) {
        if(card == null){
            return false;
        }
        return readerDao.exist(card, false);
    }
}
