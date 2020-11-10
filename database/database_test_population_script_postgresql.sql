delete from trackinplaylist;
delete from track;
delete from playlist;
delete from "owner";

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
(1,'jopie', 'liedje', 111,345,true,'bloemetjes',null,null),
(2,'harry', 'abc', 111,345,true,'bijtjes',null,null),
(3,'hans', 'eend', 111,345,true,'torretjes',null,null),
(4,'gert', 'aogrh', 111,345,true,null,'11-11-1111','wfwefewf'),
(5,'bert', 'eagaend', 111,345,true,null,'11-11-1111','wffhwefihwe'),
(6,'zeep', 'sge', 111,345,true,null,'11-11-1111','wffhwefihwe');

insert into "owner"(username, "password", "token") values
('mo','12345','qwerty');

insert into playlist("name", username) values
('myplaylist','mo'),
('myplaylist2','mo');

insert into trackinplaylist(playlistid, trackid) values
(1,1),
(1,2),
(1,3),
(2,3),
(2,4),
(2,5);