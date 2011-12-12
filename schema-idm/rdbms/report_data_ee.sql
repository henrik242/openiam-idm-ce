INSERT INTO MENU(MENU_ID, LANGUAGE_CD, MENU_GROUP, MENU_NAME, MENU_DESC, URL, ACTIVE, DISPLAY_ORDER)
  VALUES ('AUDITREPORT', 'en', 'REPORT', 'Audit Reports', 'Audit Information Reports', 'AUDITREPORT.rptdesign',1, 9);
  
INSERT INTO MENU(MENU_ID, LANGUAGE_CD, MENU_GROUP, MENU_NAME, MENU_DESC, URL, ACTIVE, DISPLAY_ORDER)
  VALUES ('AUTHENTICATIONrpt', 'en', 'REPORT', 'Authentication Report', 'Authentication Report', 'authentication.rptdesign',1, 1);

INSERT INTO MENU(MENU_ID, LANGUAGE_CD, MENU_GROUP, MENU_NAME, MENU_DESC, URL, ACTIVE, DISPLAY_ORDER)
  VALUES ('INACTIVITYrpt', 'en', 'REPORT', 'Inactive Users Report', 'Inactive Users Report', 'inactivity.rptdesign', 1,2);

INSERT INTO MENU(MENU_ID, LANGUAGE_CD, MENU_GROUP, MENU_NAME, MENU_DESC, URL, ACTIVE, DISPLAY_ORDER)
  VALUES ('PWDCHANGEDrpt', 'en', 'REPORT', 'Password Changed Report', 'Password Changed Report', 'pwd_changed.rptdesign',1, 3);

INSERT INTO MENU(MENU_ID, LANGUAGE_CD, MENU_GROUP, MENU_NAME, MENU_DESC, URL, ACTIVE, DISPLAY_ORDER)
  VALUES ('PWDRESETrpt', 'en', 'REPORT', 'Password Reset Report', 'Password Reset Report', 'pwd_reset.rptdesign',1, 4);

INSERT INTO MENU(MENU_ID, LANGUAGE_CD, MENU_GROUP, MENU_NAME, MENU_DESC, URL, ACTIVE, DISPLAY_ORDER)
  VALUES ('ROLESDOMAINrpt', 'en', 'REPORT', 'Roles by Security Domain', 'Roles by Security Domain', 'roles_domain.rptdesign', 1,5);

INSERT INTO MENU(MENU_ID, LANGUAGE_CD, MENU_GROUP, MENU_NAME, MENU_DESC, URL, ACTIVE, DISPLAY_ORDER)
  VALUES ('USERACTIVATErpt', 'en', 'REPORT', 'Activated Users Report', 'Activated Users Report', 'user_activate.rptdesign',1, 6);

INSERT INTO MENU(MENU_ID, LANGUAGE_CD, MENU_GROUP, MENU_NAME, MENU_DESC, URL, ACTIVE, DISPLAY_ORDER)
  VALUES ('USERDEACTIVATIONrpt', 'en', 'REPORT', 'Users Pending Deactivation', 'Users Pending Deactivation', 'user_deactivation.rptdesign',1, 7);

INSERT INTO MENU(MENU_ID, LANGUAGE_CD, MENU_GROUP, MENU_NAME, MENU_DESC, URL, ACTIVE, DISPLAY_ORDER)
  VALUES ('USERMNGrpt', 'en', 'REPORT', 'User List', 'User List', 'user_mng.rptdesign',1, 8);

commit;