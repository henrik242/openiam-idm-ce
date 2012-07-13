USE openiam;

CREATE TABLE IDENTITY_QUEST_GRP (
       IDENTITY_QUEST_GRP_ID nvarchar(32) NOT NULL,
       NAME                 nvarchar(60) NULL,
       STATUS               nvarchar(20) NULL,
       COMPANY_OWNER_ID     nvarchar(32) NULL,
       CREATE_DATE          datetime NULL,
       CREATED_BY           nvarchar(20) NULL,
       LAST_UPDATE          datetime NULL,
       LAST_UPDATED_BY      nvarchar(20) NULL,
       PRIMARY KEY (IDENTITY_QUEST_GRP_ID)
)

CREATE TABLE IDENTITY_QUESTION (
       IDENTITY_QUESTION_ID 	nvarchar(32) NOT NULL,
       IDENTITY_QUEST_GRP_ID 	nvarchar(32) NULL,
       QUESTION_TEXT        	nvarchar(255) NULL,
       REQUIRED             	int NULL,
	   ACTIVE					INT NULL,
 		USER_ID			nvarchar(32),	
       PRIMARY KEY (IDENTITY_QUESTION_ID),
       CONSTRAINT FK_IDENTITY_QUESTION_IDENTITY_QUEST_GRP
              FOREIGN KEY (IDENTITY_QUEST_GRP_ID) REFERENCES IDENTITY_QUEST_GRP(IDENTITY_QUEST_GRP_ID)
)  

CREATE TABLE USER_IDENTITY_ANS (
       IDENTITY_ANS_ID      nvarchar(32) NOT NULL,
       IDENTITY_QUESTION_ID nvarchar(32) NULL,
	   QUESTION_TEXT		nvarchar(255) null,
       USER_ID              nvarchar(32) NULL,
       QUESTION_ANSWER      nvarchar(255) NULL,
       PRIMARY KEY (IDENTITY_ANS_ID), 
       CONSTRAINT FK_USER_IDENTITY_ANS_IDENTITY_QUESTION
       		FOREIGN KEY (IDENTITY_QUESTION_ID)  REFERENCES IDENTITY_QUESTION(IDENTITY_QUESTION_ID), 
       CONSTRAINT FK_USER_IDENTITY_ANS_USERS
       		FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID)
)  

CREATE TABLE POLICY_DEF (
       POLICY_DEF_ID        nvarchar(32) NOT NULL,
       NAME                 nvarchar(60) NULL,
       DESCRIPTION          nvarchar(255) NULL,
       POLICY_TYPE          nvarchar(20) NULL,
       LOCATION_TYPE        nvarchar(20) NULL,
       POLICY_RULE_TEXT          nvarchar(500) NULL,
       POLICY_HANDLER       nvarchar(255) NULL,
	POLICY_ADVISE_HANDLER	nvarchar(255) NULL,
       PRIMARY KEY (POLICY_DEF_ID)
)  

CREATE TABLE POLICY (
       POLICY_ID            nvarchar(32) NOT NULL,
       POLICY_DEF_ID        nvarchar(32) NULL,
       NAME                 nvarchar(60) NULL,
       DESCRIPTION          nvarchar(255) NULL,
       STATUS           	int NULL,
       CREATE_DATE          datetime NULL,
       CREATED_BY           nvarchar(20) NULL,
       LAST_UPDATE          datetime NULL,
       LAST_UPDATED_BY      nvarchar(20) NULL,
       RULE_SRC_URL         nvarchar(80) NULL,
	   RULE_TEXT			TEXT NULL,
	   ENABLEMENT			INT DEFAULT 1, 
	   PRIMARY KEY (POLICY_ID), 
       CONSTRAINT FK_POLICY_POLICY_DEF
       	FOREIGN KEY (POLICY_DEF_ID) REFERENCES POLICY_DEF(POLICY_DEF_ID)
)  



