package com.epam.brest.dao.jdbc.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlFunction;

import javax.sql.DataSource;
import java.sql.Types;

public class ExistReader extends SqlFunction{

    private static final Logger LOGGER = LoggerFactory.getLogger(ExistReader.class);

    private static final String EXIST_READER =
            "SELECT EXISTS(SELECT 1 FROM lib_reader WHERE reader_id=:readerId)";

    public ExistReader(DataSource ds) {
        super(ds, EXIST_READER);
        super.declareParameter(new SqlParameter("readerId", Types.INTEGER));
        LOGGER.info("constructor ExistReader(dataSource) was started");
    }

}
