CREATE TABLE SECURITY_DOMAIN (
       DOMAIN_ID            nvarchar(20) NOT NULL,
       NAME         		nvarchar(40) NULL,
       STATUS				nvarchar(20) NULL,
       /* Refers to which managed system to use for authentication */
	   AUTH_SYS_ID			nvarchar(32) NULL,
	   LOGIN_MODULE			nvarchar(100) NULL,
       /* Default policies for the domain */
       PASSWORD_POLICY	    	nvarchar(32) NULL,
       AUTHENTICATION_POLICY	nvarchar(32) NULL,
       AUDIT_POLICY	    		nvarchar(32) NULL,
	PRIMARY KEY (DOMAIN_ID)
);

CREATE TABLE LANGUAGE (
       LANGUAGE_CD          nvarchar(5) NOT NULL,
       LANGUAGE             nvarchar(20) NULL,
	   LOCALE				INT NULL DEFAULT 0,
       PRIMARY KEY (LANGUAGE_CD)
);

CREATE TABLE BATCH_CONFIG (
	   TASK_ID 						nvarchar(32) NOT NULL,
	   TASK_NAME 					nvarchar(50) NULL,
       FREQUENCY					INT NULL,
	   FREQUENCY_UNIT_OF_MEASURE	nvarchar(10) NULL,
	   LAST_EXEC_TIME				DATETIME NULL,
	   ENABLED					    INT NULL DEFAULT 1,
	   TASK_URL            			nvarchar(255) NULL,
	   EXECUTION_ORDER				INT DEFAULT 1 NULL,
	   STATUS 						nvarchar(20) NULL,
	   PARAM1 						nvarchar(255) NULL,
 	   PARAM2 						nvarchar(255) NULL,
 	   PARAM3 						nvarchar(255) NULL,
	   PARAM4 						nvarchar(255) NULL, 
	   RULE_TYPE					nvarchar(20) NULL,
       PRIMARY KEY (TASK_ID)
);



CREATE TABLE EXCLUDE_WORD_LIST (
       WORD                 nvarchar(30) NOT NULL,
       LANGUAGE_CD          nvarchar(5) NOT NULL,
       PRIMARY KEY (WORD, LANGUAGE_CD), 
	CONSTRAINT FK_EXCLUDE_WORD_LIST_LANGUAGE
       FOREIGN KEY (LANGUAGE_CD)
                             REFERENCES LANGUAGE(LANGUAGE_CD)
);

CREATE TABLE CATEGORY(
       CATEGORY_ID          nvarchar(20) NOT NULL,
       PARENT_ID            nvarchar(20) NULL,
       CATEGORY_NAME        nvarchar(40) NULL,
	   	 CATEGORY_DESC        	nvarchar(80) NULL,
       CREATE_DATE			DATETIME NULL,
       CREATED_BY			nvarchar(20) NULL,
       SHOW_LIST            int NULL DEFAULT 0,
       DISPLAY_ORDER        int NULL DEFAULT 0,
       PRIMARY KEY (CATEGORY_ID)
);

CREATE TABLE CATEGORY_LANGUAGE (
       CATEGORY_ID          nvarchar(20) NOT NULL,
       LANGUAGE_CD          nvarchar(5) NOT NULL,
       CATEGORY_NAME        nvarchar(40) NULL,
       PRIMARY KEY (CATEGORY_ID, LANGUAGE_CD), 
	 CONSTRAINT FK_CATEGORY_LANGUAGE_LANGUAGE
       FOREIGN KEY (LANGUAGE_CD)
                             REFERENCES LANGUAGE(LANGUAGE_CD), 
	 CONSTRAINT FK_CATEGORY_LANGUAGE_CATEGORY
       FOREIGN KEY (CATEGORY_ID)
                             REFERENCES CATEGORY(CATEGORY_ID) ON DELETE CASCADE
);



CREATE TABLE METADATA_TYPE (
       TYPE_ID              nvarchar(20) NOT NULL,
       DESCRIPTION          nvarchar(40) NULL,
	   ACTIVE				INTEGER NULL DEFAULT 0,
	   SYNC_MANAGED_SYS		INTEGER NULL DEFAULT 0,
       PRIMARY KEY (TYPE_ID)
);


CREATE TABLE METADATA_ELEMENT (
       METADATA_ID          nvarchar(20) NOT NULL,
       TYPE_ID              nvarchar(20) NOT NULL,
       ATTRIBUTE_NAME       nvarchar(50) NULL,
       DESCRIPTION          nvarchar(40) NULL,
	   MIN_LEN				INT DEFAULT 0,
	   MAX_LEN				INT NULL,	   
		/* UPPER OR LOWER 		*/
	   TEXT_CASE					nvarchar(20) NULL,
	   /* STRING, INT, FLOAT 	*/
	   DATA_TYPE			nvarchar(20) NULL,
	   MIN_VALUE			NUMERIC NULL,
		MAX_VALUE			NUMERIC NULL,
		DEFAULT_VALUE		nvarchar(100) NULL,
		VALUE_LIST			nvarchar(1000),
		LABEL				nvarchar(100),
		MULTI_VALUE			INT NULL,
		AUDITABLE			INT NULL,
		REQUIRED			INT NULL,
		/* DETERMINES IF THE USER WHO OWN A RECORD AND SEE AND EDIT THEIR RECORD */
		SELF_EDITABLE		INT NULL,
		SELF_VIEWABLE		INT NULL,
		/* TEXT, PASSWORD, TEXT_AREA, CHECKBOX, SELECT, LIST */
		UI_TYPE				nvarchar(20) NULL,
		UI_OBJECT_SIZE		nvarchar(40) NULL,
		VALUE_SRC			nvarchar(1000) NULL,
       PRIMARY KEY (METADATA_ID), 
	 CONSTRAINT FK_METADATA_ELEMENT_METADATA_TYPE     
	 FOREIGN KEY (TYPE_ID)
                 REFERENCES METADATA_TYPE(TYPE_ID)
);





CREATE TABLE COMPANY (
       COMPANY_ID           nvarchar(32) NOT NULL,
       COMPANY_NAME         nvarchar(200) NULL,
       LST_UPDATE           datetime NULL,
       LST_UPDATED_BY		nvarchar(40) NULL,
       PARENT_ID            nvarchar(32) NULL,
       STATUS			    nvarchar(20) NULL,
       TYPE_ID              nvarchar(20) NULL,
	   CREATE_DATE			datetime NULL,
	   CREATED_BY			nvarchar(40) NULL,
	   ALIAS				nvarchar(200) NULL,
	   DESCRIPTION			nvarchar(200) NULL,
	   DOMAIN_NAME			nvarchar(40) NULL,
	   LDAP_STR				nvarchar(255) NULL,	
	   CLASSIFICATION		nvarchar(40) NULL,	
	   INTERNAL_COMPANY_ID	nvarchar(200) NULL,
	   ABBREVIATION 		nvarchar(20) NULL,
       SYMBOL				nvarchar(10) NULL,
 	   PRIMARY KEY (COMPANY_ID), 
	CONSTRAINT FK_COMPANY_METADATA_TYPE
       FOREIGN KEY (TYPE_ID)
                             REFERENCES METADATA_TYPE(TYPE_ID)
);



