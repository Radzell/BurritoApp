package com.appmunki.burritoapp.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.UnauthorizedException;
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
        name = "userApi",
        version = "v1",
        resource = "user",
        namespace = @ApiNamespace(
                ownerDomain = "backend.burritoapp.appmunki.com",
                ownerName = "backend.burritoapp.appmunki.com",
                packagePath = ""
        )
)
public class UserEndpoint {

    private static final Logger logger = Logger.getLogger(UserEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(User.class);
    }

    /**
     * Returns the {@link User} with the corresponding credentials.
     *
     * @param userJson the the entity to be retrieved
     * @return the entity with the corresponding credentials
     * @throws UnauthorizedException if there is no {@code User} with the provided credentials.
     */
    @ApiMethod(
            name = "login",
            path = "user/login",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Resource login(Resource userJson) throws UnauthorizedException, BadRequestException {
        User user = User.fromJson(userJson.data);
        logger.info("Logging in User with Number: " + user.getNumber()+" and "+user.getPassword());
        user = checkCredentials(user);
        user.generateToken();
        ofy().save().entity(user).now();
        Resource results = user.toResource();
        return results;
    }

    /**
     * Signups a new {@code User}.
     */
    @ApiMethod(
            name = "signup",
            path = "user/signup",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Resource signup(Resource userJson) throws BadRequestException {
        User user = User.fromJson(userJson.data);
        try {
            checkExists(user);
            throw new BadRequestException("User with number: "+user.getNumber()+" already exist");
        } catch (NotFoundException e) {
            user.generateToken();
            user.generateUID();
            logger.info("Created User.");
            ofy().save().entity(user).now();
            Resource results = user.toResource();
            return results;
        }
    }
    /**
     * Deletes the specified {@code User}.
     *
     * @param token the token of the entity to logged
     * @throws NotFoundException if the {@code mId} does not correspond to an existing
     *                           {@code User}
     */
    @ApiMethod(
            name = "logout",
            path = "user/logout",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void logout(@Named("token") String token) throws NotFoundException, UnauthorizedException {
        User user = checkCredentials(token);
        user.generateToken();
        logger.info("Logout User with Number: " + user.getToken());
    }
    /**
     * Returns the {@link User} with the corresponding ID.
     *
     * @param uid the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code User} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "user/{uid}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public User get(@Named("uid") String uid) throws NotFoundException {
        logger.info("Getting User with ID: " + uid);
        User user = ofy().load().type(User.class).id(uid).now();
        if (user == null) {
            throw new NotFoundException("Could not find User with ID: " + uid);
        }
        return user;
    }

    /**
     * Inserts a new {@code User}.
     */
    @ApiMethod(
            name = "insert",
            path = "user",
            httpMethod = ApiMethod.HttpMethod.POST)
    public User insert(User user) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that user.uid has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(user).now();
        logger.info("Created User.");

        return ofy().load().entity(user).now();
    }

    /**
     * Updates an existing {@code User}.
     *
     * @param uid  the ID of the entity to be updated
     * @param user the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code uid} does not correspond to an existing
     *                           {@code User}
     */
    @ApiMethod(
            name = "update",
            path = "user/{uid}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public User update(@Named("uid") String uid, User user) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(uid);
        ofy().save().entity(user).now();
        logger.info("Updated User: " + user);
        return ofy().load().entity(user).now();
    }

    /**
     * Deletes the specified {@code User}.
     *
     * @param uid the ID of the entity to delete
     * @throws NotFoundException if the {@code uid} does not correspond to an existing
     *                           {@code User}
     */
    @ApiMethod(
            name = "remove",
            path = "user/{uid}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("uid") String uid) throws NotFoundException {
        checkExists(uid);
        ofy().delete().type(User.class).id(uid).now();
        logger.info("Deleted User with ID: " + uid);
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
            path = "user",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<User> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<User> query = ofy().load().type(User.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<User> queryIterator = query.iterator();
        List<User> userList = new ArrayList<User>(limit);
        while (queryIterator.hasNext()) {
            userList.add(queryIterator.next());
        }
        return CollectionResponse.<User>builder().setItems(userList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(String uid) throws NotFoundException {
        try {
            ofy().load().type(User.class).id(uid).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find User with ID: " + uid);
        }
    }


    private User checkCredentials(User user) throws UnauthorizedException {
        try{
            return ofy().load().type(User.class).filter("number =",user.getNumber()).filter("password =",user.getPassword()).first().safe();
        }catch (com.googlecode.objectify.NotFoundException e){
            throw new UnauthorizedException("Could not verify User with Number: " + user.getNumber()+" and Password "+user.getPassword());
        }

    }
    private User checkCredentials(String token) throws UnauthorizedException, NotFoundException {
        try{
            return ofy().load().type(User.class).filter("token =",token).first().safe();
        }catch (com.googlecode.objectify.NotFoundException e){
            throw new NotFoundException("Could not verify User with Token: " + token);
        }

    }
    private User checkExists(User user) throws NotFoundException {
        try {
            return ofy().load().type(User.class).filter("number =",user.getNumber()).first().safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find User with Number: " + user.getNumber());
        }
    }
    private User checkExists(Long mId) throws NotFoundException {
        try {
            return ofy().load().type(User.class).id(mId).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find User with ID: " + mId);
        }
    }
}