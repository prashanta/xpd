DELETE FROM `ev_aprvvend` WHERE 1;
DELETE FROM `ev_part` WHERE 1;
DELETE FROM `ev_partbin` WHERE 1;
DELETE FROM `ev_partmtl` WHERE 1;
DELETE FROM `ev_partopr` WHERE 1;
DELETE FROM `ev_partplant` WHERE 1;
DELETE FROM `ev_partrev` WHERE 1;
DELETE FROM `ev_plantwhse` WHERE 1;
DELETE FROM `ev_vendor` WHERE 1;

TRUNCATE TABLE `ev_aprvvend`;
TRUNCATE TABLE `ev_part`;
TRUNCATE TABLE `ev_partbin`;
TRUNCATE TABLE `ev_partmtl`;
TRUNCATE TABLE `ev_partopr`;
TRUNCATE TABLE `ev_partplant`;
TRUNCATE TABLE `ev_partrev`;
TRUNCATE TABLE `ev_plantwhse`;
TRUNCATE TABLE `ev_vendor`;