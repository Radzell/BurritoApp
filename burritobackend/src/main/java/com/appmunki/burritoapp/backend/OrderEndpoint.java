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
        name = "orderApi",
        version = "v1",
        resource = "order",
        namespace = @ApiNamespace(
                ownerDomain = "backend.burritoapp.appmunki.com",
                ownerName = "backend.burritoapp.appmunki.com",
                packagePath = ""
        )
)
public class OrderEndpoint {

    private static final Logger logger = Logger.getLogger(OrderEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        OfyService.register(Order.class);
    }

    /**
     * Returns the {@link Order} with the corresponding ID.
     *
     * @param uid the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Order} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "order/{uid}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Order get(@Named("uid") String uid) throws NotFoundException {
        logger.info("Getting Order with ID: " + uid);
        Order order = ofy().load().type(Order.class).id(uid).now();
        if (order == null) {
            throw new NotFoundException("Could not find Order with ID: " + uid);
        }
        return order;
    }

    /**
     * Inserts a new {@code Order}.
     */
    @ApiMethod(
            name = "insert",
            path = "order",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Order insert(Order order) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that order.uid has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(order).now();
        logger.info("Created Order.");

        return ofy().load().entity(order).now();
    }

    /**
     * Updates an existing {@code Order}.
     *
     * @param uid   the ID of the entity to be updated
     * @param order the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code uid} does not correspond to an existing
     *                           {@code Order}
     */
    @ApiMethod(
            name = "update",
            path = "order/{uid}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Order update(@Named("uid") String uid, Order order) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(uid);
        ofy().save().entity(order).now();
        logger.info("Updated Order: " + order);
        return ofy().load().entity(order).now();
    }

    /**
     * Deletes the specified {@code Order}.
     *
     * @param uid the ID of the entity to delete
     * @throws NotFoundException if the {@code uid} does not correspond to an existing
     *                           {@code Order}
     */
    @ApiMethod(
            name = "remove",
            path = "order/{uid}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("uid") String uid) throws NotFoundException {
        checkExists(uid);
        ofy().delete().type(Order.class).id(uid).now();
        logger.info("Deleted Order with ID: " + uid);
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
            path = "order",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Order> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<Order> query = ofy().load().type(Order.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<Order> queryIterator = query.iterator();
        List<Order> orderList = new ArrayList<Order>(limit);
        while (queryIterator.hasNext()) {
            orderList.add(queryIterator.next());
        }
        return CollectionResponse.<Order>builder().setItems(orderList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(String uid) throws NotFoundException {
        try {
            ofy().load().type(Order.class).id(uid).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Order with ID: " + uid);
        }
    }
}