CREATE TABLE POLICY_DEF_PARAM (
       DEF_PARAM_ID         nvarchar(32) NOT NULL,
       POLICY_DEF_ID        nvarchar(32) NULL,
       NAME                 nvarchar(60) NULL,
       DESCRIPTION          nvarchar(255) NULL,
       OPERATION            nvarchar(20) NULL,
       VALUE1               nvarchar(255) NULL,
       VALUE2               nvarchar(255) NULL,
       REPEATS               int NULL,
	   PARAM_GROUP			nvarchar(20) NULL,
 	   HANDLER_LANGUAGE		nvarchar(20)  NULL,
	POLICY_PARAM_HANDLER nvarchar(255) NULL,
       PRIMARY KEY (DEF_PARAM_ID), 
       CONSTRAINT FK_POLICY_DEF_PARAM_POLICY_DEF
       		FOREIGN KEY (POLICY_DEF_ID) REFERENCES POLICY_DEF(POLICY_DEF_ID)
)  

CREATE TABLE POLICY_ATTRIBUTE (
       POLICY_ATTR_ID       nvarchar(32) NOT NULL,
       DEF_PARAM_ID         nvarchar(32) NULL,
       POLICY_ID            nvarchar(32) NULL,
       NAME                 nvarchar(60) NULL,
       OPERATION            nvarchar(20) NULL,
       VALUE1               nvarchar(255) NULL,
       VALUE2               nvarchar(255) NULL,
	   RULE_TEXT_TEXT					TEXT NULL,
       PRIMARY KEY (POLICY_ATTR_ID),
       CONSTRAINT FK_POLICY_ATTRIBUTE_POLICY_DEF_PARAM
       		FOREIGN KEY (DEF_PARAM_ID) REFERENCES POLICY_DEF_PARAM(DEF_PARAM_ID), 
       CONSTRAINT FK_POLICY_ATTRIBUTE_POLICY
       		FOREIGN KEY (POLICY_ID) REFERENCES POLICY(POLICY_ID)
);

CREATE TABLE POLICY_OBJECT_ASSOC (
   POLICY_OBJECT_ID nvarchar(32) NOT NULL,
   POLICY_ID nvarchar(32) NULL,
   ASSOCIATION_LEVEL nvarchar(20) NULL,
   ASSOCIATION_VALUE nvarchar(255) NULL,
   OBJECT_TYPE nvarchar(100) NULL,
   OBJECT_ID nvarchar(32) NULL,
	PARENT_ASSOC_ID nvarchar(32) NULL,
   PRIMARY KEY (POLICY_OBJECT_ID)
);



CREATE TABLE ENTITLEMENT (
       ENTITLEMENT_ID       nvarchar(32) NOT NULL,
       ENTITLEMENT_NAME     nvarchar(40) NULL,
       ENTITLEMENT_VALUE    nvarchar(80) NULL,
       DESCRIPTION          nvarchar(255) NULL,
       CREATE_DATE          datetime NULL,
       CREATED_BY           nvarchar(20) NULL,
       PRIMARY KEY (ENTITLEMENT_ID)
)  

CREATE TABLE ROLE_ENTITLEMENT (
       ENTITLEMENT_ID       nvarchar(32) NOT NULL,
       ROLE_ID              nvarchar(32) NOT NULL,
       SERVICE_ID           nvarchar(20) NOT NULL,
       PRIMARY KEY (ENTITLEMENT_ID, ROLE_ID),
       CONSTRAINT RL_EN_RL
       	FOREIGN KEY (ROLE_ID, SERVICE_ID) REFERENCES ROLE (ROLE_ID, SERVICE_ID),
	   CONSTRAINT RL_EN_EN
       	FOREIGN KEY (ENTITLEMENT_ID) REFERENCES ENTITLEMENT(ENTITLEMENT_ID)
)  



