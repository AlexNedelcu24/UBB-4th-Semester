SELECT AnPublicare, Titlu FROM Articole ORDER BY AnPublicare

CREATE INDEX index_articole_an_asc_titlu_asc ON Articole (AnPublicare ASC, Titlu ASC);