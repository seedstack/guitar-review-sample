package org.seedstack.samples.angular.interfaces.products;

import com.google.common.io.Files;
import org.seedstack.jpa.JpaUnit;
import org.seedstack.samples.angular.domain.model.product.Product;
import org.seedstack.samples.angular.domain.services.product.ProductService;
import org.seedstack.seed.Configuration;
import org.seedstack.seed.Logging;
import org.seedstack.seed.transaction.Transactional;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("products")
public class ProductsResource {

    @Inject
    private ProductService productService;

    @Configuration("application.imageFolder")
    private String imagesDirectory;

    @Logging
    private Logger logger;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional @JpaUnit("appUnit")
    public List<ProductRepresentation> all(){
        return productService.all().map(ProductRepresentation::new).collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional @JpaUnit("appUnit")
    public ProductRepresentation getSingle(@PathParam("id") String id){
        Optional<Product> product=productService.single(id);
        if(!product.isPresent()){
           throw new NotFoundException("Product "+id+" not found.");
        }
        return new ProductRepresentation(product.get());
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional @JpaUnit("appUnit")
    public List<ProductRepresentation> search(@QueryParam("term") String term){
        return  productService.search(term).map(ProductRepresentation::new).collect(Collectors.toList());
    }

    @GET
    @Path("/category/{idCategory}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional @JpaUnit("appUnit")
    public List<ProductRepresentation> searChByCategory(@PathParam("idCategory") String tagCategory){
        return productService.searchByCategory(tagCategory).map(ProductRepresentation::new).collect(Collectors.toList());

    }

    @PUT
    @Path("/{idProduct}/categories/{idCategory}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional @JpaUnit("appUnit")
    public ProductRepresentation addCategoryToProduct(@PathParam("idProduct") String idProduct, @PathParam("idCategory") String idCategory){
        try {
            Optional<Product> optProduct = productService.addCategory(idProduct, idCategory);
            if (!optProduct.isPresent()) {
                throw new NotFoundException("Product " + idProduct + " not found.");
            }
            return new ProductRepresentation(optProduct.get());
        }
        catch (IllegalArgumentException ex){
            throw new NotFoundException(ex.getMessage());
        }
    }

    @GET
    @Path("/{idProduct}/image")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Transactional @JpaUnit("appUnit")
    public Response getProductImage(@PathParam("idProduct") String idProduct){
        File imageFile = new File(imagesDirectory+idProduct+".jpg");
        if(!imageFile.exists()){
            return Response.status(Response.Status.NOT_FOUND.getStatusCode(),"Product image not found").build();
        }
        return Response.ok(imageFile, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + imageFile.getName() + "\"")
                .build();
    }

    @POST
    @Path("/{idProduct}/image")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Transactional @JpaUnit("appUnit")
    public Response uploadImage(@PathParam("idProduct") String idProduct,byte[] messageBody){
        File imageFile=new File(imagesDirectory+idProduct+".jpg");

        try{
            Files.write(messageBody, imageFile);
        }
        catch (IOException ioe){
         logger.error("Could not write image file for product {}", idProduct);
         return Response.serverError().build();
        }
        return Response.ok().build();
    }

    @DELETE
    @Path("/{idProduct}/categories/{idCategory}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional @JpaUnit("appUnit")
    public ProductRepresentation removeCategory(@PathParam("idProduct") String idProduct, @PathParam("idCategory") String idCategory){
        try {
            Optional<Product> optProduct = productService.removeCategory(idProduct, idCategory);
            if (!optProduct.isPresent()) {
                throw new NotFoundException("Product " + idProduct + " not found.");
            }
            return new ProductRepresentation(optProduct.get());
        }
        catch (IllegalArgumentException ex){
            throw new NotFoundException(ex.getMessage());
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional @JpaUnit("appUnit")
    public ProductRepresentation create(ProductRepresentation newProduct){
        try {
            Optional<Product> product = productService.create(newProduct);
            if (product.isPresent()) {
                return new ProductRepresentation(product.get());
            }
            throw new InternalServerErrorException("Problem while creating a product");
        }
        catch (IllegalArgumentException iae){
            throw new BadRequestException(iae.getMessage());
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional @JpaUnit("appUnit")
    public ProductRepresentation update(ProductRepresentation updatedProduct){
        try {
            Optional<Product> productOpt = productService.update(updatedProduct);
            if (!productOpt.isPresent()) {
                throw new NotFoundException("Product with ID :" + updatedProduct.getId() + " Not found");
            }
            return new ProductRepresentation(productOpt.get());
        }
        catch (IllegalArgumentException iae){
            throw new BadRequestException(iae.getMessage());
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional @JpaUnit("appUnit")
    public Response delete(@PathParam("id") String id){
        try{
            productService.delete(id);
            return Response.ok().build();
        }
        catch(IllegalArgumentException iae){
            throw new NotFoundException(iae.getMessage());
        }
    }

}
