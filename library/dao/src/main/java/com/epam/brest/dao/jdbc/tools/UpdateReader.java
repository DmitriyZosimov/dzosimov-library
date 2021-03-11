package com.epam.brest.dao.jdbc.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import javax.sql.DataSource;
import java.sql.Types;

public class UpdateReader extends SqlUpdate {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateReader.class);

    public UpdateReader(DataSource ds, String sql) {
        super(ds, sql);
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
