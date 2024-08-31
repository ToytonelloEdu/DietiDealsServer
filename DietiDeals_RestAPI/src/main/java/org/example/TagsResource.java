package org.example;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.data.entities.Tag;
import org.example.data.repos.TagsDbRepository;
import org.example.data.repos.TagsRepository;

import java.util.List;

@Path("tags")
public class TagsResource {
    final TagsRepository tagsRepo;

    public TagsResource() {
        tagsRepo = TagsDbRepository.getInstance();
    }

    public TagsResource(TagsRepository tagsRepo) {
        this.tagsRepo = tagsRepo;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Tag> getAllTags() {
        List<Tag> ret = tagsRepo.getAllTags();
        System.out.println(ret.size());
        for (Tag tag : ret) {
            System.out.println(tag);
        }
        return ret;
    }

    @POST
    //@RequireAuth
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addTag(Tag tag) {
        try{
            Tag resTag = tagsRepo.addTag(tag);
            return Response.status(Response.Status.CREATED).entity(resTag).build();
        } catch(Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }

}