CREATE TABLE RESOURCE_POLICY (
       RESOURCE_POLICY_ID   nvarchar(32) NOT NULL,
	   SERVICE_ID           nvarchar(20) NOT NULL,
       ROLE_ID              nvarchar(32) NULL,
       RESOURCE_ID          nvarchar(32) NULL,
       POLICY_START         datetime NULL,
       POLICY_END           datetime NULL,
       APPLY_TO_CHILDREN    int NULL,
       PRIMARY KEY (RESOURCE_POLICY_ID), 
       CONSTRAINT RS_PL_RL_RLID 
       		FOREIGN KEY (ROLE_ID,SERVICE_ID)
                             REFERENCES ROLE (ROLE_ID, SERVICE_ID), 
       CONSTRAINT RS_PL_RS_RSID 
       		FOREIGN KEY (RESOURCE_ID)
                             REFERENCES RES(RESOURCE_ID)
)  


CREATE TABLE PROVISION_CONNECTOR (
	CONNECTOR_ID 			nvarchar(32) NOT NULL,
	NAME		 			nvarchar(40) NULL,
	METADATA_TYPE_ID		nvarchar(20) NULL,
	STD_COMPLIANCE_LEVEL	nvarchar(20) NULL,
	CLIENT_COMM_PROTOCOL	nvarchar(20) NULL,
	SERVICE_URL				nvarchar(100) NULL,
	SERVICE_NAMESPACE		nvarchar(100) NULL,
	SERVICE_PORT			nvarchar(100) NULL,
	SERVICE_WSDL			nvarchar(100) NULL,
	CLASS_NAME				nvarchar(60) NULL,
	HOST					nvarchar(60) NULL,
    PORT					nvarchar(10) NULL,
    CONNECTOR_INTERFACE 	nvarchar(20) NULL,
	PRIMARY KEY(CONNECTOR_ID)
)  



CREATE TABLE MANAGED_SYS (
       MANAGED_SYS_ID           			nvarchar(32) NOT NULL,
       NAME         						nvarchar(40) NULL,
 	   DESCRIPTION							nvarchar(80) NULL,
       STATUS								nvarchar(20) NULL,
	   CONNECTOR_ID							nvarchar(32) NULL,
	   DOMAIN_ID							nvarchar(20) NULL,
	   HOST_URL								nvarchar(80) NULL,
	   APPL_URL								nvarchar(80) NULL,
	   PORT									int NULL,
	   COMM_PROTOCOL						nvarchar(20) NULL,
	   /* Need to accomodate fully qualified ldap names */
	   USER_ID								nvarchar(150) NULL,
	   PSWD									nvarchar(100) NULL,
	   START_DATE							DATE NULL,
	   END_DATE								DATE NULL,
	   RESOURCE_ID							nvarchar(32) NULL,
	   PRIMARY_REPOSITORY 					INT NULL,
	   SECONDARY_REPOSITORY_ID 				nvarchar(32) NULL,
	   ALWAYS_UPDATE_SECONDARY 				INT NULL,
	   RES_DEPENDENCY						nvarchar(32) NULL,
	   ADD_HNDLR							nvarchar(100) NULL,
	   MODIFY_HNDLR							nvarchar(100) NULL,
	   DELETE_HNDLR							nvarchar(100) NULL,
	   SETPASS_HNDLR						nvarchar(100) NULL,
	   SUSPEND_HNDLR						nvarchar(100) NULL,	   
	   HNDLR_1									nvarchar(100) NULL,
	   HNDLR_2									nvarchar(100) NULL,
 	   HNDLR_3									nvarchar(100) NULL,
 	   HNDLR_4									nvarchar(100) NULL,
	   HNDLR_5									nvarchar(100) NULL,
       DRIVER_URL                           nvarchar(100) NULL,
       CONNECTION_STRING                    nvarchar(100) NULL,
       PRIMARY KEY (MANAGED_SYS_ID),
			 CONSTRAINT FK_MNG_SYS_PROV_CON
			 	FOREIGN KEY (CONNECTOR_ID) REFERENCES PROVISION_CONNECTOR(CONNECTOR_ID)
)  

