DROP TABLE IF EXISTS lib_entities_readers;
DROP TABLE IF EXISTS lib_entity;
DROP TABLE IF EXISTS lib_book;
DROP TABLE IF EXISTS lib_reader;

CREATE TABLE lib_reader (
    reader_id integer NOT NULL IDENTITY(1, 1),
    first_name varchar(20) NOT NULL,
    last_name varchar(30) NOT NULL,
    patronymic varchar(25),
    date_of_registry date NOT NULL,
    active boolean DEFAULT TRUE,
    PRIMARY KEY (reader_id)
);

CREATE TABLE lib_book (
    book_id integer NOT NULL IDENTITY(1, 1),
    authors varchar(60) NOT NULL,
    title varchar(60) NOT NULL,
    genre integer NOT NULL,
    PRIMARY KEY (book_id)
);

CREATE TABLE lib_entity (
    entity_id integer NOT NULL IDENTITY(1, 1),
    book_id integer NOT NULL,
    PRIMARY KEY(entity_id),
    FOREIGN KEY(book_id) REFERENCES lib_book(book_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE lib_entities_readers (
    entity_id integer,
    reader_id integer,
    FOREIGN KEY (reader_id) REFERENCES lib_reader(reader_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
    FOREIGN KEY (entity_id) REFERENCES lib_entity(entity_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
);