CREATE TABLE COMPANY_ATTRIBUTE (
       COMPANY_ATTR_ID      nvarchar(32) NOT NULL,
       COMPANY_ID           nvarchar(32) NULL,
       NAME                 nvarchar(20) NULL,
       VALUE                nvarchar(255) NULL,
       METADATA_ID          nvarchar(20) NULL,
       PRIMARY KEY (COMPANY_ATTR_ID), 
	CONSTRAINT FK_COMPANY_ATTRIBUTE_COMPANY
       FOREIGN KEY (COMPANY_ID)
                             REFERENCES COMPANY(COMPANY_ID),
	CONSTRAINT FK_COMPANY_METADATA_ELEMENT
	 
       FOREIGN KEY (METADATA_ID)
                             REFERENCES METADATA_ELEMENT(METADATA_ID)
);

CREATE TABLE USERS(
       USER_ID              nvarchar(32) NOT NULL,
       FIRST_NAME           nvarchar(50) NULL,
       LAST_NAME            nvarchar(50) NULL,
       MIDDLE_INIT          nvarchar(50) NULL,
       TYPE_ID              nvarchar(20) NULL,
	   CLASSIFICATION		nvarchar(20) NULL,
       TITLE                nvarchar(100) NULL,
	   DEPT_CD				nvarchar(50) NULL, /* Dept - can POINT TO AN OU in a directory */
	   DEPT_NAME			nvarchar(100) NULL,
	   MAIL_CODE			nvarchar(50) NULL,	   
       DIVISION				nvarchar(50) NULL,
		COST_CENTER			nvarchar(20) NULL,
	    STATUS              nvarchar(40) NULL,
	   SECONDARY_STATUS		nvarchar(40) NULL,
       BIRTHDATE            datetime NULL,
       SEX                  char(1) NULL,
	   CREATE_DATE			DATEtime NULL,
 	   CREATED_BY			nvarchar(40) NULL,
	   LAST_UPDATE			DATETIME NULL,
	   LAST_UPDATED_BY		nvarchar(40) NULL,
       PREFIX               nvarchar(4) NULL,
       SUFFIX               nvarchar(20) NULL,
       USER_TYPE_IND		nvarchar(20) NULL,
       EMPLOYEE_ID			nvarchar(32) NULL,
       EMPLOYEE_TYPE		nvarchar(20) NULL,	/* TEMP, FULL-TIME, INTERN, PART-TIME, CONTRACTOR,  */			
       LOCATION_CD			nvarchar(50) NULL,
	   LOCATION_NAME		nvarchar(100) NULL,
       COMPANY_ID        	nvarchar(32) NULL,
       COMPANY_OWNER_ID		nvarchar(32) NULL,
       JOB_CODE				nvarchar(50) NULL,
       MANAGER_ID			nvarchar(32) NULL,
	   ALTERNATE_ID			nvarchar(32) NULL,  /* ALTERNATE PERSON TO CONTACT WHEN THIS USER IS NOT AVAILABLE. */
	   START_DATE			DATE NULL,
       LAST_DATE			DATE NULL,
 	   MAIDEN_NAME 			nvarchar(40) NULL,
 	   NICKNAME 			nvarchar(40) NULL,
	   PASSWORD_THEME		nvarchar(20) NULL,
	   COUNTRY              nvarchar(30) NULL,
	   BLDG_NUM				nvarchar(10) NULL,
	   STREET_DIRECTION		nvarchar(20) NULL,
	   SUITE				nvarchar(20) NULL,
       ADDRESS1             nvarchar(45) NULL,
       ADDRESS2             nvarchar(45) NULL,
	   ADDRESS3             nvarchar(45) NULL,
	   ADDRESS4             nvarchar(45) NULL,
	   ADDRESS5             nvarchar(45) NULL,
	   ADDRESS6             nvarchar(45) NULL,
	   ADDRESS7             nvarchar(45) NULL,
       CITY                 nvarchar(30) NULL,
       STATE                nvarchar(15) NULL,
       POSTAL_CD            nvarchar(10) NULL,
	   EMAIL_ADDRESS        nvarchar(320) NULL,
	   AREA_CD              nvarchar(10) NULL,
       COUNTRY_CD           nvarchar(3) NULL,
       PHONE_NBR            nvarchar(50) NULL,
       PHONE_EXT			nvarchar(20) NULL,
	   SHOW_IN_SEARCH		INT NULL,
	   DEL_ADMIN			INT NULL,
	   USER_OWNER_ID				nvarchar(32) NULL,
	   DATE_PASSWORD_CHANGED		DATETIME NULL,
	   DATE_CHALLENGE_RESP_CHANGED	DATETIME NULL,
       PRIMARY KEY (USER_ID), 
	CONSTRAINT FK_USERS_METADATA_TYPE
         FOREIGN KEY (TYPE_ID)
                             	REFERENCES METADATA_TYPE(TYPE_ID)
);





CREATE TABLE ORG_STRUCTURE (
	ORG_STRUCTURE_ID	nvarchar(32) NOT NULL,
	SUPERVISOR_ID		nvarchar(32) NOT NULL,
	STAFF_ID			nvarchar(32) NOT NULL,
	SUPERVISOR_TYPE		nvarchar(20) NULL,
    IS_PRIMARY_SUPER	INT NULL DEFAULT 0,
	START_DATE			DATETIME NULL,
	END_DATE			DATETIME NULL,
	STATUS				nvarchar(20) NULL,
    COMMENTS			nvarchar(255) NULL,
    PRIMARY KEY (ORG_STRUCTURE_ID),
	CONSTRAINT FK_SUPR_USER
         FOREIGN KEY (SUPERVISOR_ID)
                             	REFERENCES USERS(USER_ID),
	CONSTRAINT FK_STAFF_USER
         FOREIGN KEY (STAFF_ID)
                             	REFERENCES USERS(USER_ID)
);


CREATE TABLE USER_NOTE(
       USER_NOTE_ID   nvarchar(32) NOT NULL,
       USER_ID              nvarchar(32) NULL,
       NOTE_TYPE			nvarchar(20) NULL,
       DESCRIPTION			nvarchar(2000) NULL,
       CREATE_DATE			DATETIME NULL,
       CREATED_BY			nvarchar(20) NULL,
       PRIMARY KEY (USER_NOTE_ID), 
	CONSTRAINT FK_USER_NOTE_USERS
       FOREIGN KEY (USER_ID)
                             REFERENCES USERS(USER_ID)
);

