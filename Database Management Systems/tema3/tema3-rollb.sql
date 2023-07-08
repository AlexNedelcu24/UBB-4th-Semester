use ChampionsLeague
go



-- Verifica daca anul apari?iei este mai mare de 1900
CREATE FUNCTION valAnAparitie(@anAparitie VARCHAR(50)) RETURNS INT AS
BEGIN
	DECLARE @return INT
	DECLARE @an INT
	SET @return = 1
	SET @an = CAST(@anAparitie AS INT)
	
	IF (@an <= 1900)
		SET @return = 0
		
	RETURN @return
END

-- Verifica daca coeficientul este mai mare de 10
CREATE FUNCTION valCoeficient(@coeficient INT) RETURNS INT AS
BEGIN
	DECLARE @return INT
	SET @return = 1
	
	IF (@coeficient <= 10)
		SET @return = 0
		
	RETURN @return
END

CREATE FUNCTION valNume(@nume VARCHAR(50)) RETURNS INT AS
BEGIN
	DECLARE @return INT
	SET @return = 1 
	IF(@nume = null OR @nume != upper(left(@nume, 1)) + right(@nume, len(@nume) - 1)) 
		SET @return = 0 
	RETURN @return
END





CREATE PROCEDURE AddSuporterEchipaImn
	@numeSuporter VARCHAR(50),
	@prenumeSuporter VARCHAR(50),
	@echipaNume VARCHAR(50),
	@tara VARCHAR(50),
	@coeficient INT,
	@stadionID INT,
	@numeImn VARCHAR(50),
	@anAparitie VARCHAR(50)
AS
BEGIN
	BEGIN TRAN
		BEGIN TRY
			IF (dbo.valNume(@numeSuporter) = 0)
				BEGIN RAISERROR('Numele suporterului trebuie sa inceapa cu litera mare!', 14, 1)
				END
			IF (dbo.valNume(@prenumeSuporter) = 0)
				BEGIN RAISERROR('Prenumele suporterului trebuie sa inceapa cu litera mare!', 14, 1);
				END
			IF (dbo.valNume(@echipaNume) = 0)
				BEGIN RAISERROR('Numele echipei trebuie sa inceapa cu litera mare!', 14, 1);
				END
			IF (dbo.valNume(@tara) = 0)
				BEGIN RAISERROR('Numele tarii trebuie sa inceapa cu litera mare!', 14, 1);
				END
			IF (dbo.valNume(@numeImn) = 0)
				BEGIN RAISERROR('Numele imnului trebuie sa inceapa cu litera mare!', 14, 1);
				END
			IF (dbo.valAnAparitie(@anAparitie) = 0)
				BEGIN RAISERROR('Anul apari?iei imnului trebuie sã fie >1900!', 14, 1);
				END
			IF (dbo.valCoeficient(@coeficient) = 0)
				BEGIN RAISERROR('Coeficientul echipei trebuie sã fie >10!', 14, 1);
				END

			INSERT INTO Echipa (Nume, Tara, Coeficient, StadionID)
			VALUES (@echipaNume, @tara, @coeficient, @stadionID);
			DECLARE @echipaID INT = SCOPE_IDENTITY();

			INSERT INTO Imn (Nume, An_aparitie)
			VALUES (@numeImn, @anAparitie);
			DECLARE @imnID INT = SCOPE_IDENTITY();

			INSERT INTO Suporter (Nume, Prenume, EchipaID, ImnID)
			VALUES (@numeSuporter, @prenumeSuporter, @echipaID, @imnID);

			COMMIT TRAN
			SELECT 'Transaction committed'
		END TRY
		BEGIN CATCH
			ROLLBACK TRAN
			SELECT 'Transaction rollbacked'
		END CATCH
END


SELECT * FROM Suporter;
SELECT * FROM Echipa;
SELECT * FROM Imn;
EXEC AddSuporterEchipaImn @numeSuporter = 'Stan', @prenumeSuporter = 'Ion', @echipaNume = 'Dinamo', @tara = 'Romania', @coeficient = 20, @stadionID = 1, @numeImn = 'Imnul dinamo', @anAparitie = '1960';



SELECT * FROM Suporter;
SELECT * FROM Echipa;
SELECT * FROM Imn;
EXEC AddSuporterEchipaImn @numeSuporter = 'Stan', @prenumeSuporter = 'Ion', @echipaNume = 'Dinamo', @tara = 'Romania', @coeficient = 20, @stadionID = 1, @numeImn = 'Imnul dinamo', @anAparitie = '1890';



