-- Creating the TransactionHistory table for logging
use ChampionsLeague
go
CREATE TABLE TransactionHistory(
    type VARCHAR(150),
    time TIME,
    message VARCHAR(150)
)

select * from TransactionHistory

-- Dirty reads demonstration
BEGIN

	BEGIN
    -- Inserting and rolling back a new team
    BEGIN TRANSACTION
        INSERT INTO Echipa(Nume, Tara, Coeficient, StadionID) VALUES ('New Team', 'Romania', 5, 1);
        WAITFOR DELAY '00:00:10'
        ROLLBACK TRANSACTION
        INSERT INTO TransactionHistory(type, time, message) VALUES('DR Rollback', CONVERT(TIME, GETDATE()), 'Rollback successfully')
    END

    -- Reading uncommitted data
    BEGIN
    SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
    BEGIN TRANSACTION
        SELECT * FROM Echipa;
        INSERT INTO TransactionHistory(type, time, message) VALUES('Echipa select', CONVERT(TIME, GETDATE()), 'Selected all from Echipa after insert')
        SELECT * FROM Echipa;
        WAITFOR DELAY '00:00:15'
        INSERT INTO TransactionHistory(type, time, message) VALUES('Echipa select', CONVERT(TIME, GETDATE()), 'Selected all from Echipa after rollback')
    COMMIT TRANSACTION
    END

    -- Reading committed data
    BEGIN
    SET TRANSACTION ISOLATION LEVEL READ COMMITTED
    BEGIN TRANSACTION
        SELECT * FROM Echipa;
        INSERT INTO TransactionHistory(type, time, message) VALUES('Echipa select', CONVERT(TIME, GETDATE()), 'Selected all from Echipa after insert')
        WAITFOR DELAY '00:00:15'
        SELECT * FROM Echipa;
        INSERT INTO TransactionHistory(type, time, message) VALUES('Echipa select', CONVERT(TIME, GETDATE()), 'Selected all from Echipa after rollback')
    COMMIT TRANSACTION
    END
END


--NON-REPEATABLE READS
BEGIN
    --WRONG
    BEGIN
    BEGIN TRANSACTION
        INSERT INTO Fotbalist(Nume, Varsta, Pozitie) VALUES ('FotbalistX', 20, 'Mijlocas');
        INSERT INTO TransactionHistory(type, time, message) VALUES('X Insert', CONVERT(TIME, GETDATE()), 'Delay for X insert started')
        WAITFOR DELAY '00:00:05';
        UPDATE Fotbalist SET Nume = Nume + ' TRANSACTION' WHERE Nume = 'FotbalistX';
        INSERT INTO TransactionHistory(type, time, message) VALUES('X Insert', CONVERT(TIME, GETDATE()), 'Inserted')
        COMMIT TRANSACTION
        INSERT INTO TransactionHistory(type, time, message) VALUES('X Insert', CONVERT(TIME, GETDATE()), 'Insert rollback ended')
    END

    BEGIN
    SET TRANSACTION ISOLATION LEVEL READ COMMITTED
    BEGIN TRANSACTION
        SELECT * FROM Fotbalist;
        WAITFOR DELAY '00:00:10'
        SELECT * FROM Fotbalist;
    COMMIT TRANSACTION
    END

    --SOLUTION
    BEGIN
    SET TRANSACTION ISOLATION LEVEL REPEATABLE READ
    BEGIN TRANSACTION
        SELECT * FROM Fotbalist;
        WAITFOR DELAY '00:00:10'
        SELECT * FROM Fotbalist;
    COMMIT TRANSACTION
    END
END