CREATE TABLE USER_ATTACHMENT_REF (
       USER_ATTACH_REF_ID   nvarchar(20) NOT NULL,
       USER_ID              nvarchar(32) NULL,
       NAME                 nvarchar(20) NULL,
       VALUE                nvarchar(50) NULL,
       URL                  nvarchar(50) NULL,
       PRIMARY KEY (USER_ATTACH_REF_ID), 
	CONSTRAINT FK_USER_ATTACHMENT_REF_USERS
       FOREIGN KEY (USER_ID)
                             REFERENCES USERS(USER_ID)
);


CREATE TABLE USER_ATTRIBUTES (
       ID                   nvarchar(32) NOT NULL,
       USER_ID              nvarchar(32) NULL,
       METADATA_ID          nvarchar(20) NULL,
       NAME                 nvarchar(50) NULL,
       VALUE                nvarchar(1000) NULL,
	   ATTR_GROUP			nvarchar(20) NULL,
       PRIMARY KEY (ID),
	CONSTRAINT FK_USER_ATTRIBUTES_METADATA_ELEMENT
       FOREIGN KEY (METADATA_ID)
                             REFERENCES METADATA_ELEMENT(METADATA_ID), 
       FOREIGN KEY (USER_ID)
                             REFERENCES USERS(USER_ID)
);

CREATE TABLE USER_DELEGATION_ATTRIBUTE (
       ID                   nvarchar(32) NOT NULL,
       USER_ID              nvarchar(32) NULL,
       OBJ_TYPE          	nvarchar(50) NULL,	/*USER, ORG, ETC */
       NAME                 nvarchar(50) NULL,
       VALUE                nvarchar(255) NULL,
       PRIMARY KEY (ID),
       FOREIGN KEY (USER_ID)
                             REFERENCES USERS(USER_ID)
);





CREATE TABLE SERVICE (
       SERVICE_ID           nvarchar(20) NOT NULL,
       SERVICE_NAME         nvarchar(40) NULL,
       STATUS				nvarchar(20) NULL,
       LOCATION_IP_ADDRESS	nvarchar(80) NULL,
       COMPANY_OWNER_ID		nvarchar(20) NULL,
       START_DATE			DATETIME NULL,
       END_DATE				DATETIME NULL,
       LICENSE_KEY			nvarchar(255) NULL,	
       SERVICE_TYPE			nvarchar(20) NULL,
       PARENT_SERVICE_ID	nvarchar(20) NULL,
       ROOT_RESOURCE_ID     nvarchar(20) NULL,
       ACCESS_CONTROL_MODEL nvarchar(20) NULL,
	   	PARAM1		nvarchar(20) NULL,
	   	PARAM2		nvarchar(20) NULL,
	   	PARAM3		nvarchar(20) NULL,
	   	PARAM4		nvarchar(20) NULL,
	   	PARAM5		nvarchar(20) NULL,
       PRIMARY KEY (SERVICE_ID)
);

CREATE TABLE SERVICE_CONFIG (
       SERVICE_CONFIG_ID    nvarchar(20) NOT NULL,
       SERVICE_ID           nvarchar(20) NULL,
       NAME                 nvarchar(40) NULL,
       VALUE                nvarchar(40) NULL,
       PRIMARY KEY (SERVICE_CONFIG_ID),
       CONSTRAINT FK_SRV_SRV_CONF
       FOREIGN KEY (SERVICE_ID)
                             REFERENCES SERVICE(SERVICE_ID)
);


CREATE TABLE STATUS (
       STATUS_CD           	nvarchar(40) NOT NULL,
	   STATUS_TYPE			nvarchar(20) NULL,
       DESCRIPTION          nvarchar(80) NULL,
       CODE_GROUP			nvarchar(40) NOT NULL,
       LANGUAGE_CD			nvarchar(2) NOT NULL,
	   COMPANY_OWNER_ID		nvarchar(32) NOT NULL,
	   SERVICE_ID			nvarchar(20) NOT NULL,
       WEIGHT				INT	NULL,
       PRIMARY KEY (CODE_GROUP, STATUS_CD, LANGUAGE_CD)
);


CREATE TABLE MENU (
       MENU_ID              nvarchar(20) NOT NULL,
       LANGUAGE_CD          nvarchar(5) NOT NULL DEFAULT 'en',
       MENU_GROUP           nvarchar(20) NULL,
       MENU_NAME            nvarchar(40) NULL,
       MENU_DESC            nvarchar(40) NULL,
       URL                  nvarchar(100) NULL,
       ACTIVE               INT NULL,
       DISPLAY_ORDER        INT NULL,
	   PUBLIC_URL				INT NULL,
       PRIMARY KEY (MENU_ID, LANGUAGE_CD)
);


CREATE TABLE GRP (
       GRP_ID               nvarchar(32) NOT NULL,
       GRP_NAME             nvarchar(80) NULL,
       CREATE_DATE			DATETIME NULL,
       CREATED_BY			nvarchar(20) NULL,
       COMPANY_ID			nvarchar(32) NULL,
	   OWNER_ID				nvarchar(32) NULL,
       PARENT_GRP_ID		nvarchar(32) NULL,
       INHERIT_FROM_PARENT	INT NULL DEFAULT 0,
       PROVISION_METHOD		nvarchar(20) NULL,
       PROVISION_OBJ_NAME	nvarchar(80) NULL,
       TYPE_ID              nvarchar(20) NULL,
	   GROUP_CLASS			nvarchar(40) NULL,
	   GROUP_DESC			nvarchar(80) NULL,
   	   STATUS				nvarchar(20) NULL,
	   LAST_UPDATE			DATETIME NULL,
	   LAST_UPDATED_BY		nvarchar(32) NULL,
	   POLICY_ID			nvarchar(20) NULL,
	   INTERNAL_GROUP_ID	nvarchar(32) NULL,
	   EXTERNAL_GRP_NAME	nvarchar(200) NULL,
	   GROUP_APPROVER_ID	nvarchar(32) NULL,
       PRIMARY KEY (GRP_ID),
       CONSTRAINT FK_GRP_METADATA_TYPE
         FOREIGN KEY (TYPE_ID)
                             	REFERENCES METADATA_TYPE(TYPE_ID)
       
);


CREATE TABLE GRP_ATTRIBUTES (
       ID                   nvarchar(32) NOT NULL,
       GRP_ID               nvarchar(32) NULL,
       METADATA_ID          nvarchar(20) NULL,
       NAME                 nvarchar(20) NULL,
       VALUE                nvarchar(255) NULL,
       PRIMARY KEY (ID),
	CONSTRAINT FK_GRP_ATTRIBUTES_METADATA_ELEMENT
       FOREIGN KEY (METADATA_ID)
                             REFERENCES METADATA_ELEMENT(METADATA_ID), 
       FOREIGN KEY (GRP_ID)
                             REFERENCES GRP(GRP_ID)
);


