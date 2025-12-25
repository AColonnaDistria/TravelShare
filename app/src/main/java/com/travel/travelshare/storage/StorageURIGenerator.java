package com.travel.travelshare.storage;

import java.util.UUID;

public class StorageURIGenerator {
    public static String generateImageURI(String extension) {
        return StorageConfig.IMAGE_URI_ROUTE + UUID.randomUUID().toString() + extension;
    }
}
