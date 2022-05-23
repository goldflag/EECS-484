SELECT DISTINCT A.Author_id 
FROM Authors A, Books B
WHERE A.Author_id = B.Author_id 
GROUP BY A.Author_id
HAVING count(*)=1;
ORDER BY Author_ID ASC;