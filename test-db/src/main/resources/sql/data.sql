INSERT INTO lib_book (authors, title, genre)
VALUES ('Candice Fox', 'Gathering Dark', 2), 
('Alex Michaelides', 'The Silent Patient', 1),
('Nora Ephron', 'I Feel Bad About My Neck', 3), 
('Alain Mabanckou', 'Broken Glass', 13), 
('Kate Atkinson', 'Life After Life', 10),
('Mark Haddon', 'The Curious Incident of the Dog in the Night‑Time', 2),
('Naomi Klein', 'The Shock Doctrine', 9),
('Cormac McCarthy', 'The Road', 4),
('Jonathan Franzen', 'The Corrections', 7),
('Sarah Waters', 'Fingersmith', 6);

INSERT INTO lib_reader(first_name, last_name, patronymic, date_of_registry)
VALUES('Denzel', 'Washington', 'Hayes', '2020-02-01'), 
('Daniel', 'Day-Lewis', 'Michael Blake', '2021-01-01'),
('Keanu', 'Charles', 'Reeves', '2020-12-04'), 
('Michael', 'Jordan', 'Bakari', '2020-12-14'), 
('Kim', 'Min-hee', '', '2020-08-15'), 
('Bradley', 'Pitt', 'William', '2021-02-15'), 
('Darren', 'Shahlavi', 'Majian', '2020-09-16'), 
('Nicole', 'Kidman', 'Mary', '2020-11-15');

INSERT INTO lib_book_copies (book_id, reader_id)
VALUES (1, 1), (1, 2), (1, 4), (1, 6), (1, null),
(2, 1), (2, 3), (2, 7), (2, 4), (2, 8), (2, 3), (2, null),
(3, 6), (3, null),
(4, null), (4, null), (4, null),
(5, null), (5, 3),
(6, 1), (6, 5),
(7, null), (7, 2), (7, null), (7, null), (7, null),
(8, 8), (8, null), (8, 5), (8, 7),
(9, null), (9, 6), (9, null), (9, null), (9, null),
(10, null), (10, 2), (10, null);
