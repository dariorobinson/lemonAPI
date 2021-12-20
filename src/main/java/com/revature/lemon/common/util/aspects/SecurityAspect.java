package com.revature.lemon.common.util.aspects;

import com.revature.lemon.auth.TokenService;
import com.revature.lemon.common.exceptions.AuthenticationException;
import com.revature.lemon.common.exceptions.AuthorizationException;
import com.revature.lemon.common.util.RoleType;
import com.revature.lemon.common.util.web.Secured;
import com.revature.lemon.user.User;
import com.revature.lemon.userplaylist.UserPlaylist;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Aspect
@Component
public class SecurityAspect {

    Logger logger = LogManager.getLogger();
    TokenService tokenService;

    public SecurityAspect(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * For methods with @Authenticated annotation, check if the user is logged in to access it.
     */
    @Order(1)
    @Before("@annotation(com.revature.lemon.common.util.web.Authenticated)")
    public void requireAuthentication() {
        if(!sessionExists()) {
            throw new AuthenticationException("No session token found");
        }
    }

    /**
     * Helper method to get the current session
     */

    private boolean sessionExists() {
        String token = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getHeader("Authorization");
        System.out.println(token);

        return tokenService.isTokenValid(token);
    }

    /**
     * For methods with the @Secured annotation, check if the current user has the correct role to access method
     * todo leave all the print statements for testing later, still haven't tested if it works for editors and viewers
     * @param jp
     */
    /*
    @Before("@annotation(com.revature.lemon.common.util.web.Secured)")
    public void requireCreator(JoinPoint jp) {
        if(!sessionExists()) {
            throw new AuthenticationException("No current session found");
        }
        String playlistId = "";
        List<Parameter> parameters = Arrays.asList(((MethodSignature) jp.getSignature()).getMethod().getParameters());
        for(int i=0; i<parameters.size(); i++) {
            if(parameters.get(i).getName().equals("playlistId")) {
                playlistId = jp.getArgs()[i].toString();
            }
        }
        System.out.println(playlistId);

        Secured annotation = getAnnotationFromJoinPoint(jp, Secured.class);
        List<RoleType> allowedRoles = Arrays.asList(annotation.allowedAccountTypes());

        String token = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getHeader("Authorization");

        //HttpSession session = getCurrentSessionIfExist().orElseThrow(() -> new AuthenticationException("No session found."));

        User requester = ((User) session.getAttribute("authUser"));
        //String playlistId = annotation.playlistId();

        //Iterate through the user's playlists and check to see if any of their playlists match the id of current playlist, then check if they are creator or not
        List<UserPlaylist> list = requester.getPlaylistRole();
        if(list != null) {
            for (int i = 0; i < list.size(); i++) {
                System.out.println("Looking for playlists");
                UserPlaylist playlist = list.get(i);
                System.out.println("playlist = " + playlist.getId().getPlaylistId());
                System.out.println("finding: " + playlistId);
                String playlistKeyId = playlist.getId().getPlaylistId();
                if (playlistKeyId.equals(playlistId)) {
                    System.out.println("Found playlist");
                    for (RoleType role : allowedRoles) {
                        System.out.println("Roles " + role);
                        if (requester.getPlaylistRole().get(i).getUserRole().equals(role)) {
                            logger.info("User has role: ${} and is authorized to do this action", role);
                            return;
                        }
                    }
                }
            }
        }
        logger.warn("User is not authorized to do this action");
        throw new AuthorizationException("You are not authorized to do this");
    }
    */

    private <T extends Annotation> T getAnnotationFromJoinPoint(JoinPoint jp, Class<T> annotationType) {
        return ((MethodSignature) jp.getSignature()).getMethod().getAnnotation(annotationType);
    }
}
