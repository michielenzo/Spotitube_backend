drop table if exists trackinplaylist;
drop table if exists playlist;
drop table if exists "owner";
drop table if exists track;

create table "owner"(
	username varchar(50) not null primary key,
	"password" varchar(50) not null,
	"token" varchar(50) null
);

create table track(
	trackid int primary key,
	performer varchar(50) not null,
	title varchar(50) not null,
	playcount int not null,
	duration int not null,
	offlineavailable boolean not null,

	album varchar(50) null,

	publicationdate varchar(50) null,
	description varchar(1000) null
);

create table playlist(
	playlistid int generated always as identity primary key,
	"name" varchar(50) not null,
	username varchar(50),
	foreign key (username) references owner(username)
);

create table trackinplaylist(
	playlistid int,
	trackid int,
	primary key(playlistid, trackid),
	foreign key (playlistid) references playlist(playlistid),
	foreign key (trackid) references track(trackid)
);