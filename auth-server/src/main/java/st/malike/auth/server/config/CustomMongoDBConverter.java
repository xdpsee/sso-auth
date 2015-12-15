/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package st.malike.auth.server.config;

import com.mongodb.DBObject;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import st.malike.auth.server.model.User;
import st.malike.auth.server.service.security.ClientDetailService;
import st.malike.auth.server.service.security.UserAuthConfigService;

/**
 * 
 *
 * @author malike_st
 */
@Configuration
public class CustomMongoDBConverter implements Converter<DBObject, OAuth2Authentication> {

    @Autowired
    private UserAuthConfigService authConfigService;
    @Autowired
    private ClientDetailService clientDetailService;
    
   

    @Override
    public OAuth2Authentication convert(DBObject source) {
        DBObject storedRequest = (DBObject) source.get("storedRequest");
        OAuth2Request oAuth2Request = new OAuth2Request((Map<String, String>) storedRequest.get("requestParameters"),
                (String) storedRequest.get("clientId"), null, true, new HashSet((List) storedRequest.get("scope")),
                null, null, null, null);
        DBObject userAuthorization = (DBObject) source.get("userAuthentication");
        if (null != userAuthorization) { //its a user
            Object principal = userAuthorization.get("principal");
            User user = null;
            if ((null != principal) && principal instanceof String) {
                user = authConfigService.getUser((String) principal);
            } else if (null != principal) {
                DBObject principalDBO = (DBObject) principal;
                String email = (String) principalDBO.get("username");
                user = authConfigService.getUser(email);
            }
            if (null == user) {
                return null;
            }

            Authentication userAuthentication = new UserAuthenticationToken(user.getEmail()
                    , userAuthorization.get("credentials")
                    , authConfigService.getRights(user));
            return new OAuth2Authentication(oAuth2Request, userAuthentication);
        } else { //its a client
            Object clientId = storedRequest.get("clientId");
            ClientDetails client = null;
            if ((null != clientId) && clientId instanceof String) {
                client = clientDetailService.loadClientByClientId((String)clientId);
            }

            if (null == client) {
                return null;
            }

            Authentication userAuthentication = new ClientAuthenticationToken(client.getClientId(),
                    null, client.getAuthorities());
            return new OAuth2Authentication(oAuth2Request, userAuthentication);
        }
    }

    @Bean
    public CustomConversions customConversions() {
        List<Converter<?, ?>> converterList = new ArrayList<>();
        converterList.add(this);
        return new CustomConversions(converterList);
    }
}
