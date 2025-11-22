CREATE TABLE RECOMMENDATION_HISTORY (
                                        ID VARCHAR2(36) PRIMARY KEY,
                                        USER_ID VARCHAR2(36) NOT NULL,
                                        DATA TIMESTAMP NOT NULL,
                                        MACRO_AREA VARCHAR2(200) NOT NULL,
                                        CURSOS_RECOMENDADOS CLOB,
                                        FOREIGN KEY (USER_ID) REFERENCES USERS(ID)
);
