```code
-- Step 1: Add an ID column
ALTER TABLE Transactions
ADD ID BIGINT IDENTITY(1,1);

-- Step 2: Drop the composite PK
ALTER TABLE Transactions
DROP CONSTRAINT PK_Transactions;

-- Step 3: Add ID as the new PK
ALTER TABLE Transactions
ADD CONSTRAINT PK_Transactions_New PRIMARY KEY CLUSTERED (ID);







````
