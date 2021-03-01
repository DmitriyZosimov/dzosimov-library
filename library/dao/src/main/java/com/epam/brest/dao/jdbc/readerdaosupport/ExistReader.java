package com.epam.brest.dao.jdbc.readerdaosupport;

import com.epam.brest.model.Reader;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlFunction;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class ExistReader extends SqlFunction{

    private static final String EXIST_READER =
            "SELECT EXISTS(SELECT 1 FROM lib_reader WHERE reader_id=:readerId)";

    public ExistReader(DataSource ds) {
        super(ds, EXIST_READER);
        super.declareParameter(new SqlParameter("readerId", Types.INTEGER));
    }

}
