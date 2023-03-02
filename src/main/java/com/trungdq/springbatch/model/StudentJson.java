package com.trungdq.springbatch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentJson {

	private Long id;
	private String firstName;
	private String lastName;
	private String email;
}
