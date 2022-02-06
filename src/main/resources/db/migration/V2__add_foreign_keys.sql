alter table post_comments
    add constraint FK_POST_COMMENTS_ON_PARENT foreign key (parent_id) references post_comments (id);
alter table post_comments
    add constraint FK_POST_COMMENTS_ON_POST foreign key (post_id) references posts (id);
alter table post_comments
    add constraint FK_POST_COMMENTS_ON_USER foreign key (user_id) references users (id);
alter table post_votes
    add constraint FK_POST_VOTES_ON_POST foreign key (post_id) references posts (id);
alter table post_votes
    add constraint FK_POST_VOTES_ON_USER foreign key (user_id) references users (id);
alter table posts
    add constraint FK_POSTS_ON_MODERATOR foreign key (moderator_id) references users (id);
alter table posts
    add constraint FK_POSTS_ON_USER foreign key (user_id) references users (id);
alter table tag2post
    add constraint FK_TAG2POSTS_ON_POST foreign key (post_id) references posts (id);
alter table tag2post
    add constraint FK_TAG2POSTS_ON_TAG foreign key (tag_id) references tags (id);
