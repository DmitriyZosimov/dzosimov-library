package com.epam.brest.dao.jdbc.readerdaosupport;

import com.epam.brest.model.IReader;
import com.epam.brest.model.Reader;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FindAllReader extends MappingSqlQuery<IReader> {
    private static final String FIND_ALL_READER =
            "SELECT * FROM lib_reader";
    private static final String FIND_ALL_ACTIVE_READER =
            "SELECT * FROM lib_reader WHERE active=true";

    public FindAllReader(DataSource dataSource){
        super(dataSource, FIND_ALL_ACTIVE_READER);
    }

    public FindAllReader(DataSource dataSource, boolean withDeleted){
       super.setDataSource(dataSource);
       if(withDeleted){
           super.setSql(FIND_ALL_READER);
       } else {
           super.setSql(FIND_ALL_ACTIVE_READER);
       }
    }

    @Override
    protected IReader mapRow(ResultSet resultSet, int i) throws SQLException {
        IReader reader = new Reader();
        reader.setReaderId(resultSet.getInt("reader_id"));
        reader.setFirstName(resultSet.getString("first_name"));
        reader.setLastName(resultSet.getString("last_name"));
        reader.setPatronymic(resultSet.getString("patronymic"));
        reader.setDateOfRegistry(resultSet.getDate("date_of_registry").toLocalDate());
        reader.setActive(resultSet.getBoolean("active"));
        return reader;
    }
}