CREATE TABLE USER_GRP(
	USER_GRP_ID			nvarchar(32) NOT NULL,
   	GRP_ID              nvarchar(32) NOT NULL,
   	USER_ID             nvarchar(32) NOT NULL,
	STATUS				nvarchar(20) NULL DEFAULT 'ACTIVE',  
    CREATE_DATE			DATETIME NULL,
    CREATED_BY			nvarchar(32) NULL,
  	START_DATE			DATETIME NULL,
   	END_DATE			DATETIME NULL,
	PRIMARY KEY (USER_GRP_ID),
       CONSTRAINT FK_USR_GRP_GPR
       FOREIGN KEY (GRP_ID)  REFERENCES GRP(GRP_ID),
       CONSTRAINT FK_USR_GRP_USR
       FOREIGN KEY (USER_ID)  REFERENCES USERS(USER_ID)
);


CREATE TABLE PERMISSIONS (
       MENU_ID              nvarchar(20) NOT NULL,
	   SERVICE_ID           nvarchar(20) NOT NULL,
       ROLE_ID              nvarchar(32) NOT NULL,
       PRIMARY KEY (SERVICE_ID, ROLE_ID, MENU_ID)
);


CREATE TABLE SEQUENCE_GEN (
       ATTRIBUTE            nvarchar(32) NOT NULL,
       NEXT_ID              int NULL,
       PRIMARY KEY (ATTRIBUTE)
);

CREATE TABLE RELATION_SET (
       RELATION_SET_ID      nvarchar(20) NOT NULL,
       DESCRIPTION          nvarchar(80) NULL,
       PRIMARY KEY (RELATION_SET_ID)
);

CREATE TABLE RELATION_CATEGORY (
       RELATION_SET_ID      nvarchar(20) NOT NULL,
       CATEGORY_ID          nvarchar(20) NOT NULL,
       PRIMARY KEY (RELATION_SET_ID, CATEGORY_ID), 
	CONSTRAINT FK_RELATION_CATEGORY_CATEGORY
       FOREIGN KEY (CATEGORY_ID) REFERENCES CATEGORY(CATEGORY_ID),
	CONSTRAINT FK_RELATION_CATEGORY_RELATION_SET
       FOREIGN KEY (RELATION_SET_ID) REFERENCES RELATION_SET(RELATION_SET_ID)
);


CREATE TABLE RELATIONSHIP (
       RELATIONSHIP_ID      nvarchar(20) NOT NULL,
       RELATION_SET_ID      nvarchar(20) NULL,
       ITEM_OBJ             nvarchar(20) NULL,
       ITEM_ID              nvarchar(20) NULL,
       RANK                 int NULL,
       ACTIVE               bit NULL,
       PRIMARY KEY (RELATIONSHIP_ID), 
	CONSTRAINT FK_RELATIONSHIP_RELATION_SET
       FOREIGN KEY (RELATION_SET_ID)
                             REFERENCES RELATION_SET(RELATION_SET_ID)
);


CREATE TABLE RELATION_TYPE (
       RELATION_TYPE_ID     nvarchar(20) NOT NULL,
       DESCRIPTION          nvarchar(80) NOT NULL,
       PRIMARY KEY (RELATION_TYPE_ID)
);


CREATE TABLE RELATION_SOURCE (
       RELATION_TYPE_ID     nvarchar(20) NOT NULL,
       SOURCE_OBJ           nvarchar(20) NOT NULL,
       SOURCE_ID            nvarchar(20) NOT NULL,
       RELATION_SET_ID      nvarchar(20) NULL,
       PRIMARY KEY (RELATION_TYPE_ID, SOURCE_OBJ, SOURCE_ID), 
       CONSTRAINT FK_RELATION_SOURCE_RELATION_SET
       FOREIGN KEY (RELATION_SET_ID)
                             REFERENCES RELATION_SET (RELATION_SET_ID), 
	CONSTRAINT FK_RELATION_SOURCE_RELATION_TYPE
       FOREIGN KEY (RELATION_TYPE_ID)
                             REFERENCES RELATION_TYPE(RELATION_TYPE_ID)
);

CREATE TABLE CATEGORY_TYPE (
       CATEGORY_ID          nvarchar(20) NOT NULL,
       TYPE_ID              nvarchar(20) NOT NULL,
       PRIMARY KEY (TYPE_ID, CATEGORY_ID), 
	CONSTRAINT FK_CATEGORY_TYPE_CATEGORY
       FOREIGN KEY (CATEGORY_ID)
                             REFERENCES CATEGORY(CATEGORY_ID), 
	CONSTRAINT FK_CATEGORY_METADATA_TYPE
       FOREIGN KEY (TYPE_ID)
                             REFERENCES METADATA_TYPE(TYPE_ID)
);


CREATE TABLE IMAGE (
       IMAGE_ID             nvarchar(20) NOT NULL,
       IMAGE_FILE           nvarchar(80) NULL,
       IMAGE_TYPE           nvarchar(20) NULL,
       DESCRIPTION          nvarchar(250) NULL,
       MIME_TYPE            nvarchar(30) NULL,
       URL                  nvarchar(100) NULL,
       FILE_SIZE            int NULL,
       IMAGE_BLOB           IMAGE NULL,
       PRIMARY KEY (IMAGE_ID)
);

CREATE TABLE LOCATION (
       LOCATION_ID           nvarchar(32) NOT NULL,
	   NAME					nvarchar(80) NULL,
	   DESCRIPTION			nvarchar(255) NULL,
       COUNTRY              nvarchar(30) NULL,
	   BLDG_NUM				nvarchar(10) NULL,
	   STREET_DIRECTION		nvarchar(20) NULL,
       ADDRESS1             nvarchar(45) NULL,
       ADDRESS2             nvarchar(45) NULL,
	   ADDRESS3             nvarchar(45) NULL,
       CITY                 nvarchar(30) NULL,
       STATE                nvarchar(15) NULL,
       POSTAL_CD            nvarchar(10) NULL,
	   ORGANIZATION_ID		nvarchar(32) NULL,
	   INTERNAL_LOCATION_ID	nvarchar(32) NULL,
       ACTIVE				INT NULL DEFAULT 1,
       PRIMARY KEY (LOCATION_ID)
);

CREATE TABLE ADDRESS (
       ADDRESS_ID           nvarchar(32) NOT NULL,
	   NAME					nvarchar(40) NULL,
       COUNTRY              nvarchar(30) NULL,
	   BLDG_NUM				nvarchar(10) NULL,
	   STREET_DIRECTION		nvarchar(20) NULL,
	   SUITE				nvarchar(20) NULL,
       ADDRESS1             nvarchar(45) NULL,
       ADDRESS2             nvarchar(45) NULL,
	   ADDRESS3             nvarchar(45) NULL,
	   ADDRESS4             nvarchar(45) NULL,
	   ADDRESS5             nvarchar(45) NULL,
	   ADDRESS6             nvarchar(45) NULL,
	   ADDRESS7             nvarchar(45) NULL,
       CITY                 nvarchar(30) NULL,
       STATE                nvarchar(15) NULL,
       POSTAL_CD            nvarchar(10) NULL,
       IS_DEFAULT           int NULL DEFAULT 0,
       DESCRIPTION          nvarchar(100) NULL,
       ACTIVE				INT NULL DEFAULT 1,
       PARENT_ID            nvarchar(32) NULL,
       PARENT_TYPE          nvarchar(30) NULL,
       PRIMARY KEY (ADDRESS_ID)
);




