CREATE TABLE exam_user  (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  openid varchar(100) ,
  user_name varchar(100),
  create_time datetime ,
  status char(1) NULL DEFAULT 1,
  PRIMARY KEY (id)
);


CREATE TABLE exam_questionid  (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  question_date date NULL,
  question_endid bigint(20),
  PRIMARY KEY (id)
);

CREATE TABLE exam_user_question  (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  user_id bigint(20),
  unionid VARCHAR(100),
	openid VARCHAR(100),
	activityid bigint(20),
	questionid bigint(20),
	answer VARCHAR(100),
	answer_order int,
	answer_count int,
	integral int,
	status char(1) NULL DEFAULT 1,
	create_time datetime,
	update_time datetime,
  PRIMARY KEY (id)
);

CREATE TABLE exam_integral_count  (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  user_id bigint(20),
  unionid VARCHAR(100),
	integral_count int,
	activityid bigint(20),
	create_time datetime,
	update_time datetime,
  PRIMARY KEY (id)
);

CREATE TABLE exam_integral_details  (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  user_id bigint(20),
  unionid VARCHAR(100),
	openid VARCHAR(100),
	content VARCHAR(600),
	integral int,
	integral_type char(1),
	activityid bigint(20),
	create_time datetime,
  PRIMARY KEY (id)
);
