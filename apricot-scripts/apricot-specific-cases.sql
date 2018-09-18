create table triller (
   name varchar(100),
   price int
)
create unique index triller_idx on triller (name);

insert into triller values ('aaa', 100);
insert into triller values (null, null);
delete from triller where name = 'aaa'
select * from triller;
insert into triller values ('bbb', 1000);

create table ref_table (
   prefix int not null,
   suffix int not null,
   name varchar(100),
   created date,
   price int,
   primary key (suffix, prefix)
)

insert into ref_table values (10, 20, 'first', '2018-09-18', 1000);
insert into ref_table values (10, 30, 'second', '2018-09-18', 2000);
insert into ref_table values (5, 50, 'second', '2018-09-18', 3000);
select * from ref_table;

create table master_doom (
   id int primary key,
   prefix int not null,
   suffix int,
   sdefinition varchar(100) not null
)

alter table master_doom with check add constraint fk_master_doom
   foreign key(prefix, suffix)
   references ref_table(suffix, prefix);

insert into master_doom values (1000, 20, 10, 'def 1');
insert into master_doom values (2000, 20, 10, 'def 2');
insert into master_doom values (3000, 20, null, 'def 3');
insert into master_doom values (4000, 50, 5, 'def 4');

select * from master_doom;

create table ref_strange (
   prefix int not null,
   suffix int,
   name varchar(100),
   created date,
   price int,
   primary key (suffix, prefix)
)

insert into ref_strange values (10, 20, null, null, null);
insert into ref_strange values (10, null, null, null, null);


create table triller_book (
   id int primary key,
   script varchar(200),
   name varchar(100) not null
);

alter table triller_book with check add constraint fk_triller_book
   foreign key(name)
   references triller(name);


create table book_author (
   id int primary key,
   a_name varchar(100) not null,
   a_surename varchar(100) not null
);

create unique index book_author_idx on book_author (a_name, a_surename);

create table book_title (
   title_id int primary key,
   title_name varchar(100) not null,
   title_surename varchar(100) not null
);

alter table book_title with check add constraint fk_book_title
   foreign key(title_name, title_surename)
   references book_author(a_name, a_surename);

create table book_ref (
   id int,
   book_name varchar(100) not null,
   isbn varchar(100)
   constraint book_ref_unique unique (isbn)
);

create table book_shelf (
   shelf_id int primary key,
   isbn varchar(100) not null
)

alter table book_shelf with check add constraint fk_book_shelf
   foreign key(isbn)
   references book_ref(isbn);

insert into book_ref values (10, 'Dondondon', null);
insert into book_ref values (10, 'Mmmmeeee', '1-94');
select * from book_ref;

insert into book_shelf values (10, '1-94');
insert into book_shelf values (20, '1-94');
select * from book_shelf;
