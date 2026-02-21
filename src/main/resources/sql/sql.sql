INSERT INTO EMPLOYEES (BIRTH_DATE, EMAIL, NAME, PASSWORD, PHONE)
VALUES
       ('1990-05-15', 'admin@test.com', 'admin', '$2a$10$obBti51aNImlsyRNnBEVZOd44Z.aWjxAWL6FL32Vv7D7YqYxbhIKa', '555666777'), --admin123, ADMIN can manage employees
       ('1990-05-15', 'john.doe@email.com', 'John Doe', '$2a$10$rq0vStzIaby17grOUzJhw.QGAOJjlCNOpRyshtwkUXNQxZVRo2hQy	', '5551234567'), --pass123
       ('1985-09-20', 'jane.smith@email.com', 'Jane Smith', '$2a$10$eJn3LkD1Ax.QpQdULcydju1ZYP5szt4/tAzCBx0R0g.nEColqb9v6', '5559876543'), --abc456
       ('1978-03-08', 'bob.jones@email.com', 'Bob Jones', '$2a$10$kuws0sp7ASCGqh7ZzABnQuPfyLprItx.L9OFigGt5R5Ju8yG3AfsK', '5553216789'), --qwerty789
       ('1982-11-25', 'alice.white@email.com', 'Alice White', '$2a$10$EENtcpMXK7Nv2w6o20N22ulBuMBsUQV.umrfc6RB855/DLr1QJx7y', '5558765432'), --secret567
       ('1995-07-12', 'mike.wilson@email.com', 'Mike Wilson', '$2a$10$dY/sC8gJ2HWr.zZUAJe9f.BJvelSuHfttOQ06BV1IZvQNHczq.o5y', '5552345678'), --mypassword
       ('1989-01-30', 'sara.brown@email.com', 'Sara Brown', '$2a$10$I9yE3YazHqkMwp5v7GMYuOaiRFkXs7ldKMA55bba6jhbKyZsloVjW', '5558765433'), --letmein123
       ('1975-06-18', 'tom.jenkins@email.com', 'Tom Jenkins', '$2a$10$UDNE/kTWZbL/DvTuaZbxlOBdW5uhBzRYHT5phvGD0e7LxTrnnXhSi', '5553456789'), --pass4321
       ('1987-12-04', 'lisa.taylor@email.com', 'Lisa Taylor', '$2a$10$TrSbmJJr6M/QY9s3kbwBjOGFEsoMU.I7oS/ke9cNaqbsi3Hlu2OaS', '5557890123'), --securepwd
       ('1992-08-22', 'david.wright@email.com', 'David Wright', '$2a$10$3ay8y5Ni1H5OR0eMcCAp1e2OurjnUljUeZ/yhn.YLPuUH94ES2vuW', '5554567890'), --access123
       ('1980-04-10', 'emily.harris@email.com', 'Emily Harris', '$2a$10$ugwAjzwKgJHJlHWdZdqYPeHzbLwNIpXRuXCrnSiufKCNxqB/dTdIW', '5550987654'); --1234abcd

