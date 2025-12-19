package com.travel.travelshare.storage;

import java.util.List;

public class StorageConfig {
    public static final String FIREBASE_STORAGE_URI = "gs://travelshare-2609c.firebasestorage.app";

    public static final List<String> IMAGE_SUPPORTED_EXTENSIONS = List.of(
            "png", "jpg", "jpeg", "bmp", "webp", "gif"
    );

    public static final String IMAGE_URI_ROUTE = "travelshare_images";

    public static final String PICTURE_POSTS_ROUTE = "travelshare_picture_posts";
    public static final String COMMENTS_ROUTE = "travelshare_comments";
    public static final String SHARED_TO_ROUTE = "travelshare_shared_to";
    public static final String TAGS_ROUTE = "travelshare_tags";

    public static final String USERS_ROUTE = "travelshare_users";
    public static final String USER_GROUPS_ROUTE = "travelshare_user_groups";
    public static final String FOLLOWS_ROUTE = "travelshare_follows";
    public static final String LIKES_ROUTE = "travelshare_likes";
}
