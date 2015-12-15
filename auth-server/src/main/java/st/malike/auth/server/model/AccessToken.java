/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package st.malike.auth.server.model;

import java.io.Serializable;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 *
 * @author malike_st
 */
@Data
@Document(collection = "oauth2_access_token")
public class AccessToken implements Serializable {

    @Indexed
    private String id;
    private String tokenId;
    private OAuth2AccessToken oAuth2AccessToken;
    private String authenticationId;
    private String userName;
    private String clientId;
    private OAuth2Authentication authentication;
    private String refreshToken;

    public AccessToken(final OAuth2AccessToken oAuth2AccessToken, final OAuth2Authentication authentication, final String authenticationId) {
        this.tokenId = oAuth2AccessToken.getValue();
        this.oAuth2AccessToken = oAuth2AccessToken;
        this.authenticationId = authenticationId;
        this.userName = authentication.getName();
        this.clientId = authentication.getOAuth2Request().getClientId();
        this.authentication = authentication;

        OAuth2RefreshToken refreshToken = oAuth2AccessToken.getRefreshToken();
        if (refreshToken != null) {
            this.refreshToken = refreshToken.getValue();
        }
    }
}
