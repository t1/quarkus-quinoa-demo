insert into book (id, title, author, isbn, publicationyear) values(1, 'The Hobbit', 'J.R.R. Tolkien', '978-0547928227', 1937);
insert into book (id, title, author, isbn, publicationyear) values(2, '1984', 'George Orwell', '978-0451524935', 1949);
insert into book (id, title, author, isbn, publicationyear) values(3, 'To Kill a Mockingbird', 'Harper Lee', '978-0061120084', 1960);
insert into book (id, title, author, isbn, publicationyear) values(4, 'The Great Gatsby', 'F. Scott Fitzgerald', '978-0743273565', 1925);
alter sequence book_seq restart with 5;
