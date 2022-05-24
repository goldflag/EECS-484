CREATE VIEW My_Authors AS
SELECT DISTINCT A.AUTHOR_ID
FROM AUTHORS A, BOOKS B, EDITIONS E
WHERE B.Author_ID = E.Author_ID
    and B.Book_ID = A.Book_ID
    and E.Publication_Date >= '2003-01-01'
    and E.Publication_Date <= '2008-12-31';
 
SELECT DISTINCT B.Title, E.Publication_Date, A.Author_ID, A.First_Name,
A.Last_Name
FROM AUTHORS A, BOOKS B, EDITIONS E, My_Authors EA
WHERE My_Authors.AUTHOR_ID = A.AUTHOR_ID
    and B.Book_ID = E.Book_ID
    and B.Author_ID = A.Author_ID
ORDER BY A.Author_ID ASC, B.Title ASC, E.Publication_Date DESC;

DROP VIEW My_Authors;
