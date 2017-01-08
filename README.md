# myItunes
My database schema is called itunes.
Use this code to create the table and insert a value
//
CREATE TABLE Aaudio(
	aid int(5) auto_increment,
    aname varchar(100),
    album varchar(100),
    artist varchar(100),
    genre varchar(20),
    path varchar(100),
    primary key (aid)
);
INSERT INTO Aaudio VALUES(1, "", "", "", "", "")
//

