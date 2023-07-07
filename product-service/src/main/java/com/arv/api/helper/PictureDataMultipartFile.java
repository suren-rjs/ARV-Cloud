package com.arv.api.helper;

import org.apache.poi.ss.usermodel.PictureData;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PictureDataMultipartFile implements MultipartFile {

    private final PictureData pictureData;
    private final String filename;

    public PictureDataMultipartFile(PictureData pictureData, String filename) {
        this.pictureData = pictureData;
        this.filename = filename;
    }

    @Override
    public String getName() {
        return filename;
    }

    @Override
    public String getOriginalFilename() {
        return filename;
    }

    @Override
    public String getContentType() {
        return "image/" + pictureData.suggestFileExtension();
    }

    @Override
    public boolean isEmpty() {
        return pictureData == null || pictureData.getData() == null;
    }

    @Override
    public long getSize() {
        return pictureData != null && pictureData.getData() != null ? pictureData.getData().length : 0;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return pictureData != null ? pictureData.getData() : new byte[0];
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(getBytes());
    }

    @Override
    public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
        // Not implemented for simplicity
    }
}
