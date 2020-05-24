drop table users;

CREATE TABLE users (
    ID int PRIMARY KEY AUTO_INCREMENT,
    email varchar(255) UNIQUE,
    firstname varchar(255),
    lastname varchar(255),
    passwordhash varchar(255) NOT NULL,
    salt varchar(255) NOT NULL,
    userrole enum("ADMIN", "TEACHER", "STUDENT")
	
);

INSERT INTO users (email, firstname, lastname, passwordhash, salt, userrole)
VALUES
("finta@finto.finto","cambiami", "subito", "e7828842daefa995120b53a0a6b51b332ba8e57a0a9f8f59825291da68e0b0e2", "1434736222", "ADMIN")
;


CREATE TABLE courses (
    ID int PRIMARY KEY AUTO_INCREMENT,
    name varchar(255) UNIQUE,
    description text,
    CFU int,
    teacher int NOT NULL,
    CONSTRAINT FK_C_1 FOREIGN KEY (teacher) references users(ID) ON DELETE RESTRICT ON UPDATE CASCADE	
);


CREATE TABLE votes (
    ID int PRIMARY KEY AUTO_INCREMENT,
    course int NOT NULL,
    student int NOT NULL,
    vote int,
    status enum("VOID", "ASSIGNED", "ACCEPTED", "DECLINED"),
    CONSTRAINT FK_R_1 FOREIGN KEY (course) references courses(ID) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT FK_R_2 FOREIGN KEY (student) references users(ID) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT FK_unique UNIQUE (course,student)

);



INSERT INTO users (email, firstname, lastname, passwordhash, salt, userrole)
VALUES
("fintass@finto.finto","cambiami", "subito", "e7828842daefa995120b53a0a6b51b332ba8e57a0a9f8f59825291da68e0b0e2", "1434736222", "ADMIN"),
("1@finto.finto","cambiami", "subito", "e7828842daefa995120b53a0a6b51b332ba8e57a0a9f8f59825291da68e0b0e2", "1434736222", "STUDENT"),
("2@finto.finto","EEG", "subito", "e7828842daefa995120b53a0a6b51b332ba8e57a0a9f8f59825291da68e0b0e2", "1434736222", "TEACHER"),
("3@finto.finto","cambiami", "subsdfsdito", "e7828842daefa995120b53a0a6b51b332ba8e57a0a9f8f59825291da68e0b0e2", "1434736222", "ADMIN")
;


Ogni corso, invece, è caratterizzato da un codice univoco, un nome, una breve descrizione, un
numero di crediti (CFU) e un docente titolare.
I voti vanno da 1 a 30 e possono trovarsi in diversi stati: {ASSEGNATO, ACCETTATO, RIFIUTATO}.
-- password
-- 1434736222
-- 1434736222password
-- e7828842daefa995120b53a0a6b51b332ba8e57a0a9f8f59825291da68e0b0e2






