/* test authentication */

insert into USERS (user_id,first_name, last_name, STATUS, COMPANY_ID  ) values('3100','Active','User','ACTIVE','100');
insert into LOGIN(SERVICE_ID, LOGIN, MANAGED_SYS_ID, USER_ID, PASSWORD,RESET_PWD, IS_LOCKED, AUTH_FAIL_COUNT) VALUES('IDM','activeuser','0','3100','b83a81d1b3f5f209a70ec02c3d7f7fc5',0,0,0);

insert into USERS (user_id,first_name, last_name, STATUS, COMPANY_ID  ) values('3101','ubActive','User','INACTIVE','100');
insert into LOGIN(SERVICE_ID, LOGIN, MANAGED_SYS_ID, USER_ID, PASSWORD,RESET_PWD, IS_LOCKED, AUTH_FAIL_COUNT) VALUES('IDM','inactiveuser','0','3101','b83a81d1b3f5f209a70ec02c3d7f7fc5',0,0,0);

insert into USERS (user_id,first_name, last_name, STATUS, SECONDARY_STATUS, COMPANY_ID  ) values('3102','locked','User','ACTIVE','LOCKED','100');
insert into LOGIN(SERVICE_ID, LOGIN, MANAGED_SYS_ID, USER_ID, PASSWORD,RESET_PWD, IS_LOCKED, AUTH_FAIL_COUNT) VALUES('IDM','lockeduser','0','3102','b83a81d1b3f5f209a70ec02c3d7f7fc5',0,0,0);



/* TABLES TO TEST THE DATABASE CONNECTOR */
CREATE TABLE TEST_HR_SRC(
       EMPL_ID              varchar(32) NOT NULL,
       FIRST_NAME           	varchar(50) NULL,
       LAST_NAME            varchar(50) NULL,
       MIDDLE_INIT          VARCHAR(50) NULL,
       JOB_CODE			    VARCHAR(30) NULL,
       AREA_CD			    VARCHAR(3) NULL,
       PHONE_NBR			  VARCHAR(10) NULL,
       EMAIL                VARCHAR(50) NULL,
       PRIMARY KEY (EMPL_ID)
) ENGINE=InnoDB;


INSERT INTO TEST_HR_SRC(EMPL_ID, FIRST_NAME, LAST_NAME, JOB_CODE,AREA_CD,PHONE_NBR,EMAIL) VALUES('1233','JOHN', 'SMITH', '100','914','123-4567','TEST@OPENIAM.COM');
INSERT INTO TEST_HR_SRC(EMPL_ID, FIRST_NAME, LAST_NAME, JOB_CODE,AREA_CD,PHONE_NBR,EMAIL) VALUES('1234','ROGER', 'RABBIT', '100','914','123-4589','TEST2@OPENIAM.COM');
INSERT INTO TEST_HR_SRC(EMPL_ID, FIRST_NAME, LAST_NAME, JOB_CODE) VALUES('1235','William', 'Smith', '100');
INSERT INTO TEST_HR_SRC(EMPL_ID, FIRST_NAME, LAST_NAME, JOB_CODE) VALUES('1236','Jeffery', 'Aber', '100');


CREATE DEFINER=`root`@`localhost` PROCEDURE `createTestSyncData`()
BEGIN

  DECLARE ctr INT DEFAULT 1;

  WHILE ctr < 2500 DO

      insert into TEST_HR_SRC (EMPL_ID, FIRST_NAME, LAST_NAME, MIDDLE_INIT, JOB_CODE)
      VALUES(CONCAT('EMP_',CTR), CONCAT('WILLIAM',CTR), CONCAT('SMITH',CTR), 'X', '100');

    SET ctr = ctr + 1;
  END WHILE;


END

/* test app table connector */

USE openiam;

