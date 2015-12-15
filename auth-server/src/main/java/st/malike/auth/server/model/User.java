/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package st.malike.auth.server.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author malike_st
 */
@Data
@Document(collection = "oauth2_user")
public class User {
    @Id
    private String id;
    private String email;
    private String password;
    private List<String> rights = new ArrayList<>();

    @CreatedDate
    private Date dateCreated;
}
