package org.acme.com.resource;

import java.time.LocalDate;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.acme.com.model.User;
import org.acme.com.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Path("user")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class UserRepositoryResource {

    @Inject
    UserRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoryResource.class.getName());

    @GET
    public List<User> get() {
        return userRepository.listAll();
    }

    @GET
    @Path("{id}")
    public User getSingle(Long id) {
      User entity = userRepository.findById(id);
        if (entity == null) {
            throw new WebApplicationException("User with id of " + id + " does not exist.", 404);
        }
        return entity;
    }

    @POST
    @Transactional
    public Response create(User user) {
        user.setCreateData(LocalDate.now());
        userRepository.persist(user);
        return Response.ok(user).status(201).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public User update(Long id, User user) {
        if (user.getName() == null) {
            throw new WebApplicationException("User was not set on request.", 422);
        }

        User entity = userRepository.findById(id);
        if (entity == null) {
            throw new WebApplicationException("User with id of " + id + " does not exist.", 404);
        }

        entity.setName(user.getName());
        entity.setPassword(user.getPassword());  
        entity.setIsAdmin(user.getIsAdmin());
        return entity;
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(Long id) {
      User entity = userRepository.findById(id);
      if (entity == null) {
          throw new WebApplicationException("User with id of " + id + " does not exist.", 404);
      }

      userRepository.delete(entity);
      return Response.status(204).build();
    }

    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {

        @Inject
        ObjectMapper objectMapper;

        @Override
        public Response toResponse(Exception exception) {
            LOGGER.error("Failed to handle request", exception);

            int code = 500;
            if (exception instanceof WebApplicationException) {
                code = ((WebApplicationException) exception).getResponse().getStatus();
            }

            ObjectNode exceptionJson = objectMapper.createObjectNode();
            exceptionJson.put("exceptionType", exception.getClass().getName());
            exceptionJson.put("code", code);

            if (exception.getMessage() != null) {
                exceptionJson.put("error", exception.getMessage());
            }

            return Response.status(code)
                    .entity(exceptionJson)
                    .build();
        }

    }
}