package org.example;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.data.repos.ImagesFilesRepository;
import org.example.data.repos.ImagesRepository;
import org.example.data.repos.PhotosDbRepository;
import org.example.data.repos.PhotosRepository;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Path("photos")
public class ImagesResource {
    private static ImagesRepository imagesRepo;
    private static PhotosRepository photosRepo;

    public ImagesResource() {
        imagesRepo = ImagesFilesRepository.getInstance();
        photosRepo = PhotosDbRepository.getInstance();
    }

    public ImagesResource(ImagesRepository imagesRepo, PhotosRepository photosRepo) {
        ImagesResource.imagesRepo = imagesRepo;
        ImagesResource.photosRepo = photosRepo;
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
    public Response uploadProfilePicture(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @PathParam("user") String user
    ) {
        System.out.println("Uploading profile picture for user "+ user);
        try{
            imagesRepo.saveImageByStream(user, uploadedInputStream);
            photosRepo.saveProfilePicture(user, user+".jpg");

            return Response.status(Response.Status.CREATED).build();
        } catch (IOException e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        
    }

    @POST
    @Path("auctions/{id}/photo/{index}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadAuctionPhoto(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @PathParam("id") Integer auctionId,
            @PathParam("index") Integer photoIndex
    ) {
        System.out.println("Uploading picture n. "+photoIndex+"for auction with id "+auctionId);
        try{
            String name = "auction"+auctionId+"-photo"+photoIndex;
            imagesRepo.saveImageByStream(name, uploadedInputStream);
            photosRepo.saveAuctionPicture(auctionId, name+".jpg");

            return Response.status(Response.Status.CREATED).build();
        } catch (IOException e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

}
