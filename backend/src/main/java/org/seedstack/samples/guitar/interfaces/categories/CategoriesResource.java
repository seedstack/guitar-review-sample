package org.seedstack.samples.guitar.interfaces.categories;

import org.seedstack.jpa.JpaUnit;
import org.seedstack.samples.guitar.domain.model.category.Category;
import org.seedstack.samples.guitar.domain.model.category.CategoryRepository;
import org.seedstack.seed.transaction.Transactional;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Category resources.
 * Provides category related API endpoints.
 */
@Path("categories")
public class CategoriesResource {

    @Inject
    private CategoryRepository categoryRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional @JpaUnit("appUnit")
    public List<CategoryRepresentation> all(){
        return categoryRepository.all().map(CategoryRepresentation::new).collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional @JpaUnit("appUnit")
    public CategoryRepresentation single(@PathParam("id") String id){
        Optional<Category> optCategory= categoryRepository.single(id);
        if(!optCategory.isPresent()){
            throw new NotFoundException("Category with id "+id+" Not found");
        }
        return new CategoryRepresentation(optCategory.get());
    }



    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional @JpaUnit("appUnit")
    public Response create(CategoryRepresentation category, @Context UriInfo uriInfo){
        Category cat = new Category(category.getTag());
        cat.updateTitle(category.getTitle());
        try {
            Optional<Category> optCat = categoryRepository.create(cat);
            if(optCat.isPresent()) {
                URI newUri = new URI(uriInfo.getRequestUri().toString() + "/" + optCat.get().getId());
                return Response.created(newUri).entity(new CategoryRepresentation(optCat.get())).build();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        catch(IllegalArgumentException | URISyntaxException e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional @JpaUnit("appUnit")
    public CategoryRepresentation update(CategoryRepresentation category){
        Category cat = new Category(category.getTag());
        cat.updateTitle(category.getTitle());
        try {
            Optional<Category> optCat = categoryRepository.updateCategory(cat);
            if(optCat.isPresent()){
                return new CategoryRepresentation(optCat.get());
            }
            throw new NotFoundException("Category "+category.getTag()+" not found for update");
        }
        catch (IllegalArgumentException e){
            throw new NotFoundException("Category "+category.getTag()+" not found for update");
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional @JpaUnit("appUnit")
    public Response delete(@PathParam("id") String id){
        categoryRepository.delete(id);
        return Response.ok().build();
    }
}
