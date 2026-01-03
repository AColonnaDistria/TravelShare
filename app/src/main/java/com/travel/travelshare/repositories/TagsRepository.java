package com.travel.travelshare.repositories;
import com.travel.travelshare.model.annotation.Tag;

public class TagsRepository extends SimpleRepository<Tag> {
    public TagsRepository() {
        super(Tag.class, "travelshare_tags");
    }
}