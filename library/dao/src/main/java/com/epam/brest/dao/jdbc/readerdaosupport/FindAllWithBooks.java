package com.epam.brest.dao.jdbc.readerdaosupport;

import com.epam.brest.model.IReader;
import org.springframework.jdbc.object.MappingSqlQuery;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FindAllWithBooks extends MappingSqlQuery<IReader> {

    private static final String FIND_ALL_WITH_BOOKS =
            "SELECT * FROM lib_reader r " +
                    "LEFT JOIN lib_book b ON r.reader_id=b.reader_id ";

    @Override
    protected IReader mapRow(ResultSet resultSet, int i) throws SQLException {
        return null;
    }
}