CREATE TABLE MNG_SYS_OBJECT_MATCH (
		OBJECT_SEARCH_ID	nvarchar(32) NOT NULL,
		MANAGED_SYS_ID      nvarchar(32) NOT NULL,
		/* USER, GROUP, ROLE, COMPUTER, RESOURCE, ETC */
		OBJECT_TYPE			nvarchar(20) NULL DEFAULT 'USER',
		/* BASE_DN, SEARCH */ 
		MATCH_METHOD		nvarchar(20) NULL DEFAULT 'BASE_DN',
		BASE_DN				nvarchar(200) NULL,
		SEARCH_BASE_DN		nvarchar(200) NULL,
		SEARCH_FILTER		nvarchar(1000) NULL,
		KEY_FIELD			nvarchar(40) NULL,
       PRIMARY KEY (OBJECT_SEARCH_ID),
			 CONSTRAINT FK_MNG_SYS_OBJ_MATC
			 	FOREIGN KEY (MANAGED_SYS_ID) REFERENCES MANAGED_SYS(MANAGED_SYS_ID)
)  

CREATE TABLE MANAGED_SYS_ATTRIBUTE (
       MNG_SYS_ATTR_ID       nvarchar(32) NOT NULL,
       NAME                 nvarchar(60) NULL,
       VALUE1               nvarchar(255) NULL,
       VALUE2               nvarchar(255) NULL,
       MANAGED_SYS_ID  nvarchar(32) NULL,
       PRIMARY KEY (MNG_SYS_ATTR_ID),
       CONSTRAINT FK_MNG_MNG_ATTR
       		FOREIGN KEY (MANAGED_SYS_ID) REFERENCES MANAGED_SYS(MANAGED_SYS_ID)
) 


CREATE TABLE APPROVER_ASSOC(
    APPROVER_ASSOC_ID     nvarchar(32)    NOT NULL,
	REQUEST_TYPE		nvarchar(32) NULL,			/* Each type of request can have a different approval process. */
	APPROVER_LEVEL      int	   NULL DEFAULT 1,   /* approval level */
	ASSOCIATION_TYPE	nvarchar(20)	   NULL,	/* GROUP, ROLE, SUPERVISOR, INDIVIDUAL, RESOURCE  */
	ASSOCIATION_OBJ_ID	nvarchar(32)	   NULL,	/* ID OF GROUP, ROLE, SUPERVISOR,  OR RESOURCE   */
	APPROVER_USER_ID	nvarchar(32)	   NULL,    /* ID OF THE APPROVER - WE CAN HAVE MANY APPROVERS  */
	ON_APPROVE_NOTIFY_USER_ID nvarchar(32)   NULL,
	ON_REJECT_NOTIFY_USER_ID  nvarchar(32)   NULL,
	APPROVE_NOTIFY_USER_TYPE nvarchar(20)   NULL,  /* USER, SUPERVISOR, TARGET_USER*/
	REJECT_NOTIFY_USER_TYPE  nvarchar(20)   NULL,
	ACTN    			nvarchar(32)	   NULL DEFAULT 'START',   /* START, ACCEPT, REJECT - FUTURE USE  */
	APPROVER_ROLE_ID nvarchar(32)  NULL,
	APPROVER_ROLE_DOMAIN nvarchar(20)  NULL,
	APPLY_DELEGATION_FILTER int  NULL,
PRIMARY KEY (APPROVER_ASSOC_ID) 
)  




CREATE TABLE MNG_SYS_GROUP(
    MNG_SYS_GROUP_ID    nvarchar(32)    NOT NULL,
    MANAGED_SYS_ID      nvarchar(32)    NOT NULL,
    PRIMARY KEY (MANAGED_SYS_ID, MNG_SYS_GROUP_ID), 
    CONSTRAINT Refmanaged_sys831 FOREIGN KEY (MANAGED_SYS_ID)
    REFERENCES MANAGED_SYS(MANAGED_SYS_ID)
)  




CREATE TABLE PROV_REQUEST(
    REQUEST_ID        nvarchar(32)     NOT NULL,
    REQUESTOR_ID      nvarchar(100),
    REQUEST_DATE      DATETIME,
    STATUS            nvarchar(20),
    STATUS_DATE       DATETIME,
    REQUEST_REASON    nvarchar(500),
	REQUEST_TYPE 		nvarchar(20),
	CHANGE_ACCESS_BY	nvarchar(32),
	REQUEST_XML 		TEXT,
    NEW_ROLE_ID              nvarchar(32) NULL,
    NEW_SERVICE_ID           nvarchar(20) NULL,
	MANAGED_RESOURCE_ID	nvarchar(32) NULL,
    REQUEST_FOR_ORG_ID  nvarchar(32) NULL,
    PRIMARY KEY (REQUEST_ID)
)  

