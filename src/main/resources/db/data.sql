insert into user_tb(username, password, email) values('ssar', '$2a$10$dIy6lKq9RdWQ1YMUsvWT2u/VfGQKrCVVmv9JxW0V2LNu6zozVUMza', 'ssar@nate.com');
insert into user_tb(username, password, email) values('cos', '$2a$10$dIy6lKq9RdWQ1YMUsvWT2u/VfGQKrCVVmv9JxW0V2LNu6zozVUMza', 'cos@nate.com');
insert into board_tb(title, content, user_id, created_at) values('제', '내용1', 1, now());
insert into board_tb(title, content, user_id, created_at) values('Apple', '내용4', 2, now());
insert into board_tb(title, content, user_id, created_at) values('Apple2', '내용5', 2, now());
insert into board_tb(title, content, user_id, created_at) values('Banana', '내용4', 1, now());
insert into board_tb(title, content, user_id, created_at) values('喆', '내용5', 2, now());
insert into board_tb(title, content, user_id, created_at) values('목2', '내용2', 1, now());
insert into board_tb(title, content, user_id, created_at) values('22', '내용3', 1, now());
insert into board_tb(title, content, user_id, created_at) values('Apple喆', '내용4', 1, now());
insert into board_tb(title, content, user_id, created_at) values('apple3', '내용5', 2, now());
insert into board_tb(title, content, user_id, created_at) values('목3', '내용2', 1, now());
insert into board_tb(title, content, user_id, created_at) values('목4', '내용2', 2, now());
insert into board_tb(title, content, user_id, created_at) values('목5', '내용2', 1, now());
insert into board_tb(title, content, user_id, created_at) values('banana제22', '내용3', 1, now());
insert into board_tb(title, content, user_id, created_at) values('banana喆喆', '내용4', 1, now());
insert into board_tb(title, content, user_id, created_at) values('apple제3', '내용5', 2, now());
insert into board_tb(title, content, user_id, created_at) values('제목Applebanana123喆', '내용5', 2, now());
insert into reply_tb(comment, user_id, board_id) values('쌀', 1, 3);
insert into reply_tb(comment, user_id, board_id) values('감자', 1, 2);
insert into reply_tb(comment, user_id, board_id) values('딸기', 2, 1);
insert into reply_tb(comment, user_id, board_id) values('수박', 2, 5);
insert into reply_tb(comment, user_id, board_id) values('사과', 2, 4);
insert into reply_tb(comment, user_id, board_id) values('배', 1, 3);
insert into reply_tb(comment, user_id, board_id) values('멜론', 2, 2);
insert into reply_tb(comment, user_id, board_id) values('사과', 1, 1);
insert into reply_tb(comment, user_id, board_id) values('수박', 2, 5);
insert into reply_tb(comment, user_id, board_id) values('배', 2, 3);
insert into reply_tb(comment, user_id, board_id) values('멜론', 1, 2);
insert into reply_tb(comment, user_id, board_id) values('사과', 2, 1);
insert into reply_tb(comment, user_id, board_id) values('수박', 1, 4);