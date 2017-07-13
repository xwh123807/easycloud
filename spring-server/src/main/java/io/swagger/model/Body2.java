package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.joda.time.DateTime;
import javax.validation.constraints.*;
/**
 * Body2
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-06-30T06:26:34.006Z")

public class Body2   {
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("petId")
  private Long petId = null;

  @JsonProperty("quantity")
  private Integer quantity = null;

  @JsonProperty("shipDate")
  private DateTime shipDate = null;

  /**
   * Order Status
   */
  public enum StatusEnum {
    PLACED("placed"),
    
    APPROVED("approved"),
    
    DELIVERED("delivered");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String text) {
      for (StatusEnum b : StatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("status")
  private StatusEnum status = null;

  @JsonProperty("complete")
  private Boolean complete = false;

  public Body2 id(Long id) {
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @ApiModelProperty(value = "")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Body2 petId(Long petId) {
    this.petId = petId;
    return this;
  }

   /**
   * Get petId
   * @return petId
  **/
  @ApiModelProperty(value = "")
  public Long getPetId() {
    return petId;
  }

  public void setPetId(Long petId) {
    this.petId = petId;
  }

  public Body2 quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

   /**
   * Get quantity
   * @return quantity
  **/
  @ApiModelProperty(value = "")
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public Body2 shipDate(DateTime shipDate) {
    this.shipDate = shipDate;
    return this;
  }

   /**
   * Get shipDate
   * @return shipDate
  **/
  @ApiModelProperty(value = "")
  public DateTime getShipDate() {
    return shipDate;
  }

  public void setShipDate(DateTime shipDate) {
    this.shipDate = shipDate;
  }

  public Body2 status(StatusEnum status) {
    this.status = status;
    return this;
  }

   /**
   * Order Status
   * @return status
  **/
  @ApiModelProperty(value = "Order Status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public Body2 complete(Boolean complete) {
    this.complete = complete;
    return this;
  }

   /**
   * Get complete
   * @return complete
  **/
  @ApiModelProperty(value = "")
  public Boolean getComplete() {
    return complete;
  }

  public void setComplete(Boolean complete) {
    this.complete = complete;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Body2 body2 = (Body2) o;
    return Objects.equals(this.id, body2.id) &&
        Objects.equals(this.petId, body2.petId) &&
        Objects.equals(this.quantity, body2.quantity) &&
        Objects.equals(this.shipDate, body2.shipDate) &&
        Objects.equals(this.status, body2.status) &&
        Objects.equals(this.complete, body2.complete);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, petId, quantity, shipDate, status, complete);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Body2 {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    petId: ").append(toIndentedString(petId)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    shipDate: ").append(toIndentedString(shipDate)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    complete: ").append(toIndentedString(complete)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

