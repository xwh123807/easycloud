package org.easycloud.metadata.domain.api;

import org.easycloud.metadata.domain.model.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-03-08T01:55:37.567Z")

@Api(value = "stores", description = "the stores API")
public interface StoresApi {

    @ApiOperation(value = "Delete purchase order by ID", notes = "For valid response try integer IDs with value < 1000. Anything above 1000 or nonintegers will generate API errors", response = Void.class, tags={ "store", })
    @ApiResponses(value = { 
        @ApiResponse(code = 400, message = "Invalid ID supplied", response = Void.class),
        @ApiResponse(code = 404, message = "Order not found", response = Void.class) })
    @RequestMapping(value = "/stores/order/{orderId}",
        produces = { "application/json", "application/xml" }, 
        method = RequestMethod.DELETE)
    ResponseEntity<Void> deleteOrder(@ApiParam(value = "ID of the order that needs to be deleted",required=true ) @PathVariable("orderId") String orderId);


    @ApiOperation(value = "Find purchase order by ID", notes = "For valid response try integer IDs with value <= 5 or > 10. Other values will generated exceptions", response = Order.class, tags={ "store", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = Order.class),
        @ApiResponse(code = 400, message = "Invalid ID supplied", response = Order.class),
        @ApiResponse(code = 404, message = "Order not found", response = Order.class) })
    @RequestMapping(value = "/stores/order/{orderId}",
        produces = { "application/json", "application/xml" }, 
        method = RequestMethod.GET)
    ResponseEntity<Order> getOrderById(@ApiParam(value = "ID of pet that needs to be fetched",required=true ) @PathVariable("orderId") String orderId);


    @ApiOperation(value = "Place an order for a pet", notes = "", response = Order.class, tags={ "store", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = Order.class),
        @ApiResponse(code = 400, message = "Invalid Order", response = Order.class) })
    @RequestMapping(value = "/stores/order",
        produces = { "application/json", "application/xml" }, 
        method = RequestMethod.POST)
    ResponseEntity<Order> placeOrder(@ApiParam(value = "order placed for purchasing the pet"  ) @RequestBody Order body);

}