SELECT * FROM Suporter;
SELECT * FROM Echipa;
SELECT * FROM Imn;
EXEC AddSuporterEchipaImn @numeSuporter = @numeSuporter = 'Stan', @prenumeSuporter = 'Ion', @echipaNume = 'Dinamo', @tara = 'Romania', @coeficient = 8, @stadionID = 1, @numeImn = 'Imnul dinamo', @anAparitie = '1960';


CREATE PROCEDURE AddSuporterEchipaImn2
	@numeSuporter VARCHAR(50),
	@prenumeSuporter VARCHAR(50),
	@echipaNume VARCHAR(50),
	@tara VARCHAR(50),
	@coeficient INT,
	@stadionID INT,
	@numeImn VARCHAR(50),
	@anAparitie VARCHAR(50)
AS
BEGIN
	BEGIN TRAN
		BEGIN TRY
			IF (dbo.valNume(@echipaNume) = 0)
				BEGIN RAISERROR('Numele echipei trebuie sa inceapa cu litera mare!', 14, 1);
				END
			IF (dbo.valNume(@tara) = 0)
				BEGIN RAISERROR('Numele tarii trebuie sa inceapa cu litera mare!', 14, 1);
				END
			IF (dbo.valCoeficient(@coeficient) = 0)
				BEGIN RAISERROR('Coeficientul echipei trebuie sã fie >10!', 14, 1);
				END

			INSERT INTO Echipa (Nume, Tara, Coeficient, StadionID)
			VALUES (@echipaNume, @tara, @coeficient, @stadionID);
			DECLARE @echipaID INT = SCOPE_IDENTITY();
			COMMIT TRAN
			SELECT 'Transaction committed'

			BEGIN TRAN
				BEGIN TRY
					IF (dbo.valNume(@numeImn) = 0)
						BEGIN RAISERROR('Numele imnului trebuie sa inceapa cu litera mare!', 14, 1);
						END
					IF (dbo.valAnAparitie(@anAparitie) = 0)
						BEGIN RAISERROR('Anul apari?iei imnului trebuie sã fie >1900!', 14, 1);
						END

					INSERT INTO Imn (Nume, An_aparitie)
					VALUES (@numeImn, @anAparitie);
					DECLARE @imnID INT = SCOPE_IDENTITY();
					COMMIT TRAN
					SELECT 'Transaction committed'

					BEGIN TRAN
						BEGIN TRY
							IF (dbo.valNume(@numeSuporter) = 0)
								BEGIN RAISERROR('Numele suporterului trebuie sa inceapa cu litera mare!', 14, 1)
								END
							IF (dbo.valNume(@prenumeSuporter) = 0)
								BEGIN RAISERROR('Prenumele suporterului trebuie sa inceapa cu litera mare!', 14, 1);
								END

							INSERT INTO Suporter (Nume, Prenume, EchipaID, ImnID)
							VALUES (@numeSuporter, @prenumeSuporter, @echipaID, @imnID);
							COMMIT TRAN
							SELECT 'Transaction committed'
						END TRY
						BEGIN CATCH
							ROLLBACK TRAN
							SELECT 'Transaction rollbacked'
						END CATCH

				END TRY
				BEGIN CATCH
					ROLLBACK TRAN
					SELECT 'Transaction rollbacked'
				END CATCH
		END TRY
		
		BEGIN CATCH
			ROLLBACK TRAN
			SELECT 'Transaction rollbacked'
		END CATCH
END
				

EXEC AddSuporterEchipaImn2 @numeSuporter = 'Varcas', @prenumeSuporter = 'Nicu', @echipaNume = 'CFR', @tara = 'Romania', @coeficient = 20, @stadionID = 1, @numeImn = 'ImnCFR', @anAparitie = '1980';
SELECT * FROM Suporter;
SELECT * FROM Echipa;
SELECT * FROM Imn;

EXEC AddSuporterEchipaImn2 @numeSuporter = 'Varcas', @prenumeSuporter = 'Nicu', @echipaNume = 'cfr', @tara = 'Romania', @coeficient = 20, @stadionID = 1, @numeImn = 'ImnCFR', @anAparitie = '1980';

EXEC AddSuporterEchipaImn2 @numeSuporter = 'Varcas', @prenumeSuporter = 'Nicu', @echipaNume = 'CFR', @tara = 'Romania', @coeficient = 8, @stadionID = 1, @numeImn = 'ImnCFR', @anAparitie = '1980';

EXEC AddSuporterEchipaImn2 @numeSuporter = 'Varcas', @prenumeSuporter = 'Nicu', @echipaNume = 'CFR', @tara = 'Romania', @coeficient = 20, @stadionID = 1, @numeImn = 'ImnCFR', @anAparitie = '1899';
