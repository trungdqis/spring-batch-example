package com.trungdq.springbatch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StudentJson {

	private Long id;
	private String firstName;
	private String lastName;
	private String email;
}
