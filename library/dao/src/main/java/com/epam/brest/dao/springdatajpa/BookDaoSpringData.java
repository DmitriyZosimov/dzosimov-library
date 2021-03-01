package com.epam.brest.dao.springdatajpa;

import com.epam.brest.dao.BookDao;
import com.epam.brest.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookDaoSpringData extends JpaRepository<Book, Integer> {
}
