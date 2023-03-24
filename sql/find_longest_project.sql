SELECT p.name, TIMESTAMPDIFF(MONTH, p.start_date, p.finish_date) as Months
FROM project p, (SELECT MAX(TIMESTAMPDIFF(MONTH, start_date, finish_date)) as max_diff FROM project) pp
WHERE TIMESTAMPDIFF(MONTH, p.start_date, p.finish_date) = pp.max_diff
ORDER BY p.name