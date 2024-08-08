package org.example.data.repos;

import java.io.*;

public class ImagesFilesRepository implements ImagesRepository{
    private static volatile ImagesFilesRepository instance;

    private ImagesFilesRepository(){}

    public static ImagesFilesRepository getInstance(){
        if(instance == null){
            instance = new ImagesFilesRepository();
        }
        return instance;
    }


    @Override
    public File getImageByPath(String path) throws FileNotFoundException {
        File file = new File(path);
        if(! file.exists()) throw new FileNotFoundException();
        return file;
    }


    @Override
    public void saveImageByStream(String name, InputStream inputStream) throws IOException {
        OutputStream os = null;
        try {
            File fileToUpload = new File("src/main/java/org/example/files/"+name +".jpg");
            os = new FileOutputStream(fileToUpload);
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) != -1) {
                os.write(b, 0, length);
            }
            os.flush();
        } finally {
            try {
                assert os != null;
                os.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
