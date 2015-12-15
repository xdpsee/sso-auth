/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package st.malike.auth.server.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import st.malike.auth.server.model.RefreshToken;

/**
 *
 * @author malike_st
 */

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {

   
}