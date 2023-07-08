BEGIN TRAN
INSERT INTO Articole (Titlu, NrAutori, NrPagini, AnPublicare, Tid) VALUES ('balene', '5', '11', '1990', 1)
WAITFOR DELAY '00:00:15'
ROLLBACK TRAN