CREATE TABLE REQ_APPROVER(
    REQ_APPROVER_ID     nvarchar(32)    NOT NULL,
    APPROVER_ID         nvarchar(32)	   NULL,
    APPROVER_TYPE		nvarchar(20)    NULL,   /* MANAGER, APP_APPROVER, FORM_APPROVER */
    APPROVER_LEVEL      int	   NULL,
    REQUEST_ID          nvarchar(32)    NULL,
    ACTION_DATE         DATETIME       NULL,
    STATUS              nvarchar(20)    NULL,  /* PENDING, COMPLETE */
    ACTION              nvarchar(20)    NULL,  /* APPROVE, REJECT, DELEGATE */
    CMT             nvarchar(1000)   NULL,
    MANAGED_SYS_ID      nvarchar(32)    NULL,
    MNG_SYS_GROUP_ID	nvarchar(32)	NULL,
    ROLE_DOMAIN	        nvarchar(20)	NULL,
    PRIMARY KEY (REQ_APPROVER_ID)
)  


CREATE TABLE MNG_SYS_LIST(
    MANAGED_SYS_ID    nvarchar(32)    NOT NULL,
    REQUEST_ID        nvarchar(32)    NOT NULL,
    PRIMARY KEY (MANAGED_SYS_ID, REQUEST_ID), 
    CONSTRAINT Refmanaged_sys941 FOREIGN KEY (MANAGED_SYS_ID)
    REFERENCES MANAGED_SYS(MANAGED_SYS_ID),
    CONSTRAINT RefPROV_REQUEST951 FOREIGN KEY (REQUEST_ID)
    REFERENCES PROV_REQUEST(REQUEST_ID)
)  

CREATE TABLE REQUEST_ATTACHMENT(
    REQUEST_ATTACHMENT_ID    nvarchar(32)    NOT NULL,
    NAME                     nvarchar(20),
    ATTACHMENT               nvarchar(20),
    USER_ID                  nvarchar(32),
    ATTACHMENT_DATE          DATETIME,
    REQUEST_ID               nvarchar(32)    NOT NULL,
    PRIMARY KEY (REQUEST_ATTACHMENT_ID), 
    CONSTRAINT RefPROV_REQUEST1001 FOREIGN KEY (REQUEST_ID)
    REFERENCES PROV_REQUEST(REQUEST_ID)
)  

CREATE TABLE REQUEST_ATTRIBUTE(
    REQUEST_ATTR_ID    nvarchar(32)    NOT NULL,
    NAME               nvarchar(40),
    VALUE              nvarchar(255),
    METADATA_ID        nvarchar(32),
    ATTR_GROUP         nvarchar(20),
    REQUEST_ID         nvarchar(32)    NULL,
    PRIMARY KEY (REQUEST_ATTR_ID), 
    CONSTRAINT RefPROV_REQUEST1011 FOREIGN KEY (REQUEST_ID)
    REFERENCES PROV_REQUEST(REQUEST_ID)
)  

CREATE TABLE REQUEST_USER(
    REQ_USER_ID    nvarchar(32)    NOT NULL,
    USER_ID             nvarchar(32),
    FIRST_NAME          nvarchar(20),
    LAST_NAME           nvarchar(20),
    MIDDLE_INIT         nvarchar(20),
    DEPT_CD             nvarchar(20),
    DIVISION            nvarchar(20),
    LOCATION_CD         nvarchar(20),
    AFFILIATION         nvarchar(20),
    REQUEST_ID          nvarchar(32)   NULL,
    PRIMARY KEY (REQ_USER_ID), 
    CONSTRAINT RefPROV_REQUEST931 FOREIGN KEY (REQUEST_ID)
    REFERENCES PROV_REQUEST(REQUEST_ID)
)  





