package org.example;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Base64;

@Path("photos")
public class ImagesResource {

    @GET
    @Path("{name}")
    @Produces("image/jpg")
    public Response getImage(@PathParam("name") String name) {
        String correctPath = "src/main/java/org/example/files/"+name;
        try{
            File file = new File(correctPath);
            if(! file.exists()) throw new FileNotFoundException();
            return Response.ok(file, "image/jpg")
                    //.header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"")
                    .build();
        } catch (Exception e){
            return Response.status(404).build();
        }
    }

    @POST
    @Path("{user}/profile")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String uploadProPic(@PathParam("user") String username,@FormParam("image") String image) throws FileNotFoundException {
        String result = "false";
        FileOutputStream fos;

        fos = new FileOutputStream("src/main/java/org/example/files/" + username + ".jpg");

        // decode Base64 String to image
        try
        {

            byte[] byteArray = Base64.getMimeDecoder().decode(image);
            fos.write(byteArray);

            result = "true";
            fos.close();
        }
        catch (Exception e)
        {
            return e.getLocalizedMessage();
        }

        return result;
    }



}
