xpd - eXport ProgressDB Daemon

A program to export Progress database to MySQL.

- Queries tables - saves results as SQL file and populates secondary database

- List of tables to backup:
	1. Part
	2. PartMtl
	3. PartRev
	4. PartBin
	5. PartOpr
	6. PlantWhse
	7. PartTran?
	8. WhseBin
	
- Steps:

1. Query all data from Progress table
2. Save SQL file in file system
3. Populate corresponding MySql table