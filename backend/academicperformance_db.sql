drop database academicperformancedb;
drop user academiceperformance;
create user academicperformance with password 'password';
create database academicperformancedb with template=template0 owner=academicperformance;
\connect academicperformancedb;
alter default privileges grant all on tables to academicperformance;
alter default privileges grant all on sequences to academicperformance;

create table ap_users(
user_id integer primary key not null,
first_name varchar(20) not null,
last_name varchar(20) not null,
kind varchar(20) not null,
username varchar(30) not null,
password text not null
);

create table ap_teachers(
teacher_id integer primary key not null,
user_id integer not null
);
alter table ap_teachers add constraint teach_users_fk
foreign key (user_id) references ap_users(user_id);

create table ap_subjects(
subject_id integer primary key not null,
name varchar(20) not null
);

create table ap_teacher_subject(
teacher_id integer not null,
subject_id integer not null,
primary key(teacher_id, subject_id)
);
alter table ap_teacher_subject add constraint teacher_subject_teacher_fk
foreign key (teacher_id) references ap_teachers(teacher_id);
alter table ap_teacher_subject add constraint teacher_subject_subject_fk
foreign key (subject_id) references ap_subjects(subject_id);

create table ap_students(
student_id integer primary key not null,
user_id integer not null,
address varchar(40) not null,
dob date not null,
year integer not null
);
alter table ap_students add constraint stu_users_fk
foreign key (user_id) references ap_users(user_id);

create table ap_reports(
report_id integer primary key not null,
student_id integer not null,
subject_id integer not null,
year integer not null,
title varchar(20) not null,
obtained_marks numeric(10,2) not null,
maximum_marks numeric(10,2) not null
);
alter table ap_reports add constraint rep_stud_fk
foreign key (student_id) references ap_students(student_id);
alter table ap_reports add constraint rep_sub_fk
foreign key (subject_id) references ap_subjects(subject_id);

create table ap_comments(
comment_id integer primary key not null,
report_id integer not null,
description text not null
);
alter table ap_comments add constraint comm_rep_fk
foreign key (report_id) references ap_reports(report_id);

create sequence ap_users_seq increment 1 start 1;
create sequence ap_teachers_seq increment 1 start 1000;
create sequence ap_students_seq increment 1 start 10000;
create sequence ap_subjects_seq increment 1 start 100000;
create sequence ap_reports_seq increment 1 start 1000000;
create sequence ap_comments_seq increment 1 start 10000000;
