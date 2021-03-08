package com.epam.brest.dao.jdbc.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import javax.sql.DataSource;
import java.sql.Types;

public class UpdateReader extends SqlUpdate {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateReader.class);

    private static final String UPDATE_READER =
            "UPDATE lib_reader SET first_name=:firstName, " +
                    "last_name=:lastName, " +
                    "patronymic=:patronymic, " +
                    "date_of_registry=:dateOfRegistry, " +
                    "active=:active " +
                    "WHERE reader_id=:readerId";

    public UpdateReader(DataSource ds) {
        super(ds, UPDATE_READER);
        super.declareParameter(new SqlParameter("firstName", Types.VARCHAR));
        super.declareParameter(new SqlParameter("lastName", Types.VARCHAR));
        super.declareParameter(new SqlParameter("patronymic", Types.VARCHAR));
        super.declareParameter(new SqlParameter("dateOfRegistry", Types.DATE));
        super.declareParameter(new SqlParameter("active", Types.BOOLEAN));
        super.declareParameter(new SqlParameter("readerId", Types.INTEGER));
        LOGGER.info("constructor UpdateReader(dataSource) was started");
        LOGGER.debug("dataSource={}", ds);
    }
}