--PHANTOM READS
BEGIN
    --WRONG
     BEGIN
    BEGIN TRANSACTION
        INSERT INTO TransactionHistory(type, time, message) VALUES('PR Insert', CONVERT(TIME, GETDATE()), 'Delay for NRR insert started')
        WAITFOR DELAY '00:00:05'
        INSERT INTO Fotbalist(Nume, Varsta, Pozitie) VALUES ('FotbalistPR', 30, 'Atacant');
        INSERT INTO TransactionHistory(type, time, message) VALUES('PR Insert', CONVERT(TIME, GETDATE()), 'Inserted')
        COMMIT TRANSACTION
        INSERT INTO TransactionHistory(type, time, message) VALUES('PR Insert', CONVERT(TIME, GETDATE()), 'Insert rollback ended')
    END

    BEGIN
    SET TRANSACTION ISOLATION LEVEL REPEATABLE READ
    BEGIN TRANSACTION
        SELECT * FROM Fotbalist
        WAITFOR DELAY '00:00:10'
        SELECT * FROM Fotbalist
    COMMIT TRANSACTION
    END

    --SOLUTION
    BEGIN
    SET TRANSACTION ISOLATION LEVEL SERIALIZABLE
    BEGIN TRANSACTION
        SELECT * FROM Fotbalist
        WAITFOR DELAY '00:00:10'
        SELECT * FROM Fotbalist
    COMMIT TRANSACTION
    INSERT INTO TransactionHistory(type, time, message) VALUES('Phantom reads', CONVERT(TIME, GETDATE()), 'PR succeeded')
    END
END



--DEADLOCK
BEGIN
    --TRANSACTION 1
    BEGIN
    SET TRANSACTION ISOLATION LEVEL SERIALIZABLE
    BEGIN TRY
    BEGIN TRANSACTION
        UPDATE Echipa SET Nume='EchipaNoua' WHERE EchipaID=1;
        WAITFOR DELAY '00:00:10'
        UPDATE Fotbalist SET Nume = Nume + ' EchipaNoua' WHERE EchipaID=1;
    COMMIT TRANSACTION
    END TRY
    begin catch
        ROLLBACK TRANSACTION
        PRINT 'VICTIM1'
        INSERT INTO TransactionHistory(type, time, message) VALUES('Deadlock', CONVERT(TIME, GETDATE()), 'Trans 1 is the victim')
    end catch
    END

    --TRANSACTION 2
    BEGIN
    SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
    BEGIN TRY
    BEGIN TRANSACTION
        UPDATE Fotbalist SET Nume = Nume + ' EchipaNoua' WHERE EchipaID=2;
        WAITFOR DELAY '00:00:10'
        UPDATE Echipa SET Nume='EchipaNoua' WHERE EchipaID=2;
    COMMIT TRANSACTION
    INSERT INTO TransactionHistory(type, time, message) VALUES('Deadlock', CONVERT(TIME, GETDATE()), 'Trans 2 succeeded')
    END TRY
    begin catch
        ROLLBACK TRANSACTION
        PRINT 'VICTIM2'
        INSERT INTO TransactionHistory(type, time, message) VALUES('Deadlock', CONVERT(TIME, GETDATE()), 'Trans 2 is the victim')
    end catch
    END

     --TRANSACTION 2 SOLUTION
    BEGIN
    SET DEADLOCK_PRIORITY HIGH;
    BEGIN TRY
    BEGIN TRANSACTION
        UPDATE Fotbalist SET Nume = Nume + ' EchipaNoua' WHERE EchipaID=2;
        WAITFOR DELAY '00:00:10'
        UPDATE Echipa SET Nume='EchipaNoua' WHERE EchipaID=2;
    COMMIT TRANSACTION
    INSERT INTO TransactionHistory(type, time, message) VALUES('Deadlock', CONVERT(TIME, GETDATE()), 'Trans 2 succeeded')
    END TRY
    begin catch
        ROLLBACK TRANSACTION
        PRINT 'VICTIM2'
        INSERT INTO TransactionHistory(type, time, message) VALUES('Deadlock', CONVERT(TIME, GETDATE()), 'Trans 2 is the victim')
    end catch
    END
end


----------------C#---------------------------

go

CREATE PROCEDURE usp_run_thread1
AS
    BEGIN
        SET TRANSACTION ISOLATION LEVEL SERIALIZABLE
        BEGIN TRANSACTION
        UPDATE Echipa SET Nume='EchipaNoua' WHERE EchipaID=1;
        WAITFOR DELAY '00:00:10'
        UPDATE Fotbalist SET Nume = Nume + ' EchipaNoua' WHERE EchipaID=1;
        COMMIT TRANSACTION
    END;
GO





