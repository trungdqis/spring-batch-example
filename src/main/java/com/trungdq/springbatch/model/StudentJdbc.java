package com.trungdq.springbatch.model;

import lombok.Data;

@Data
public class StudentJdbc {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