CREATE TABLE ATTRIBUTE_MAP (
	ATTRIBUTE_MAP_ID nvarchar(32) NOT NULL,
	MANAGED_SYS_ID nvarchar(32) NOT NULL,
	RESOURCE_ID nvarchar(32) NULL,
	/* User, Group, OTHER */
	MAP_FOR_OBJECT_TYPE nvarchar(20) DEFAULT 'USER',
	ATTRIBUTE_NAME	nvarchar(50) NULL,
	/* CN, DN, UID, ETC   */
	TARGET_ATTRIBUTE_NAME nvarchar(50) NULL,
	/* IS AUTHORITATIVE SRC. 0-FALSE, 1-TRUE */
	AUTHORITATIVE_SRC int default 0,
	ATTRIBUTE_POLICY_ID  nvarchar(32) NULL,
	RULE_TEXT_TEXT			text null,
	STATUS nvarchar(20) default 'ACTIVE',
	START_DATE 		date default NULL,
	END_DATE 			date default NULL,
	STORE_IN_IAMDB INT DEFAULT 0,
	DATA_TYPE       nvarchar(20) NULL,
	PRIMARY KEY  (ATTRIBUTE_MAP_ID)
)  


CREATE TABLE RECONCILIATION_CONFIG (
  RECON_CONFIG_ID	 nvarchar(32) NOT NULL,
  RESOURCE_ID 		nvarchar(32) NULL,
  FREQUENCY         nvarchar(20) NULL,
  STATUS            nvarchar(20) NULL DEFAULT 'ACTIVE',
  PRIMARY KEY  (RECON_CONFIG_ID)
)  

CREATE TABLE RECONCILIATION_SITUATION (
  RECON_SITUATION_ID	 	nvarchar(32) NOT NULL,
  RECON_CONFIG_ID 			nvarchar(32) NULL,
  SITUATION					nvarchar(40) NULL,
  SITUATION_RESP		    nvarchar(40) NULL,
  SCRIPT				    nvarchar(80) NULL,
  PRIMARY KEY  (RECON_SITUATION_ID),
CONSTRAINT RECON_SITUATION FOREIGN KEY (RECON_CONFIG_ID)
    REFERENCES RECONCILIATION_CONFIG(RECON_CONFIG_ID)
)  




CREATE TABLE SYNCH_CONFIG (
       SYNCH_CONFIG_ID        	nvarchar(32) NOT NULL,
       NAME       				nvarchar(60) 	NULL,
	   STATUS       			nvarchar(20) 	NULL,
	   SYNCH_SRC				nvarchar(20) 	NULL, /* CSV FILE, MANAGED_RESOURCE */ 
	   FILE_NAME				nvarchar(80) 	NULL,
	   SRC_LOGIN_ID				nvarchar(100) 	NULL,
	   SRC_PASSWORD				nvarchar(100) 	NULL,
	   SRC_HOST					nvarchar(100) 	NULL,
       DRIVER					nvarchar(100) 	NULL,
	   CONNECTION_URL			nvarchar(100) 	NULL,
	   QUERY					nvarchar(1000) 	NULL,
	   QUERY_TIME_FIELD			nvarchar(50) 	NULL,   /* FIELD IN THE TABLE THAT SHOULD BE USED WHEN DOING INCREMENTAL SEARCH */
	   BASE_DN                  nvarchar(50)     NULL,
	   LAST_EXEC_TIME			DATETIME		NULL,
	   LAST_REC_PROCESSED       nvarchar(32)     NULL,
	   MANAGED_SYS_ID			nvarchar(32) 	NULL, 
	   LOAD_MATCH_ONLY			INT 			NULL DEFAULT 0,
	   UPDATE_ATTRIBUTE     	INT 			NULL DEFAULT 1,
	   SYNCH_FREQUENCY			nvarchar(20) 	NULL,
	   SYNCH_TYPE				nvarchar(20) 	NULL,
	   DELETE_RULE_TEXT				nvarchar(80) 	NULL,
	   PROCESS_RULE_TEXT				nvarchar(80)	 	NULL,
	   VALIDATION_RULE_TEXT			nvarchar(80) 	NULL,
	   TRANSFORMATION_RULE_TEXT		nvarchar(80) 	NULL,
	   MATCH_FIELD_NAME			nvarchar(40) 	NULL,
	   MATCH_MANAGED_SYS_ID 	nvarchar(32) 	NULL,
	   MATCH_SRC_FIELD_NAME 	nvarchar(40) 	NULL,
	   CUSTOM_MATCH_RULE_TEXT		nvarchar(100) 	NULL,
	   CUSTOM_ADAPTER_SCRIPT	nvarchar(100) 	NULL,	
	   CUSTOM_MATCH_ATTR		nvarchar(40) 	NULL,
	   WS_URL	                nvarchar(100) 	NULL,
	   WS_SCRIPT		        nvarchar(100) 	NULL,
PRIMARY KEY (SYNCH_CONFIG_ID) 
)  


