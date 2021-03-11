package com.epam.brest.dao.jdbc.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlFunction;

import javax.sql.DataSource;
import java.sql.Types;

public class ExistReader extends SqlFunction<Boolean> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExistReader.class);

    public ExistReader(DataSource ds, String sql) {
        super(ds, sql);
        super.declareParameter(new SqlParameter("readerId", Types.INTEGER));
        LOGGER.info("constructor ExistReader(dataSource) was started");
    }

}
