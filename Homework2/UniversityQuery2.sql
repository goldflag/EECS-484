SELECT DISTINCT S1.SID, S1.Name
FROM Students S1
JOIN Students S2 ON S1.SID <> S2.SID
JOIN Members M1 ON S1.SID = M1.SID
JOIN Members M2 ON S2.SID = M2.SID 
AND M1.PID = M2.PID
WHERE S2.SID IN
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
ORDER BY S1.Name DESC;