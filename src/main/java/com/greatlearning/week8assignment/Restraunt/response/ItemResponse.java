package com.greatlearning.week8assignment.Restraunt.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemResponse {
	
	private String name;
	private Double price;
	private Integer quantity;

}
