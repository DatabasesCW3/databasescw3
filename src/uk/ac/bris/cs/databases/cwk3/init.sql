DROP TABLE IF EXISTS Person;
DROP TABLE IF EXISTS Post;
DROP TABLE IF EXISTS Topic;
DROP TABLE IF EXISTS Forum;
DROP TABLE IF EXISTS LikesPost;
DROP TABLE IF EXISTS LikesTopic;
DROP TABLE IF EXISTS Favourite;

CREATE TABLE Person (
  id INTEGER PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  username VARCHAR(10) NOT NULL UNIQUE,
  stuId VARCHAR(10) NULL
);

CREATE TABLE Post (
  id INTEGER PRIMARY KEY,
  user INTEGER REFERENCES Person(id),
  body TEXT NOT NULL
);

CREATE TABLE Topic (
  id INTEGER PRIMARY KEY,
  user INTEGER REFERENCES Person(id),
  title VARCHAR(100) NOT NULL,
  body TEXT NOT NULL
);

CREATE TABLE Forum (
  id INTEGER PRIMARY KEY,
  title VARCHAR(100) NOT NULL
);

CREATE TABLE LikesPost (
  id INTEGER PRIMARY KEY,
  user INTEGER REFERENCES Person(id),
  post INTEGER REFERENCES Post(id)
);

CREATE TABLE LikesTopic (
  id INTEGER PRIMARY KEY,
  user INTEGER REFERENCES Person(id),
  topic INTEGER REFERENCES Topic(id)
);

CREATE TABLE Favourite (
  id INTEGER PRIMARY KEY,
  user INTEGER REFERENCES Person(id),
  topic INTEGER REFERENCES Topic(id)
);
