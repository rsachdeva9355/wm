package com.rtv.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Address resource. You can create addresses in lieu of an address token which
 * can thereafter be retrieved to fetch the address back. The system asynchronously geocodes
 * the address into geographic coordinates (like latitude 30.423021 and longitude 72.083739).
 * The system can also intelligently dedupe addresses.
 *
 * @author bhupi
 */
@Path("addresses")
@Api(value = "addresses")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DemoResource {

    private static final Logger LOG = LoggerFactory.getLogger(DemoResource.class);

    private final Datastore store;
    private ObjectMapper objectMapper;

    public DemoResource(
        final Datastore store, ObjectMapper objectMapper)
    {
        this.store = store;
        this.objectMapper = objectMapper;
    }

//    @POST
//    @Timed
//    @ApiOperation(value = "Create a new address")
//    @Produces(MediaType.APPLICATION_JSON)
//    public @Valid Address create(
//        @Valid
//        @NotNull
//        @ApiParam("address to be stored")
//        Address address,
//        @QueryParam("dedupe")
//        @DefaultValue("false")
//        @ApiParam("true if an attempt to dedupe the address should be made")
//        BooleanParam tryDedupe)
//    {
//        DemoDO addressDO;
//        validateAddresses(Collections.singletonList(address));
//        if (tryDedupe.get()) {
//            Address duplicateAddress = findDuplicateAddress(address);
//            if(null != duplicateAddress){
//                return duplicateAddress;
//            }
//        }
//
//        // Unable to or not required to dedupe
//        addressDO = new DemoDO();
//        addressDO.setId(new ObjectId().toString());
//        address.setId(addressDO.getId());
//        addressDO.setHierarchical(address.getHierarchical());
//        addressDO.setContact(address.getContact());
//        addressDO.setDedupeId(buildDedupeId(address));
//        return populateGeometry(address, addressDO);
//    }
}
