CREATE INDEX indx_login_user_id ON LOGIN (USER_ID);
CREATE INDEX indx_login_login ON LOGIN (LOGIN);
CREATE INDEX indx_login_service_id ON LOGIN (SERVICE_ID);
CREATE INDEX indx_login_last_login ON LOGIN (LAST_LOGIN);

CREATE INDEX indx_email_address_parent_id ON EMAIL_ADDRESS (PARENT_ID);
CREATE INDEX indx_email_address_email_address ON EMAIL_ADDRESS (EMAIL_ADDRESS);

CREATE INDEX indx_phone_parent_id ON PHONE (PARENT_ID);
CREATE INDEX indx_phone_area_id ON PHONE (AREA_CD);
CREATE INDEX indx_phone_phone_nbr ON PHONE (PHONE_NBR);

CREATE INDEX indx_user_attribute_name ON USER_ATTRIBUTES (NAME);
CREATE INDEX indx_user_attribute_user_id ON USER_ATTRIBUTES (USER_ID);
CREATE INDEX indx_user_attribute_user_id ON USER_ATTRIBUTES (VALUE);
CREATE INDEX indx_user_attribute_user_id ON USER_ATTRIBUTES (METADATA_ID);

CREATE INDEX indx_user_grp_user_id ON USER_GRP (USER_ID);

CREATE INDEX indx_company_company_id ON COMPANY (COMPANY_ID);

CREATE INDEX indx_users_company_id ON USERS (COMPANY_ID);
CREATE INDEX indx_users_show_in_search ON USERS (SHOW_IN_SEARCH);
CREATE INDEX indx_users_user_type_ind ON USERS (USER_TYPE_IND);
CREATE INDEX indx_users_classification ON USERS (CLASSIFICATION);
CREATE INDEX indx_users_location_cd ON USERS (LOCATION_CD);
CREATE INDEX indx_users_first_name ON USERS (FIRST_NAME);
CREATE INDEX indx_users_last_name ON USERS (LAST_NAME);
CREATE INDEX indx_users_nickname ON USERS (NICKNAME);
CREATE INDEX indx_users_status ON USERS (STATUS);
CREATE INDEX indx_users_secondary_status ON USERS (SECONDARY_STATUS);
CREATE INDEX indx_users_start_date ON USERS (START_DATE);
CREATE INDEX indx_users_last_date ON USERS (LAST_DATE);
CREATE INDEX indx_users_birthday ON USERS (BIRTHDATE);
CREATE INDEX indx_users_postal_cd ON USERS (POSTAL_CD);
CREATE INDEX indx_users_dept_cd ON USERS (DEPT_CD);
CREATE INDEX indx_users_division ON USERS (DIVISION);
CREATE INDEX indx_users_employee_id ON USERS (EMPLOYEE_ID);

