package org.example.data.repos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface ImagesRepository {

    File getImageByPath(String path) throws FileNotFoundException;

    void saveImageByStream(String user, InputStream inputStream) throws IOException;

}
