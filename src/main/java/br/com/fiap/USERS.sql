CREATE TABLE USERS (
                       ID VARCHAR2(36) PRIMARY KEY,
                       NOME VARCHAR2(200) NOT NULL,
                       EMAIL VARCHAR2(200) NOT NULL UNIQUE,
                       SENHA_HASH VARCHAR2(200) NOT NULL,
                       CREATED_AT TIMESTAMP
);