CREATE TABLE EMAIL_ADDRESS (
       EMAIL_ID             nvarchar(32) NOT NULL,
	   NAME					nvarchar(40) NULL,
       DESCRIPTION          nvarchar(100) NULL,
       EMAIL_ADDRESS        nvarchar(320) NULL,
       IS_DEFAULT           int NULL,
       ACTIVE				INT NULL DEFAULT 1,
       PARENT_ID            nvarchar(32) NULL,
       PARENT_TYPE          nvarchar(30) NULL,
       PRIMARY KEY (EMAIL_ID)
);

CREATE TABLE PHONE (
       PHONE_ID             nvarchar(32) NOT NULL,
	   NAME					nvarchar(40) NULL,
       AREA_CD              nvarchar(10) NULL,
       COUNTRY_CD           nvarchar(3) NULL,
       DESCRIPTION          nvarchar(100) NULL,
       PHONE_NBR            nvarchar(50) NULL,
       PHONE_EXT			nvarchar(20) NULL,
       IS_DEFAULT           int NULL,
       ACTIVE				INT NULL DEFAULT 1,
       PARENT_ID            nvarchar(32) NULL,
       PARENT_TYPE          nvarchar(30) NULL,
	   PHONE_TYPE			nvarchar(20) NULL,
       PRIMARY KEY (PHONE_ID)
);

CREATE TABLE ROLE(
       ROLE_ID              nvarchar(32) NOT NULL,
       SERVICE_ID           nvarchar(20) NOT NULL,
       ROLE_NAME            nvarchar(80) NULL,
       CREATE_DATE			DATETIME NULL,
       CREATED_BY			nvarchar(32) NULL,
       DESCRIPTION          nvarchar(255) NULL,
       PROVISION_OBJ_NAME   nvarchar(80) NULL,
	   PARENT_ROLE_ID		nvarchar(32) NULL,
	   TYPE_ID				nvarchar(20) NULL,
	   INHERIT_FROM_PARENT	INT NULL,
	   OWNER_ID				nvarchar(32) NULL,
       STATUS				nvarchar(20) NULL,
	   INTERNAL_ROLE_ID		nvarchar(32) NULL,
	   ROLE_APPROVER_ID		nvarchar(32) NULL,
	   ROLE_END_DATE			DATETIME NULL,
      	 PRIMARY KEY (ROLE_ID, SERVICE_ID), 
	CONSTRAINT FK_ROLE_SERVICE
       FOREIGN KEY (SERVICE_ID) REFERENCES SECURITY_DOMAIN(DOMAIN_ID)
);



CREATE TABLE ROLE_ATTRIBUTE (
	   ROLE_ATTR_ID			nvarchar(32) NOT NULL,
       NAME                 nvarchar(40) NULL,
       VALUE                nvarchar(255) NULL,
       METADATA_ID          nvarchar(20) NULL,
       ROLE_ID        	 	nvarchar(32) NOT NULL,
	   SERVICE_ID           nvarchar(20) NOT NULL,
	   ATTR_GROUP			nvarchar(20) NULL,
       PRIMARY KEY (ROLE_ATTR_ID),
	CONSTRAINT FK_ROLE_ROLE_ATTRIBUTE
       FOREIGN KEY (ROLE_ID,SERVICE_ID)
                             REFERENCES ROLE(ROLE_ID,SERVICE_ID)
);


CREATE TABLE ROLE_POLICY (
   ROLE_POLICY_ID nvarchar(32) NOT NULL,
   ROLE_ID 				nvarchar(32),
   SERVICE_ID 		nvarchar(20),
   ACTION 				nvarchar(20),
   NAME 					nvarchar(40),
   VALUE1 				nvarchar(40),
   VALUE2 				nvarchar(40),
   ACTION_QUALIFIER		nvarchar(20),
   EXECUTION_ORDER 		INT,
   POLICY_SCRIPT 		nvarchar(100),
  FOREIGN KEY (ROLE_ID, SERVICE_ID) 
  	REFERENCES ROLE (ROLE_ID, SERVICE_ID),
  PRIMARY KEY (ROLE_POLICY_ID)
);



CREATE TABLE GRP_ROLE(
	   SERVICE_ID           nvarchar(20) NOT NULL,
	   ROLE_ID              nvarchar(32) NOT NULL,
       GRP_ID               nvarchar(32) NOT NULL,
	PRIMARY KEY (SERVICE_ID, ROLE_ID, GRP_ID),
       CONSTRAINT FK_GRP_ROLE_ROLE
       FOREIGN KEY (ROLE_ID,SERVICE_ID)  REFERENCES ROLE(ROLE_ID,SERVICE_ID),
       CONSTRAINT FK_GRP_ROLE_GRP
       FOREIGN KEY (GRP_ID)  REFERENCES GRP(GRP_ID)
);


CREATE TABLE USER_ROLE (
		USER_ROLE_ID		nvarchar(32) NOT NULL,
	   	SERVICE_ID			nvarchar(20) NOT NULL,
	   	ROLE_ID             nvarchar(32) NOT NULL,
       	USER_ID             nvarchar(32) NOT NULL,
		STATUS				nvarchar(20) NULL,	   
    	CREATE_DATE			DATETIME NULL,
    	START_DATE			DATETIME NULL,
    	END_DATE			DATETIME NULL,
    	CREATED_BY			nvarchar(32) NULL,
	PRIMARY KEY (SERVICE_ID, ROLE_ID, USER_ID),
	   CONSTRAINT FK_USR_ROLE_ROLE
        FOREIGN KEY (ROLE_ID,SERVICE_ID)
                             REFERENCES ROLE(ROLE_ID,SERVICE_ID), 
       CONSTRAINT FK_USR_ROLE_USR
		FOREIGN KEY (USER_ID)
                             REFERENCES USERS(USER_ID)
);


CREATE TABLE USER_AFFILIATION (
		USER_AFFILIATION_ID		nvarchar(32) NOT NULL,
        COMPANY_ID    nvarchar(32) NOT NULL,
        USER_ID       nvarchar(32) NOT NULL,
        STATUS				nvarchar(20) NULL,
        CREATE_DATE		DATETIME NULL,
        START_DATE		DATETIME NULL,
        END_DATE			DATETIME NULL,
        CREATED_BY		nvarchar(32) NULL,
	PRIMARY KEY (USER_AFFILIATION_ID),
	   CONSTRAINT FK_USR_ORG_AFFL FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(COMPANY_ID),
     CONSTRAINT FK_USR_USER_AFFIL FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID)
);

CREATE TABLE RESOURCE_TYPE(
       RESOURCE_TYPE_ID     nvarchar(20) NOT NULL,
       DESCRIPTION          nvarchar(100) NULL,
       METADATA_TYPE_ID     nvarchar(20) NULL,
       PROVISION_RESOURCE   int NULL,
       PROCESS_NAME         nvarchar(80) NULL,
  PRIMARY KEY (RESOURCE_TYPE_ID)
);

