package org.example;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.data.repos.ImagesFilesRepository;
import org.example.data.repos.ImagesRepository;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import java.io.*;

@Path("photos")
public class ImagesResource {
    private static ImagesRepository imagesRepo;

    public ImagesResource() {
        imagesRepo = ImagesFilesRepository.getInstance();
    }

    public ImagesResource(ImagesRepository imagesRepo) {
        ImagesResource.imagesRepo = imagesRepo;
    }

    @GET
    @Path("{name}")
    @Produces("image/jpg")
    public Response getImage(@PathParam("name") String name) {
        String correctPath = "src/main/java/org/example/files/"+name;
        try{
            File file = imagesRepo.getImageByPath(correctPath);

            return Response.ok(file, "image/jpg").build();
        } catch (Exception e){
            return Response.status(404).build();
        }
    }
    @POST
    @Path("{user}/profile")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @PathParam("user") String user
    ) {
        try{
            imagesRepo.saveImageByStream(uploadedInputStream, user);

            return Response.status(Response.Status.CREATED).build();
        } catch (IOException e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        
    }



}
