SELECT DISTINCT S.SID, S.Name
FROM Students S
JOIN Students X ON S.SID <> X.SID
JOIN Members M ON S.SID = M.SID
JOIN Members Y ON X.SID = Y.SID 
AND M.PID = Y.PID
WHERE X.SID IN
(SELECT DISTINCT S.SID
FROM Students S, Enrollments E1, Enrollments E2, Enrollments E3, Courses C1,
Courses C2, Courses C3
WHERE S.SID = E1.SID
        AND S.SID = E2.SID
        AND S.SID = E3.SID
      AND E1.CID = C1.CID
      AND E2.CID = C2.CID
      AND E3.CID = C3.CID
        AND (C1.C_Name = 'EECS482'
      OR C1.C_Name = 'EECS483')
      AND (C2.C_Name = 'EECS484'
      OR C2.C_Name = 'EECS485')
      AND C3.C_Name = 'EECS280'
)
ORDER BY S.Name DESC;
