/* Creates the DB and initial user account and privleges */

CREATE database bpmcore DEFAULT CHARACTER SET utf8
CHARACTER SET = utf8;
USE bpmcore;

/* enable remote access to the database */
GRANT ALL ON bpmcore.* TO idmuser@'*' IDENTIFIED BY 'idmuser';


CREATE database bpmhistory DEFAULT CHARACTER SET utf8
CHARACTER SET = utf8;
USE bpmhistory;

GRANT ALL ON bpmhistory.* TO idmuser@'*' IDENTIFIED BY 'idmuser';

