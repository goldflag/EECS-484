SELECT DISTINCT A.Author_ID, A.First_Name, A.Last_Name
FROM Authors A
WHERE NOT EXISTS (SELECT DISTINCT S.SUBJECT FROM SUBJECTS S, AUTHORS JK, BOOKS B
                    WHERE JK.AUTHOR_ID = B.AUTHOR_ID
                    AND B.SUBJECT_ID = S.SUBJECT_ID
                    AND JK.FIRST_NAME = 'J. K.'
                    AND JK.LAST_NAME = 'Rowling'
                MINUS
                SELECT DISTINCT S2.Subject FROM Subjects S2, Books B2
                        WHERE S2.Subject_ID = B2.Subject_ID
                        and B2.Author_ID = A.Author_ID)
ORDER BY A.Last_Name ASC, A.Author_ID;