DROP TABLE IF EXISTS lib_reader;
DROP TABLE IF EXISTS lib_book;

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
    reader_id integer,
    PRIMARY KEY (book_id),
    FOREIGN KEY (reader_id) REFERENCES lib_reader(reader_id)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
);

