package com.epam.brest.dao;

import com.epam.brest.model.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReaderDao extends JpaRepository<Reader, Integer> {

    @Query("SELECT r FROM Reader r WHERE r.active = true")
    List<Reader> findAllActiveReaders();

    //TODO: edit
    @Modifying
    @Query("UPDATE Reader r SET r.active = false WHERE r.readerId = :readerId")
    int removeReader(@Param("readerId") Integer id);

    @Modifying
    @Query("UPDATE Reader SET active = true WHERE readerId = :reader.readerId")
    int restoreReader(@Param("reader") Reader reader);

}
