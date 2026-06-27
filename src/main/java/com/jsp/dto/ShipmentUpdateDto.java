package com.jsp.dto;

import com.jsp.entity.ShipmentStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShipmentUpdateDto {
	private Integer shipmentId;
	private String location;
	private ShipmentStatus status;
	private String remarks;
}
