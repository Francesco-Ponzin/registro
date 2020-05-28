CREATE TABLE users (
    ID int PRIMARY KEY AUTO_INCREMENT,
    email varchar(255) UNIQUE NOT NULL,
    firstname varchar(255),
    lastname varchar(255),
    passwordhash varchar(255) NOT NULL,
    salt varchar(255) NOT NULL,
    userrole enum("ADMIN", "TEACHER", "STUDENT")
	
);




CREATE TABLE courses (
    ID int PRIMARY KEY AUTO_INCREMENT,
    name varchar(255) UNIQUE NOT NULL,
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
("admin@engim.edu","amministratore", "provvisorio", "e7828842daefa995120b53a0a6b51b332ba8e57a0a9f8f59825291da68e0b0e2", "1434736222", "ADMIN")
;



-- password
-- 1434736222
-- 1434736222password
-- e7828842daefa995120b53a0a6b51b332ba8e57a0a9f8f59825291da68e0b0e2