INSERT INTO CLIENTS (BALANCE, EMAIL, NAME, PASSWORD)
VALUES (1000.00, 'client1@example.com', 'Medelyn Wright', '$2a$10$mPoPNt6OeQMP53Hx9baVOu6wg.pHzucO01FIrVm34FRrLCELl24eO'), --password123
       (1500.50, 'client2@example.com', 'Landon Phillips', '$2a$10$o84Q70h5nuJ0MUBwV7dsR.2S4cAoifz9H0qMBb9jCNoZ/6Yqf0YAu'), --securepass
       (800.75, 'client3@example.com', 'Harmony Mason', '$2a$10$bVcfrcTuaZWHGupfv67IY.GJsDrs9R.BZVpEd5hzBx3ThCEQ9POea'), --abc123
       (1200.25, 'client4@example.com', 'Archer Harper', '$2a$10$194gwuqOwm.sJcMo8p0/leqZa.CKB4HhjBU0KYKRiZVHUn4FTMvPm'), --pass456
       (900.80, 'client5@example.com', 'Kira Jacobs', '$2a$10$EfKq3It9M49N8uwGXvYHFOzIbLc279tz2cePbduXaquIocf2nRgUa'), --letmein789
       (1100.60, 'client6@example.com', 'Maximus Kelly', '$2a$10$GKBr5htPHt1fkfTMFUTmTeZq5wfkcYrHBaMQVnkn6bVDn8CCiLVT2'), --adminpass
       (1300.45, 'client7@example.com', 'Sierra Mitchell', '$2a$10$GDYhllVH3U.q4/fhOcAGfehJ221Zz7F8z/EnG.4hj8gYEFMEbTaHK'), --mypassword
       (950.30, 'client8@example.com', 'Quinton Saunders', '$2a$10$PUp9k5qMPOe4zyK4/81goukJ3wWJA9PS.Mjar2zwqE1cX7KKeJz8i'), --test123
       (1050.90, 'client9@example.com', 'Amina Clarke', '$2a$10$qWM83SG1rSGTyOJ7Aq4fCeBjESueqUak25qt24u8wIPTD7YRU6iH.'), --qwerty123
       (880.20, 'client10@example.com', 'Bryson Chavez', '$2a$10$Wl2qpbPSzhy7dkExQA1T3uMxHpPh/kPLhwnXVIXHHQRgX/2QEEDOK'); --pass789

INSERT INTO BOOKS (name, genre, age_group, price, publication_year, author, number_of_pages, characteristics,description, language)
VALUES ('The Hidden Treasure', 'Adventure', 'ADULT', 24.99, '2018-05-15', 'Emily White', 400, 'Mysterious journey','An enthralling adventure of discovery', 'ENGLISH'),
       ('Echoes of Eternity', 'Fantasy', 'TEEN', 16.50, '2011-01-15', 'Daniel Black', 350, 'Magical realms', 'A spellbinding tale of magic and destiny', 'ENGLISH'),
       ('Whispers in the Shadows', 'Mystery', 'ADULT', 29.95, '2018-08-11', 'Sophia Green', 450, 'Intriguing suspense','A gripping mystery that keeps you guessing', 'ENGLISH'),
       ('The Starlight Sonata', 'Romance', 'ADULT', 21.75, '2011-05-15', 'Michael Rose', 320, 'Heartwarming love story','A beautiful journey of love and passion', 'ENGLISH'),
       ('Beyond the Horizon', 'Science Fiction', 'CHILD', 18.99, '2004-05-15', 'Alex Carter', 280,'Interstellar adventure', 'An epic sci-fi adventure beyond the stars', 'ENGLISH'),
       ('Dancing with Shadows', 'Thriller', 'ADULT', 26.50, '2015-05-15', 'Olivia Smith', 380, 'Suspenseful twists','A thrilling tale of danger and intrigue', 'ENGLISH'),
       ('Voices in the Wind', 'Historical Fiction', 'ADULT', 32.00, '2017-05-15', 'William Turner', 500,'Rich historical setting', 'A compelling journey through time', 'ENGLISH'),
       ('Serenade of Souls', 'Fantasy', 'TEEN', 15.99, '2013-05-15', 'Isabella Reed', 330, 'Enchanting realms','A magical fantasy filled with wonder', 'ENGLISH'),
       ('Silent Whispers', 'Mystery', 'ADULT', 27.50, '2021-05-15', 'Benjamin Hall', 420, 'Intricate detective work','A mystery that keeps you on the edge', 'ENGLISH'),
       ('Whirlwind Romance', 'Romance', 'OTHER', 23.25, '2022-05-15', 'Emma Turner', 360, 'Passionate love affair','A romance that sweeps you off your feet', 'ENGLISH');
