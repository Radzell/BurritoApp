package com.appmunki.burritoapp.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p/>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
@Api(
        name = "dishApi",
        version = "v1",
        resource = "dish",
        namespace = @ApiNamespace(
                ownerDomain = "backend.burritoapp.appmunki.com",
                ownerName = "backend.burritoapp.appmunki.com",
                packagePath = ""
        )
)
public class DishEndpoint {

    private static final Logger logger = Logger.getLogger(DishEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        OfyService.register(Dish.class);
    }

    /**
     * Returns the {@link Dish} with the corresponding ID.
     *
     * @param uid the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Dish} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "dish/{uid}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Dish get(@Named("uid") String uid) throws NotFoundException {
        logger.info("Getting Dish with ID: " + uid);
        Dish dish = ofy().load().type(Dish.class).id(uid).now();
        if (dish == null) {
            throw new NotFoundException("Could not find Dish with ID: " + uid);
        }
        return dish;
    }

    /**
     * Inserts a new {@code Dish}.
     */
    @ApiMethod(
            name = "insert",
            path = "dish",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Dish insert(Dish dish) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that dish.uid has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        dish.generateUID();
        ofy().save().entity(dish).now();
        logger.info("Created Dish.");

        return ofy().load().entity(dish).now();
    }

    /**
     * Updates an existing {@code Dish}.
     *
     * @param uid  the ID of the entity to be updated
     * @param dish the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code uid} does not correspond to an existing
     *                           {@code Dish}
     */
    @ApiMethod(
            name = "update",
            path = "dish/{uid}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Dish update(@Named("uid") String uid, Dish dish) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(uid);
        ofy().save().entity(dish).now();
        logger.info("Updated Dish: " + dish);
        return ofy().load().entity(dish).now();
    }

    /**
     * Deletes the specified {@code Dish}.
     *
     * @param uid the ID of the entity to delete
     * @throws NotFoundException if the {@code uid} does not correspond to an existing
     *                           {@code Dish}
     */
    @ApiMethod(
            name = "remove",
            path = "dish/{uid}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("uid") String uid) throws NotFoundException {
        checkExists(uid);
        ofy().delete().type(Dish.class).id(uid).now();
        logger.info("Deleted Dish with ID: " + uid);
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "list",
            path = "dish",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Dish> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<Dish> query = ofy().load().type(Dish.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<Dish> queryIterator = query.iterator();
        List<Dish> dishList = new ArrayList<Dish>(limit);
        while (queryIterator.hasNext()) {
            dishList.add(queryIterator.next());
        }
        return CollectionResponse.<Dish>builder().setItems(dishList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(String uid) throws NotFoundException {
        try {
            ofy().load().type(Dish.class).id(uid).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Dish with ID: " + uid);
        }
    }
}