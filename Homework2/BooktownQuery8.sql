CREATE VIEW AV(Author_ID) AS
SELECT DISTINCT B.Author_ID
FROM Authors A
JOIN Books B ON B.Author_ID = A.Author_ID
GROUP BY B.Author_ID
HAVING COUNT(*) = 3;

SELECT DISTINCT P.Publisher_ID, P.Name
FROM Publishers P
JOIN Editions E ON E.Publisher_ID = P.Publisher_ID
JOIN Books B ON E.Book_ID = B.Book_ID
JOIN AV G ON G.Author_ID = B.Author_ID
ORDER BY P.Publisher_ID DESC;

DROP VIEW AV;
