package org.example.filter;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.example.auth.JwtAuthorizationController;

import java.io.IOException;

@SuppressWarnings("unused")
@Provider
@ModifyOwnProfile
@Priority(Priorities.AUTHORIZATION)
public class OwnResourceFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String token = AuthenticationFilter.CheckAuthenticationHeader(containerRequestContext);
        String username = containerRequestContext.getUriInfo().getPathParameters().getFirst("username");
        String claim = JwtAuthorizationController.getInstance().getUsernameClaim(token);

        if(username != null && claim != null) {
            if(username.equals(claim)) {
                System.out.println("User and claim are matching");
            }
            else {
                System.out.println("User and claim are NOT matching");
                containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }
        }

    }
}
