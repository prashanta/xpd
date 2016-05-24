# xpd - eXport ProgressDB Daemon#

A program to export Progress database to MySQL.

### List of backup tables: ###

1. AprvVend
2. Part
3. PartBin
4. PartMtl
5. PartOpr
6. PartPlant
7. PartRev
8. PlantWhse
9. PODetail
10. POHeader
11. Vendor
	
### Backup Algorithm: ###

0. Check if there are database connections
1. Query all data from Progress table
2. Save SQL file in file system
3. Populate corresponding MySql table

### How to Build ###

To package everything in one neat jar file:

_mvn clean compile package_ 

### Usage ###

***java -jar xpd <Table> <options>***


<Table> is: 

Part, PartMtl, PartRev, PartPlant, AprvVend, PlantWhse, Vendor, PartOpr, PartBin, POHeader, PODetail

<options> is:

--all : Export all tables. Ignores any supplied table names.

--file-only : Save to SQL file only.


### Tuning MySql Performance ###

Inserting large amount of data to MySQL is slow, but the performance can be improved. Steps:

1. Log into MySQL shell, use: _mysql -h localhost -u root -p_
2. Supply password at prompt
3. Check system variables max\_allowed\_packet and net\_buffer\_length, use these commands: SHOW GLOBAL VARIABLES LIKE 'max\_allowed\_packet'; and  SHOW GLOBAL VARIABLES LIKE 'net\_buffer\_length';
4. Issue following commands to import large data:

SET foreign_key_checks = 0;

SET UNIQUE_CHECKS = 0;

SET AUTOCOMMIT = 0;

source d:\path\file.sql;

(Once data import complete)

SET foreign_key_checks = 1;

SET UNIQUE_CHECKS = 1;

SET AUTOCOMMIT = 1;

exit;

Thats all!
