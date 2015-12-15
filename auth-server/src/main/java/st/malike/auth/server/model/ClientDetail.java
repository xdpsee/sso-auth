/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package st.malike.auth.server.model;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author malike_st
 */
@Data
@Document(collection = "oauth2_client")
public class ClientDetail {
    @Id
    private String id;
    @Indexed
    private String clientId;
    private Set<String> resourceIds;
    private boolean secretRequired;
    @Indexed
    private String clientSecret;
    private boolean scoped;
    private Set<String> scope;
    private Set<String> authorizedGrantTypes;
    private Set<String> registeredRedirectUri;
    private Collection<String> authorities;
    private Integer accessTokenValiditySeconds;
    private Integer refreshTokenValiditySeconds;
    private boolean autoApprove;
    private Map<String, Object> additionalInformation;
}
