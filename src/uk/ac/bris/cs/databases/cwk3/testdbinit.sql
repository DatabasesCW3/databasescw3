-- adds dummy data to the test db

--                        ID name            uname      stuId
INSERT INTO Person VALUES(1, 'testperson 1', 'uname 1', 'stuId 1');
INSERT INTO Person VALUES(2, 'testperson 2', 'uname 2', 'stuId 2');
INSERT INTO Person VALUES(3, 'testperson 3', 'uname 3', 'stuId 3');
INSERT INTO Person VALUES(4, 'testperson 4', 'uname 4', 'stuId 4');
INSERT INTO Person VALUES(5, 'testperson 5', 'uname 5', 'stuId 5');

--                      id    u  tp whn  postnum
INSERT INTO Post VALUES(1,    1, 1, 0 ,   1, 'test post 1');
INSERT INTO Post VALUES(2,    3, 2, 600,  1, 'test post 1t2');
INSERT INTO Post VALUES(3,    3, 1, 5,    2, 'test post 2');
INSERT INTO Post VALUES(4,    2, 1, 7,    3, 'test post 3');
INSERT INTO Post VALUES(5,    4, 1, 8,    4, 'test post 4');
INSERT INTO Post VALUES(6,    4, 3, 7,    1, 'test post 1t3');
INSERT INTO Post VALUES(7,    4, 4, 900,  1, 'test post 1t4');
INSERT INTO Post VALUES(null, 4, 1, 9,    5, 'test post 5');
INSERT INTO Post VALUES(null, 4, 1, 10,   6, 'test post 6');
INSERT INTO Post VALUES(null, 4, 1, 11,   7, 'test post 7');
INSERT INTO Post VALUES(null, 4, 1, 12,   8, 'test post 8');
INSERT INTO Post VALUES(null, 4, 1, 13,   9, 'test post 9');
INSERT INTO Post VALUES(null, 4, 1, 14,   10, 'test post 10');
INSERT INTO Post VALUES(null, 4, 1, 15,   11, 'test post 11');
INSERT INTO Post VALUES(null, 4, 1, 16,   12, 'test post 12');
INSERT INTO Post VALUES(null, 4, 1, 17,   13, 'test post 13');
INSERT INTO Post VALUES(null, 4, 1, 18,   14, 'test post 14');

INSERT INTO Forum VALUES(1, 'test forum 1');
INSERT INTO Forum VALUES(2, 'test forum 2');

--                       id author            forum
INSERT INTO Topic VALUES(1, 1, 'testtopic 1', 1);
INSERT INTO Topic VALUES(2, 1, 'testtopic 2', 2);
INSERT INTO Topic VALUES(3, 1, 'testtopic 3', 1);
INSERT INTO Topic VALUES(4, 1, 'testtopic 4', 2);

--                            id   usr topic
INSERT INTO LikesTopic VALUES(null, 1, 1);
INSERT INTO LikesTopic VALUES(null, 2, 1);

--                            id   usr post
INSERT INTO LikesPost VALUES(null, 1, 1);
INSERT INTO LikesPost VALUES(null, 1, 13);
INSERT INTO LikesPost VALUES(null, 2, 14);
INSERT INTO LikesPost VALUES(null, 1, 15);
INSERT INTO LikesPost VALUES(null, 3, 1);
