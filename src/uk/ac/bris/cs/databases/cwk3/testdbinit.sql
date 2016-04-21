-- adds dummy data to the test db

--                        ID name            uname      stuId
INSERT INTO Person VALUES(1, 'testperson 1', 'uname 1', 'stuId 1');
INSERT INTO Person VALUES(2, 'testperson 2', 'uname 2', 'stuId 2');
INSERT INTO Person VALUES(3, 'testperson 3', 'uname 3', 'stuId 3');
INSERT INTO Person VALUES(4, 'testperson 4', 'uname 4', 'stuId 4');
INSERT INTO Person VALUES(5, 'testperson 5', 'uname 5', 'stuId 5');

--                      id u  t  whn postnum
INSERT INTO Post VALUES(1, 1, 1, 0, 1, 'test post 1');
INSERT INTO Post VALUES(2, 3, 2, 0, 1, 'test post 1t2');
INSERT INTO Post VALUES(3, 3, 1, 0, 2, 'test post 2');
INSERT INTO Post VALUES(4, 2, 1, 0, 3, 'test post 3');
INSERT INTO Post VALUES(5, 4, 1, 0, 4, 'test post 4');

INSERT INTO Forum VALUES(1, 'test forum 1');
INSERT INTO Forum VALUES(2, 'test forum 2');

--                       id author            forum
INSERT INTO Topic VALUES(1, 1, 'testtopic 1', 1);
INSERT INTO Topic VALUES(2, 1, 'testtopic 2', 2);

--                            id   usr topic
INSERT INTO LikesTopic VALUES(null, 1, 1);
INSERT INTO LikesTopic VALUES(null, 2, 1);