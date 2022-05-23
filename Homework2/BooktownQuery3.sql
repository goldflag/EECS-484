SELECT E.ISBN 
From Editions E, Books B, Authors A
WHERE E.Book_id = B.Book_id 
AND B.author_id = A.author_id 
AND A.First_name = 'Agatha' 
AND A.Last_name = 'Christie'
ORDER BY ISBN DESC;