SELECT DISTINCT S.SID
FROM Students S JOIN Enrollments E1 ON S.SID = E1.SID
                        JOIN Courses C1 ON E1.CID = C1.CID
                        JOIN Enrollments E2 ON S.SID = E2.SID
                        JOIN Courses C2 ON E2.CID = C2.CID
                        JOIN Enrollments E3 ON S.SID = E3.SID
                        JOIN Courses C3 ON E3.CID = C3.CID
WHERE (C1.C_Name = 'EECS442'
  AND C2.C_Name = 'EECS445'
  AND C3.C_Name = 'EECS492')
   OR (C1.C_Name = 'EECS482'
  AND C2.C_Name = 'EECS486')
   OR C1.C_Name = 'EECS281'
ORDER BY S.SID ASC;