CREATE TABLE RES(
       RESOURCE_ID          nvarchar(32) NOT NULL,
       RESOURCE_TYPE_ID     nvarchar(20) NULL,
       DESCRIPTION          nvarchar(100) NULL,
	   	 NAME      			nvarchar(40) NULL,
       RESOURCE_PARENT      nvarchar(32) NULL,
       BRANCH_ID            nvarchar(32) NULL,
       CATEGORY_ID	    	nvarchar(20) NULL,
       DISPLAY_ORDER	    int NULL  DEFAULT 1,
       NODE_LEVEL	    	int NULL  DEFAULT 1,
       SENSITIVE_APP		int null  DEFAULT 0,
 	   MANAGED_SYS_ID		nvarchar(32) NULL,
	   URL					nvarchar(255) NULL,
	   RES_OWNER_GROUP_ID   nvarchar(32) NULL,
	   RES_OWNER_USER_ID    nvarchar(32) NULL,
       PRIMARY KEY (RESOURCE_ID), 
	CONSTRAINT FK_RESOURCE_RESOURCE_TYPE
       FOREIGN KEY (RESOURCE_TYPE_ID)  REFERENCES RESOURCE_TYPE(RESOURCE_TYPE_ID),
	CONSTRAINT FK_RESOURCE_CATEGORY
       FOREIGN KEY (CATEGORY_ID) REFERENCES CATEGORY(CATEGORY_ID)
);


CREATE TABLE RESOURCE_PROP (
       RESOURCE_PROP_ID     nvarchar(32) NOT NULL,
       RESOURCE_ID          nvarchar(32) NULL,
       METADATA_ID          nvarchar(20) NULL,
	     NAME					nvarchar(40) NOT NULL,
       PROP_VALUE           nvarchar(500) NULL,
       PROP_GROUP           nvarchar(20) NULL,
       PRIMARY KEY (RESOURCE_PROP_ID), 
	CONSTRAINT FK_RESOURCE_PROP_RESOURCE
       FOREIGN KEY (RESOURCE_ID) REFERENCES RES(RESOURCE_ID)
);

CREATE TABLE PRIVILEGE(
       PRIVILEGE_ID         nvarchar(32) NOT NULL,
	   RESOURCE_ID			nvarchar(32) NOT NULL,
       ABBRV				nvarchar(3) NOT NULL,
       DESCRIPTION          nvarchar(40) NULL,
       PRIMARY KEY (PRIVILEGE_ID)
);

CREATE TABLE RESOURCE_USER(
	   RESOURCE_USER_ID    	nvarchar(32) NOT NULL,
       RESOURCE_ID          nvarchar(32) NOT NULL,
       USER_ID              nvarchar(32) NOT NULL,
       PRIVILEGE_LIST       nvarchar(100) NULL,
	   OP_AGAINST_ROLE		INT NULL,
       STATUS				nvarchar(20) NULL,
	   START_DATE			DATETIME NULL,
       END_DATE				DATETIME NULL,
       PRIMARY KEY (RESOURCE_USER_ID), 
	CONSTRAINT FK_RESOURCE_USER_USERS
         FOREIGN KEY (USER_ID)    REFERENCES USERS(USER_ID), 
       FOREIGN KEY (RESOURCE_ID)  REFERENCES RES(RESOURCE_ID)
);

CREATE TABLE RESOURCE_ROLE(
       RESOURCE_ID          nvarchar(32) NOT NULL,
	   SERVICE_ID           nvarchar(20) NOT NULL,
       ROLE_ID              nvarchar(32) NOT NULL,
       PRIVILEGE_ID         nvarchar(100) NOT NULL DEFAULT 'NA',
	   START_DATE			DATETIME NULL,
       END_DATE				DATETIME NULL,
       PERMIT               smallint    NULL DEFAULT 0,
       PRIMARY KEY (RESOURCE_ID, SERVICE_ID, ROLE_ID, PRIVILEGE_ID), 
	CONSTRAINT FK_RESOURCE_ROLE_ROLE
       FOREIGN KEY (ROLE_ID, SERVICE_ID)
                             REFERENCES ROLE(ROLE_ID, SERVICE_ID), 
	CONSTRAINT FK_RESOURCE_ROLE_RESOURCE
       FOREIGN KEY (RESOURCE_ID)
                             REFERENCES RES(RESOURCE_ID)
);


CREATE TABLE RESOURCE_GROUP(
       RES_GROUP_ID         nvarchar(32) NOT NULL,
       RESOURCE_ID          nvarchar(32) NOT NULL,
       GRP_ID               nvarchar(32) NOT NULL,
	   START_DATE			DATETIME NULL,
       END_DATE				DATETIME NULL,
       PRIMARY KEY (RES_GROUP_ID),
	CONSTRAINT FK_RESOURCE_GRP
       FOREIGN KEY (GRP_ID)
                             REFERENCES GRP(GRP_ID),
	CONSTRAINT FK_RESOURCE_GRP_RESOURCE
       FOREIGN KEY (RESOURCE_ID)
                             REFERENCES RES(RESOURCE_ID)
);


CREATE TABLE RESOURCE_COMPANY(
       RESOURCE_ID          nvarchar(32) NOT NULL,
       COMPANY_ID           nvarchar(32) NOT NULL,
	   STATUS				nvarchar(20) NULL,
	   START_DATE			DATETIME NULL,
	   LAST_DATE 			DATETIME NULL,
       PRIMARY KEY (RESOURCE_ID, COMPANY_ID), 
       CONSTRAINT FK_RESOURCE_COMPANY
       FOREIGN KEY (COMPANY_ID)
				 REFERENCES COMPANY(COMPANY_ID)
);


