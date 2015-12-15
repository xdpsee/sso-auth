/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package st.malike.auth.server.service.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import st.malike.auth.server.model.AccessToken;
import st.malike.auth.server.model.RefreshToken;
import st.malike.auth.server.repository.AccessTokenRepository;
import st.malike.auth.server.repository.RefreshTokenRepository;

/**
 *
 * @author malike_st
 */
public class TokenStoreService implements TokenStore {

    @Autowired
    private AccessTokenRepository oAuth2AccessTokenRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    private final AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        return readAuthentication(token.getValue());
    }

    @Override
    public OAuth2Authentication readAuthentication(String tokenId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tokenId").is(tokenId));
        AccessToken token = mongoTemplate.findOne(query, AccessToken.class, "oauth2_access_token");
        return null == token ? null : token.getAuthentication();
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        AccessToken accessToken = new AccessToken(token,
                authentication, authenticationKeyGenerator.extractKey(authentication));
        mongoTemplate.save(accessToken);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tokenId").is(tokenId));
        AccessToken token = mongoTemplate.findOne(query, AccessToken.class, "oauth2_access_token");
        if (null == token) {
            throw new InvalidTokenException("Token not valid");
        }
        return token.getOAuth2AccessToken();
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken accessToken) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tokenId").is(accessToken.getValue()));
        AccessToken token = mongoTemplate.findOne(query, AccessToken.class, "oauth2_access_token");
        if (token != null) {
            oAuth2AccessTokenRepository.delete(token);
        }
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        refreshTokenRepository.save(new RefreshToken(refreshToken, authentication));
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String accessToken) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tokenId").is(accessToken));
        RefreshToken token = mongoTemplate.findOne(query, RefreshToken.class, "oauth2_refresh_token");
        return token.getOAuth2RefreshToken();
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tokenId").is(token.getValue()));
        RefreshToken auth2AuthenticationRefreshToken = mongoTemplate.findOne(query, RefreshToken.class, "oauth2_refresh_token");
        return auth2AuthenticationRefreshToken.getAuthentication();
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken accessToken) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tokenId").is(accessToken.getValue()));
        RefreshToken token = mongoTemplate.findOne(query, RefreshToken.class, "oauth2_refresh_token");
        if (token != null) {
            refreshTokenRepository.delete(token);
        }
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        Query query = new Query();
        query.addCriteria(Criteria.where("refreshToken").is(refreshToken.getValue()));
        AccessToken token = mongoTemplate.findOne(query, AccessToken.class, "oauth2_access_token");
        if (token != null) {
            oAuth2AccessTokenRepository.delete(token);
        }
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        String authenticationId = authenticationKeyGenerator.extractKey(authentication);
        if (null == authenticationId) {
            return null;
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("authenticationId").is(authenticationId));
        AccessToken token = mongoTemplate.findOne(query, AccessToken.class, "oauth2_access_token");
        return token == null ? null : token.getOAuth2AccessToken();
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("clientId").is(clientId));
        List<AccessToken> accessTokens = mongoTemplate.find(query, AccessToken.class, "oauth2_access_token");
        return extractAccessTokens(accessTokens);
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("clientId").is(clientId));
        query.addCriteria(Criteria.where("userName").is(userName));
        List<AccessToken> accessTokens = mongoTemplate.find(query, AccessToken.class, "oauth2_access_token");
        return extractAccessTokens(accessTokens);
    }

    private Collection<OAuth2AccessToken> extractAccessTokens(List<AccessToken> tokens) {
        List<OAuth2AccessToken> accessTokens = new ArrayList<>();

        for (AccessToken token : tokens) {
            accessTokens.add(token.getOAuth2AccessToken());
        }

        return accessTokens;
    }

}
