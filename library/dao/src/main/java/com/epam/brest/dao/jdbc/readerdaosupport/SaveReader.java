package com.epam.brest.dao.jdbc.readerdaosupport;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import javax.sql.DataSource;
import java.sql.Types;

public class SaveReader extends SqlUpdate {
    private static final String INSERT_READER =
            "INSERT INTO lib_reader(first_name, last_name, patronymic, date_of_registry)" +
                    "VALUES(:firstName, :lastName, :patronymic, :dateOfRegistry)";

    public SaveReader(DataSource ds) {
        super(ds, INSERT_READER);
        super.declareParameter(new SqlParameter("firstName", Types.VARCHAR));
        super.declareParameter(new SqlParameter("lastName", Types.VARCHAR));
        super.declareParameter(new SqlParameter("patronymic", Types.VARCHAR));
        super.declareParameter(new SqlParameter("dateOfRegistry", Types.DATE));
        super.setGeneratedKeysColumnNames("reader_id");
        super.setReturnGeneratedKeys(true);
    }
}
