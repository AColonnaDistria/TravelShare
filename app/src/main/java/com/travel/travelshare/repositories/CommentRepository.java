package com.travel.travelshare.repositories;
import com.travel.travelshare.model.post.Comment;

public class CommentRepository extends SimpleRepository<Comment> {

    public CommentRepository() {
        super(Comment.class, "travelshare_comments");
    }
}
