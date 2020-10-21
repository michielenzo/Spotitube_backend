use spotitube;
go

delete from trackinplaylist;
delete from track;
delete from playlist;
delete from owner;
go

insert into track(trackid,
				  performer,
				  title,
				  playcount,
				  duration,
				  offlineavailable,
				  album,
				  publicationdate,
				  description)
values
(1,'jopie', 'liedje', 111,345,1,'bloemetjes',null,null),
(2,'harry', 'abc', 111,345,1,'bijtjes',null,null),
(3,'hans', 'eend', 111,345,1,'torretjes',null,null),
(4,'gert', 'aogrh', 111,345,1,null,'11-11-1111','wfwefewf'),
(5,'bert', 'eagaend', 111,345,1,null,'11-11-1111','wffhwefihwe'),
(6,'zeep', 'sge', 111,345,1,null,'11-11-1111','wffhwefihwe');
go

insert into owner(username, password, token) values
('mo','12345','qwerty');
go

insert into playlist(playlistid, name, username) values
(1,'myplaylist','mo'),
(2,'myplaylist2','mo');
go

insert into trackinplaylist(playlistid, trackid) values
(1,1),
(1,2),
(1,3),
(2,3),
(2,4),
(2,5);
go
