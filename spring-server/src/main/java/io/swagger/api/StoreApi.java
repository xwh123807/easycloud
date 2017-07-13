package io.swagger.api;

import io.swagger.model.Body2;
import io.swagger.model.InlineResponse2003;
import java.util.Map;

import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-06-30T06:26:34.006Z")

@Api(value = "store", description = "the store API")
public interface StoreApi {

    @ApiOperation(value = "Delete purchase order by ID", notes = "For valid response try integer IDs with positive integer value.         Negative or non-integer values will generate API errors", response = Void.class, tags={ "store", })
    @ApiResponses(value = { 
        @ApiResponse(code = 400, message = "Invalid ID supplied", response = Void.class),
        @ApiResponse(code = 404, message = "Order not found", response = Void.class) })
    @RequestMapping(value = "/store/order/{orderId}",
        produces = { "application/xml", "application/json" }, 
        method = RequestMethod.DELETE)
    ResponseEntity<Void> deleteOrder( @Min(1)@ApiParam(value = "ID of the order that needs to be deleted",required=true ) @PathVariable("orderId") Long orderId);


    @ApiOperation(value = "Returns pet inventories by status", notes = "Returns a map of status codes to quantities", response = Integer.class, responseContainer = "Map", authorizations = {
        @Authorization(value = "api_key")
    }, tags={ "store", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = Integer.class) })
    @RequestMapping(value = "/store/inventory",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<Map<String, Integer>> getInventory();


    @ApiOperation(value = "Find purchase order by ID", notes = "For valid response try integer IDs with value >= 1 and <= 10.         Other values will generated exceptions", response = InlineResponse2003.class, tags={ "store", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = InlineResponse2003.class),
        @ApiResponse(code = 400, message = "Invalid ID supplied", response = InlineResponse2003.class),
        @ApiResponse(code = 404, message = "Order not found", response = InlineResponse2003.class) })
    @RequestMapping(value = "/store/order/{orderId}",
        produces = { "application/xml", "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<InlineResponse2003> getOrderById( @Min(1) @Max(10)@ApiParam(value = "ID of pet that needs to be fetched",required=true ) @PathVariable("orderId") Long orderId);


    @ApiOperation(value = "Place an order for a pet", notes = "", response = InlineResponse2003.class, tags={ "store", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = InlineResponse2003.class),
        @ApiResponse(code = 400, message = "Invalid Order", response = InlineResponse2003.class) })
    @RequestMapping(value = "/store/order",
        produces = { "application/xml", "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<InlineResponse2003> placeOrder(@ApiParam(value = "order placed for purchasing the pet" ,required=true ) @RequestBody Body2 body);

}
