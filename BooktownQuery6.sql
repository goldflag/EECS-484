SELECT B.Title, E.Publication_Date, B.Author_ID, A.First_Name, A.Last_Name
FROM Books B, Editions E, Authors A
WHERE B.