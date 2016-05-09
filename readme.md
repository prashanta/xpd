xpd - eXport ProgressDB Daemon

A program to export Progress database to MySQL.

- Queries tables - saves results as SQL file and populates secondary database

- List of tables to backup:
	1. AprvVend
	2. Part
	3. PartBin
	4. PartMtl
	5. PartOpr
	6. PartPlant
	7. PartRev
	8. PlantWhse
	9. Vendor
	
- Steps:

0. Check if there are database connections
1. Query all data from Progress table
2. Save SQL file in file system
3. Populate corresponding MySql table

## How to Build

To package everything in one neat jar file:

_mvn assembly:single_ 


Usage: xpd [<Table1> <Table2> ...] 

where <Table*> is: Part, PartMtl, PartRev, PartPlant, AprvVend, PlantWhse, Vendor, PartOpr, PartBin
--all : Export all tables
--file-only : Save to SQL file only
