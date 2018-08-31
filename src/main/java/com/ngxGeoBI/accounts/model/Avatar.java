package com.ngxGeoBI.accounts.model;

import lombok.Data;
import org.springframework.http.MediaType;

@Data
public class Avatar {

    private Long userId;
    private byte[] photo;
    private MediaType mediaType;

    public Avatar(long userId, byte[] data, MediaType mediaType) {
        this.mediaType = mediaType;
        this.photo = data;
        this.userId = userId;
    }

    public MediaType getMediaType() {
        return this.mediaType;
    }

    public byte[] getPhoto() {
        return this.photo;
    }

    public Long getUserId() {
        return this.userId;
    }
}
