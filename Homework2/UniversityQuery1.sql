SELECT DISTINCT C.CID FROM COURSES C
MINUS
SELECT E.CID FROM ENROLLMENTS E
JOIN Students S ON E.SID = S.SID
WHERE S.Major != 'CS' OR S.Major IS NULL
ORDER BY CID DESC
HAVING COUNT(*) < 10;