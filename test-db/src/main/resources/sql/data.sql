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

INSERT INTO lib_entity (book_id)
VALUES (1), (1), (1), (1), (1), 
(2), (2), (2), (2), (2), (2), (2), 
(3), (3), 
(4), (4), (4), 
(5), (5), 
(6), (6),
(7), (7), (7), (7), (7),
(8), (8), (8), (8), 
(9), (9), (9), (9), (9), 
(10), (10), (10);

INSERT INTO lib_entities_readers (entity_id, reader_id)
VALUES (1, 1), (6, 1), (20, 1),
(2, 2), (37, 2), (23, 2),
(7, 3), (11, 3), (19, 3),
(3, 4), (9, 4),
(21, 5), (29, 5),
(13, 6), (32, 6), (4, 6),
(6, 7), (30, 7), 
(10, 8), (27, 8);
