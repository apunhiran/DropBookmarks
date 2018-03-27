--  *********************************************************************
--  Rollback 1 Change(s) Script
--  *********************************************************************
--  Change Log: migrations.xml
--  Ran at: 3/16/18 4:44 PM
--  Against: root@localhost@jdbc:mysql://localhost:3306/DropBookmarks
--  Liquibase version: 3.4.2
--  *********************************************************************

--  Lock Database
UPDATE DATABASECHANGELOGLOCK SET LOCKED = 1, LOCKEDBY = '192.168.31.113 (192.168.31.113)', LOCKGRANTED = '2018-03-16 16:44:45.211' WHERE ID = 1 AND LOCKED = 0;

--  Rolling Back ChangeSet: migrations.xml::3::Apun
DELETE FROM users WHERE id=1;

DELETE FROM DATABASECHANGELOG WHERE ID = '3' AND AUTHOR = 'Apun' AND FILENAME = 'migrations.xml';

--  Release Database Lock
UPDATE DATABASECHANGELOGLOCK SET LOCKED = 0, LOCKEDBY = NULL, LOCKGRANTED = NULL WHERE ID = 1;