CREATE TABLE LOGIN(
       SERVICE_ID           nvarchar(20) NOT NULL,
       LOGIN                nvarchar(320)NOT NULL, /* Cannot be utf8 because of length, see http://bugs.mysql.com/bug.php?id=4541 */
	   MANAGED_SYS_ID 		nvarchar(32) NOT NULL,
	   IDENTITY_TYPE		nvarchar(20) NULL,			/* SSO, PROVISION, BOTH */
	   CANONICAL_NAME		nvarchar(100) NULL,
       USER_ID              nvarchar(32) NULL,
       PASSWORD             nvarchar(80) NULL,
       PWD_EQUIVALENT_TOKEN nvarchar(80) NULL,
       PWD_CHANGED          datetime NULL,				/* last time the password was changed */
       PWD_EXP              datetime NULL,				/* date when the pswd expires */
       RESET_PWD	    	integer NOT NULL DEFAULT 0,		/* 1 - PASSWORD HAS BEEN RESET. FIRST TIME LOGIN*/
       FIRST_TIME_LOGIN	   	integer NOT NULL DEFAULT 1,		/* 1 - FIRST TIME LOGIN, 0 - USER HAS LOGGED IN AND CHANGED PSWD */
	   IS_LOCKED	        integer NOT NULL DEFAULT 0,		/* IS THIS IDENTITY LOCKED  */
       STATUS		    	nvarchar(20) NULL,			/* STATUS OF THIS IDENTITY  */
       GRACE_PERIOD			DATETIME NULL, 				/* WHEN THE GRACE_PRD_EXP 	*/	
       CREATE_DATE			DATETIME NULL,
       CREATED_BY			nvarchar(32) NULL,
       CURRENT_LOGIN_HOST	nvarchar(40) NULL,		/* HOST FROM WHICH YOU ARE LOGGED IN*/	
       AUTH_FAIL_COUNT		INTEGER NOT NULL DEFAULT 0,				/* NUMBER OF TIMES AUTH. HAS FAILED */
       LAST_AUTH_ATTEMPT	DATETIME NULL,	    	/* WHEN WAS THE LAST AUTH. ATTEMPT  */   
	   LAST_LOGIN			DATETIME NULL,			/* LAST TIME THE PERSON SUCCESSFULLY LOGGED IN */
	   LAST_LOGIN_IP        nvarchar(60) NULL,
	   PREV_LOGIN           DATETIME NULL,
	   PREV_LOGIN_IP        nvarchar(60) NULL,
	   IS_DEFAULT			INTEGER NULL DEFAULT 0,
	    PWD_CHANGE_COUNT	INTEGER DEFAULT 0 NULL,  /* Indicates the number of times that a password has changed in a day */
       PRIMARY KEY (SERVICE_ID, LOGIN,MANAGED_SYS_ID), 
	CONSTRAINT FK_LOGIN_USERS
       FOREIGN KEY (USER_ID)
                             REFERENCES USERS(USER_ID), 
       CONSTRAINT FK_LOGIN_SERVICE
       FOREIGN KEY (SERVICE_ID)
                             REFERENCES SECURITY_DOMAIN(DOMAIN_ID)
);

CREATE TABLE LOGIN_ATTRIBUTE (
	   LOGIN_ATTR_ID		nvarchar(32) NOT NULL,
       LOGIN                nvarchar(320) NOT NULL, /* Must be latin1. See LOGIN.LOGIN */
       SERVICE_ID	    	nvarchar(20) NOT NULL,
	   MANAGED_SYS_ID       nvarchar(32) NOT NULL,
       NAME                 nvarchar(20) NULL,
       VALUE                nvarchar(255) NULL,
       METADATA_ID          nvarchar(20) NULL,
	   ATTR_GROUP			nvarchar(20) NULL,
       PRIMARY KEY (LOGIN_ATTR_ID), 
	CONSTRAINT FK_LOGIN_LOGIN_ATTRIBUTE
       FOREIGN KEY (SERVICE_ID, LOGIN, MANAGED_SYS_ID)
                             REFERENCES LOGIN(SERVICE_ID, LOGIN, MANAGED_SYS_ID)
);

CREATE TABLE AUTH_STATE (
       USER_ID              nvarchar(32) NOT NULL,
       AUTH_STATE           numeric(5,1) NULL,
       TOKEN                nvarchar(2000) NULL,
       AA                   nvarchar(20) NULL,
       EXPIRATION           numeric(18,0) NULL,
       LAST_LOGIN	    datetime NULL,
       IP_ADDRESS	    nvarchar(20) NULL,
       PRIMARY KEY (USER_ID), 
	CONSTRAINT FK_AUTH_STATE_USERS FOREIGN KEY (USER_ID) REFERENCES USERS (USER_ID)
);



CREATE TABLE PWD_HISTORY (
	   PWD_HISTORY_ID		nvarchar(32) NOT NULL,
       LOGIN                nvarchar(320) NOT NULL, /* Must be latin1. See LOGIN.LOGIN */
       SERVICE_ID	    	nvarchar(20) NOT NULL,
	   MANAGED_SYS_ID		nvarchar(32) NOT NULL,
       DATE_CREATED         datetime NOT NULL,
       PASSWORD             nvarchar(80) NULL,
       PRIMARY KEY (PWD_HISTORY_ID)
);


CREATE TABLE CREDENTIALS (
       USER_ID              nvarchar(32) NOT NULL,
       CREDENTIAL_TYPE      nvarchar(20) NOT NULL,
       VALUE                nvarchar(100) NULL,
       IS_PUBLIC            bit,
       PRIMARY KEY (USER_ID, CREDENTIAL_TYPE), 
	CONSTRAINT FK_CREDENTIALS_USERS
       FOREIGN KEY (USER_ID)
                             REFERENCES USERS(USER_ID)
);