create table MY_USR_TABLE
(
TABLE_ID      INTEGER NOT NULL  AUTO_INCREMENT,
LOGIN_ID      VARCHAR(60) NOT NULL,
USER_PASSWORD VARCHAR(60) NOT NULL,
FIRST_NAME    VARCHAR(40) NULL,
LAST_NAME     VARCHAR(40) NULL,
EMPLOYEE_ID   VARCHAR(20) NULL,
DOB           DATE NULL,
CREATE_DATE   DATETIME NULL,
USR_ROLE      VARCHAR(20),
PHONE_EXT     INT NULL,
PHONE_NBR     VARCHAR(20) NULL,
PRIMARY KEY (TABLE_ID)
)



create table JF_ADM_USERS
(
  ULT_ID               INTEGER not null,
  OWNR_CARR_CODE       VARCHAR(5) not null,
  USR_NAME             VARCHAR(15) default 'R' not null,
  FIRST_NAME           VARCHAR(30),
  LAST_NAME            VARCHAR(30),
  USR_TYP              VARCHAR(5),
  USR_DSGN             VARCHAR(30),
  SALUTATION           VARCHAR(5),
  DOB                  DATETIME,
  MAX_SESNS            INTEGER(3),
  CRNT_SESNS           INTEGER(3),
  LANGUAGE             VARCHAR(30),
  APLN_ADMIN_IND       VARCHAR(1),
  SYS_ADMIN_IND        VARCHAR(1),
  LAST_LOGIN_DATT      DATETIME,
  LAST_LOGOUT_DATT     DATETIME,
  GRACE_LOGINS_RMNG    INTEGER(2),
  ONLINE_IND           VARCHAR(1),
  USR_PWD              VARCHAR(255),
  USR_PWD_CODE         VARCHAR(255),
  USR_PWD_CODE_VAL     VARCHAR(255),
  REG_MODE             VARCHAR(10),
  ACNT_ENABLED         VARCHAR(1),
  PWD_EXPRY_DT         DATETIME,
  PWD_EXPRY_ALRT_DT    DATETIME,
  TRACE_ENABLED        VARCHAR(1),
  STAFF_ID             VARCHAR(10),
  DFLT_APLN_CLASSIFIER VARCHAR(5) not null,
  MGR_ID               VARCHAR(15),
  ADRS_ID              INTEGER(15),
  VALID_UNTIL          DATETIME,
  INACTIVE             VARCHAR(1),
  DTL_ID               INTEGER(15),
  CREATED_DATE         DATETIME,
  CREATED_BY           VARCHAR(30),
  MODIFIED_DATE        DATETIME,
  MODIFIED_BY          VARCHAR(30),
  DESKTOP_ALOWD_IND    VARCHAR(1) default 'N',
  USR_ROLE             VARCHAR(50)
);

create table JF_ADM_GROUPS
(
  UGR_ID         INTEGER(15) not null,
  OWNR_CARR_CODE VARCHAR(5) not null,
  USR_GRP        VARCHAR(30),
  USR_GRP_DESC   VARCHAR(100),
  INACTIVE       VARCHAR(1),
  DTL_ID         INTEGER(15),
  CREATED_DATE   DATETIME,
  CREATED_BY     VARCHAR(30),
  MODIFIED_DATE  DATETIME,
  MODIFIED_BY    VARCHAR(30)
);


create table JF_ADM_USER_GROUP_MAP
(
  GUG_ID        INTEGER(15) not null,
  UGR_ID        INTEGER(15) not null,
  ULT_ID        INTEGER(15) not null,
  GMR_CODE      VARCHAR(20),
  INACTIVE      VARCHAR(1),
  DTL_ID        INTEGER(15),
  CREATED_DATE  DATETIME,
  CREATED_BY    VARCHAR(30),
  MODIFIED_DATE DATETIME,
  MODIFIED_BY   VARCHAR(30)
);


create table APP_USER (
  APP_USER_ID          VARCHAR(32) not null,
  LOGIN_NAME           VARCHAR(50),
  FIRST_NAME           VARCHAR(30),
  LAST_NAME            VARCHAR(30),
  MIDDLE_INIT          VARCHAR(1),
  DOB                  DATETIME,
  EMLOYEE_ID           VARCHAR(50),
  PASSWORD             VARCHAR(255),
  USR_ROLE_NAME        VARCHAR(50),
  PRIMARY KEY (APP_USER_ID)
);
