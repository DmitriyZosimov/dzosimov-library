package com.epam.brest.dao.jdbc.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import javax.sql.DataSource;
import java.sql.Types;

public class SaveReader extends SqlUpdate {
    private static final Logger LOGGER = LoggerFactory.getLogger(SaveReader.class);

    public SaveReader(DataSource ds, String sql) {
        super(ds, sql);
        super.declareParameter(new SqlParameter("firstName", Types.VARCHAR));
        super.declareParameter(new SqlParameter("lastName", Types.VARCHAR));
        super.declareParameter(new SqlParameter("patronymic", Types.VARCHAR));
        super.declareParameter(new SqlParameter("dateOfRegistry", Types.DATE));
        super.setGeneratedKeysColumnNames("reader_id");
        super.setReturnGeneratedKeys(true);
        LOGGER.info("constructor SaveReader(dataSource) was started");
        LOGGER.debug("dataSource={}", ds);

    }
}