CREATE TABLE IDM_AUDIT_LOG (
      LOG_ID        	nvarchar(32) NOT NULL,
      OBJECT_TYPE_ID    nvarchar(20) NOT NULL,            /* TYPE OF OBJECT THAT IS BEING AUDITED */
	  OBJECT_ID    		nvarchar(32) NULL,                /* ID OF THE OBJECT THAT IS BEING AUDITED */ 
	  ACTION_ID         nvarchar(50) NOT NULL,            /* TYPE OF ACTION                              */
      ACTION_STATUS     nvarchar(100) NULL,                /* STATUS OF THE ACTION                               */                                 
      REASON            nvarchar(1000) NULL,                /* REASON WHY THE STATUS IS WHAT IT IS. IF AUTH_FAILS, THEN WHY */
      REASON_DETAIL    	ntext NULL,                       /* INCASE WE WANT TO CAPTURE SOME OUTPUT LIKE AN EXCEPTION LOG  */
      ACTION_DATETIME 	DATETIME2 NULL,               /* DATE AND TIME THIS EVENT OCCURED.                                          */
      ACTION_END_DATETIME 	DATETIME2 NULL,               /* DATE AND TIME THIS EVENT OCCURED.                                          */
      OBJECT_NAME      	nvarchar(255) NULL,               /* OBJECT THAT WAS ACTED UPON                                                     */
      RESOURCE_NAME 	nvarchar(255) NULL,               /* RESOURCE INVOLVED IN THIS TASK                                                */
      USER_ID              nvarchar(32) NULL,
      SERVICE_ID           nvarchar(20) NULL,
      LOGIN_ID             nvarchar(320) NULL, /* Must be latin1. See LOGIN.LOGIN */
      HOST                 nvarchar(100) NULL,              /* HOST WHERE THE EVENT OCCURRED */
      NODE_IP              nvarchar(60) NULL,              /* NODE THAT SENT THE REQUEST TO THE IDM SERVER */
      CLIENT_ID            nvarchar(20) NULL,              /* CLIENT, AGENT OR INTERFACE WHERE THIS EVENT OCCURRED      */
      REQ_URL              nvarchar(255) NULL,
      LINKED_LOG_ID		 	nvarchar(40) NULL,			  /* INDICATES IF THIS EVENT IS LINKED TO ANOTHER EVENT 		*/
	  LINK_SEQUENCE			INT NULL,
	  ORIG_OBJECT_STATE   	ntext NULL,                     /* SNAP SHOT OF THE ORIGINAL OBJECT                 */
      NEW_OBJECT_STATE		ntext NULL,
	  LOG_HASH   			nvarchar(80) NULL,                     /* VALUES THAT WERE CHANGED DURING THIS OPERATION                 */
	  SRC_SYSTEM_ID 		nvarchar(40) NULL, 
 	  TARGET_SYSTEM_ID 		nvarchar(40) NULL,
	  REQUEST_ID			nvarchar(40) NULL,
      SESSION_ID			nvarchar(40) NULL,
	  CUSTOM_ATTRNAME1          nvarchar(255) NULL,        /* additional NAME fileds that can be used to have their own columns instead of in big blob. */
      CUSTOM_ATTRNAME2          nvarchar(255) NULL,
      CUSTOM_ATTRNAME3          nvarchar(255) NULL,
      CUSTOM_ATTRNAME4          nvarchar(255) NULL,
      CUSTOM_ATTRNAME5          nvarchar(255) NULL,
      CUSTOM_ATTRNAME6          nvarchar(255) NULL,
      CUSTOM_ATTRNAME7          nvarchar(255) NULL,
      CUSTOM_ATTRNAME8          nvarchar(255) NULL,
      CUSTOM_ATTRNAME9          nvarchar(255) NULL,
      CUSTOM_ATTRNAME10         nvarchar(255) NULL,
      CUSTOM_ATTRVALUE1         nvarchar(255) NULL,
      CUSTOM_ATTRVALUE2         nvarchar(255) NULL,
      CUSTOM_ATTRVALUE3         nvarchar(255) NULL,
      CUSTOM_ATTRVALUE4         nvarchar(255) NULL,
      CUSTOM_ATTRVALUE5         nvarchar(255) NULL,
      CUSTOM_ATTRVALUE6         nvarchar(255) NULL,
      CUSTOM_ATTRVALUE7         nvarchar(255) NULL,
      CUSTOM_ATTRVALUE8         nvarchar(255) NULL,
      CUSTOM_ATTRVALUE9         nvarchar(255) NULL,
      CUSTOM_ATTRVALUE10      	nvarchar(255) NULL,
      CUSTOM_PARAMNAME1       	nvarchar(255) NULL,           /* additional NAME fileds that can be used to have their own columns instead of in big blob. */
      CUSTOM_PARAMNAME2       	nvarchar(255) NULL,
      CUSTOM_PARAMNAME3       	nvarchar(255) NULL,
      CUSTOM_PARAMNAME4       	nvarchar(255) NULL,
      CUSTOM_PARAMNAME5       	nvarchar(255) NULL,
      CUSTOM_PARAMNAME6       	nvarchar(255) NULL,
      CUSTOM_PARAMNAME7       	nvarchar(255) NULL,
      CUSTOM_PARAMNAME8       	nvarchar(255) NULL,
      CUSTOM_PARAMNAME9       	nvarchar(255) NULL,
      CUSTOM_PARAMNAME10    	nvarchar(255) NULL,
      CUSTOM_PARAMVALUE1    	nvarchar(255) NULL,
      CUSTOM_PARAMVALUE2    	nvarchar(255) NULL,
      CUSTOM_PARAMVALUE3    	nvarchar(255) NULL,
      CUSTOM_PARAMVALUE4    	nvarchar(255) NULL,
      CUSTOM_PARAMVALUE5    	nvarchar(255) NULL,
      CUSTOM_PARAMVALUE6    	nvarchar(255) NULL,
      CUSTOM_PARAMVALUE7    	nvarchar(255) NULL,
      CUSTOM_PARAMVALUE8    	nvarchar(255) NULL,
      CUSTOM_PARAMVALUE9    	nvarchar(255) NULL,
      CUSTOM_PARAMVALUE10   	nvarchar(255) NULL,
      PRIMARY KEY (LOG_ID)
);


CREATE TABLE ORG_POLICY (
   ORG_POLICY_ID 		nvarchar(32) NOT NULL,
   NAME						nvarchar(40) NULL,
   TARGET_AUDIENCE_TYPE 	nvarchar(20) NULL,
   TARGET_AUDIENCE 			nvarchar(255) NULL,
   START_DATE 				DATE NULL,
   END_DATE 				DATE NULL,
   POLICY_TEXT 				ntext NULL,
	PRIMARY KEY(ORG_POLICY_ID)
);

CREATE TABLE ORG_POLICY_USER_LOG (
   ORG_POLICY_LOG_ID 		nvarchar(32) NOT NULL,
   ORG_POLICY_ID 			nvarchar(32) NOT NULL,
   USER_ID 					nvarchar(32) NOT NULL,
   TIME_STAMP				DATETIME NOT NULL,
   RESPONSE					nvarchar(255) NULL,
	PRIMARY KEY(ORG_POLICY_LOG_ID));




CREATE  TABLE PRIVILEGE_DEF (
  PRIVILEGE_ID nvarchar(32) NOT NULL ,
  ACTION nvarchar(20) NULL ,
  DESCRIPTION nvarchar(255) NULL ,
  PRIMARY KEY (PRIVILEGE_ID) )


CREATE  TABLE RESOURCE_PRIVILEGE (
  RESOURCE_ID nvarchar(32) NOT NULL ,
  PRIVILEGE_ID nvarchar(32) NOT NULL ,
  PRIMARY KEY (RESOURCE_ID, PRIVILEGE_ID) ,
  CONSTRAINT FK_RESOURCE_PRIVILEGE_RES
    FOREIGN KEY (RESOURCE_ID )
    REFERENCES RES (RESOURCE_ID ),
  CONSTRAINT FK_RESOURCE_PRIVILEGE_PRIVILEGE_DEF
    FOREIGN KEY (PRIVILEGE_ID )
    REFERENCES PRIVILEGE_DEF (PRIVILEGE_ID) )


CREATE  TABLE USER_PRIVILEGE (
  USER_PRIVILEGE_ID nvarchar(32) NOT NULL,
  USER_ID nvarchar(32) NULL ,
  RESOURCE_ID nvarchar(32) NULL ,
  PRIVILEGE_ID nvarchar(32) NULL ,
  PERMIT smallint  NULL DEFAULT 0 ,
  START_DATE DATETIME NULL ,
  END_DATE DATETIME NULL ,
  PRIMARY KEY (USER_PRIVILEGE_ID) )


CREATE TABLE NOTIFICATION_CONFIG (
	   NOTIFICATION_CONFIG_ID 	nvarchar(32) NOT NULL,
	   NAME                     nvarchar(40) NULL,
	   SELECTION_PARAM_XML      ntext,
	   MSG_SUBJECT 				nvarchar(100) NULL,
       MSG_TEMPLATE_URL 		nvarchar(100) NULL,
       MSG_FROM					nvarchar(100) NULL,  /* IF ITS AN EMAIL */
       MSG_BCC					nvarchar(100) NULL,  /* IF ITS AN EMAIL */
       PRIMARY KEY (NOTIFICATION_CONFIG_ID))

