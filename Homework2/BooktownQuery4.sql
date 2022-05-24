SELECT A.Last_name, A.First_name
FROM authors A, 
	(SELECT X.Author_id
	FROM Authors X, Books B, Subjects S
	WHERE X.Author_id = B. Author_id 
	AND B.Subject_id = S.Subject_id 
	AND S.Subject = 'Children/YA'
	INTERSECT 
	SELECT X.Author_id
	FROM Authors X, Books B Subjects S
	WHERE X.Author_id = B.Author_id AND B.Subject_id = S.Subject_id AND S.Subject = 'Fiction') Answer
WHERE Answer.Author_id = A.author_id;
ORDER BY First_Name ASC, Last_Name ASC;
