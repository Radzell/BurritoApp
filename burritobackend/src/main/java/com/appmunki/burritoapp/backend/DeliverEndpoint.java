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
        name = "deliverApi",
        version = "v1",
        resource = "deliver",
        namespace = @ApiNamespace(
                ownerDomain = "backend.burritoapp.appmunki.com",
                ownerName = "backend.burritoapp.appmunki.com",
                packagePath = ""
        )
)
public class DeliverEndpoint {

    private static final Logger logger = Logger.getLogger(DeliverEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        OfyService.register(Deliver.class);
    }

    /**
     * Returns the {@link Deliver} with the corresponding ID.
     *
     * @param uid the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Deliver} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "deliver/{uid}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Deliver get(@Named("uid") String uid) throws NotFoundException {
        logger.info("Getting Deliver with ID: " + uid);
        Deliver deliver = ofy().load().type(Deliver.class).id(uid).now();
        if (deliver == null) {
            throw new NotFoundException("Could not find Deliver with ID: " + uid);
        }
        return deliver;
    }

    /**
     * Inserts a new {@code Deliver}.
     */
    @ApiMethod(
            name = "insert",
            path = "deliver",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Deliver insert(Deliver deliver) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that deliver.uid has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(deliver).now();
        logger.info("Created Deliver.");

        return ofy().load().entity(deliver).now();
    }

    /**
     * Updates an existing {@code Deliver}.
     *
     * @param uid     the ID of the entity to be updated
     * @param deliver the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code uid} does not correspond to an existing
     *                           {@code Deliver}
     */
    @ApiMethod(
            name = "update",
            path = "deliver/{uid}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Deliver update(@Named("uid") String uid, Deliver deliver) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(uid);
        ofy().save().entity(deliver).now();
        logger.info("Updated Deliver: " + deliver);
        return ofy().load().entity(deliver).now();
    }

    /**
     * Deletes the specified {@code Deliver}.
     *
     * @param uid the ID of the entity to delete
     * @throws NotFoundException if the {@code uid} does not correspond to an existing
     *                           {@code Deliver}
     */
    @ApiMethod(
            name = "remove",
            path = "deliver/{uid}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("uid") String uid) throws NotFoundException {
        checkExists(uid);
        ofy().delete().type(Deliver.class).id(uid).now();
        logger.info("Deleted Deliver with ID: " + uid);
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
            path = "deliver",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Deliver> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<Deliver> query = ofy().load().type(Deliver.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<Deliver> queryIterator = query.iterator();
        List<Deliver> deliverList = new ArrayList<Deliver>(limit);
        while (queryIterator.hasNext()) {
            deliverList.add(queryIterator.next());
        }
        return CollectionResponse.<Deliver>builder().setItems(deliverList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(String uid) throws NotFoundException {
        try {
            ofy().load().type(Deliver.class).id(uid).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Deliver with ID: " + uid);
        }
    }
}