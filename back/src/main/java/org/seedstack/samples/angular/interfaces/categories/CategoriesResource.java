package org.seedstack.samples.angular.interfaces.categories;

import org.seedstack.jpa.JpaUnit;
import org.seedstack.samples.angular.domain.model.category.Category;
import org.seedstack.samples.angular.domain.services.category.CategoryService;
import org.seedstack.seed.transaction.Transactional;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("categories")
public class CategoriesResource {

    @Inject
    private CategoryService categoryService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional @JpaUnit("appUnit")
    public List<CategoryRepresentation> all(){
        return categoryService.all().map(CategoryRepresentation::new).collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional @JpaUnit("appUnit")
    public CategoryRepresentation single(@PathParam("id") String id){
        Optional<Category> optCategory= categoryService.single(id);
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
            Optional<Category> optCat = categoryService.create(cat);
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
            Optional<Category> optCat = categoryService.updateCategory(cat);
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
        categoryService.delete(id);
        return Response.ok().build();
    }
}