CREATE TABLE SYNCH_CONFIG_DATA_MAPPING(
    MAPPING_ID    		nvarchar(32)    NOT NULL,
    SYNCH_CONFIG_ID     nvarchar(32) NULL,
    IDM_FIELD_NAME      nvarchar(40),
	SRC_FIELD_NAME		nvarchar(40),
	MULTIVALUED         INT NULL,
    PRIMARY KEY (MAPPING_ID), 
    CONSTRAINT SYNCH_DATA_MAP FOREIGN KEY (SYNCH_CONFIG_ID)
    REFERENCES SYNCH_CONFIG(SYNCH_CONFIG_ID)
)  
GO

CREATE VIEW USER_ROLE_VW
AS
SELECT ug.USER_ID, gr.ROLE_ID, gr.SERVICE_ID
FROM USER_GRP ug LEFT JOIN GRP_ROLE gr on (ug.GRP_ID = gr.GRP_ID)
		 WHERE gr.ROLE_ID is not null
 UNION
SELECT ur.USER_ID,  ur.ROLE_ID, ur.SERVICE_ID
FROM USER_ROLE ur
	WHERE ur.ROLE_ID is not null;

GO

CREATE VIEW USER_EMAIL_VW AS
	SELECT *
	FROM EMAIL_ADDRESS
	WHERE NAME = 'EMAIL1' AND PARENT_TYPE='USER';
GO

CREATE VIEW USER_PHONE_VW AS
SELECT *
FROM PHONE
WHERE NAME = 'DESK PHONE' AND PARENT_TYPE='USER';

GO
	
CREATE VIEW USER_IDENTITY_VW AS
  SELECT u.*, l.auth_fail_count, l.canonical_name,
  l.current_login_host, l.first_time_login, l.grace_period,
  l.identity_type, l.is_default, l.is_locked, l.last_auth_attempt, l.last_login,
  l.last_login_ip, l.login, l.managed_sys_id, l.prev_login, l.prev_login_ip,
  l.pwd_change_count, l.pwd_exp, l.reset_pwd, l.service_id
  FROM LOGIN l, USERS u
  WHERE l.USER_ID = u.USER_ID;


GO

CREATE VIEW USER_PSWD_EXPIRED_YESTERDAY_VW AS
select LOGIN,l.SERVICE_ID, l.MANAGED_SYS_ID, l.USER_ID, GRACE_PERIOD AS EXPIRATION_DATE,  u.FIRST_NAME, u.LAST_NAME, u.STATUS
FROM LOGIN l,  USERS u
WHERE l.USER_ID = u.USER_ID AND
    MANAGED_SYS_ID = 0 AND
(    (
    GRACE_PERIOD IS NOT NULL AND
    CONVERT(DATE, GRACE_PERIOD) = DATEADD(DAY,-1,CONVERT(DATE,GETDATE()))
    )
   OR
   (
    PWD_EXP IS NOT NULL AND GRACE_PERIOD IS NULL AND
    CONVERT(DATE, PWD_EXP) = DATEADD(DAY,-1,CONVERT(DATE,GETDATE()))
    )
)

GO