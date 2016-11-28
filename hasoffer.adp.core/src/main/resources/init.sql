
create table t_hotdeal (
    id bigint(20)  not null auto_increment primary key,
    createTime datetime,
    sourceUrl varchar(255),
    title varchar(255),
    refprice float
 ) DEFAULT CHARSET=utf8;
