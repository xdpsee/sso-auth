/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package st.malike.auth.server.model;

import java.io.Serializable;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 *
 * @author malike_st
 */
@Data
@Document(collection = "oauth2_refresh_token")
public class RefreshToken implements Serializable {

    @Indexed
    private String id;
    private final String tokenId;
    private final OAuth2RefreshToken oAuth2RefreshToken;
    private final OAuth2Authentication authentication;

    public RefreshToken(OAuth2RefreshToken oAuth2RefreshToken, OAuth2Authentication authentication) {
        this.oAuth2RefreshToken = oAuth2RefreshToken;
        this.authentication = authentication;
        this.tokenId = oAuth2RefreshToken.getValue();
    }

}
