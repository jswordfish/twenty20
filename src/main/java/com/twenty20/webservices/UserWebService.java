package com.twenty20.webservices;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.api.client.googleapis.auth.clientlogin.ClientLogin.Response;
import com.twenty20.common.Twenty20Exception;
import com.twenty20.domain.User;

@Path("/userService")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserWebService {
	@Autowired
	com.twenty20.services.UserService userService;
	
	@POST
	@Path("/user/token/{token}")
	@Produces("application/json")
	@Consumes("application/json")
	public javax.ws.rs.core.Response saveOrUpdateUser(User user, @PathParam("token") String token){
		try {
			userService.saveOrUpdate(user);
			return javax.ws.rs.core.Response.ok().build();
		} catch (Twenty20Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return javax.ws.rs.core.Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}
	
	@POST
	@Path("/delete/user/token/{token}")
	@Produces("application/json")
	@Consumes("application/json")
	public javax.ws.rs.core.Response deleteUser(User user, @PathParam("token") String token){
		try {
			User user2 = userService.getUniqueUser(user.getUserName());
				if(user2 != null){
					userService.delete(user2.getId());
				}
			return javax.ws.rs.core.Response.ok().build();
		} catch (Twenty20Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return javax.ws.rs.core.Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}
	
	@POST
	@Path("/users/token/{token}")
	@Produces("application/json")
	@Consumes("application/json")
	public javax.ws.rs.core.Response getAllUsers(User user, @PathParam("token") String token){
		try {
			List<User> users = userService.findAll();
			return javax.ws.rs.core.Response.ok(users).build();
		} catch (Twenty20Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return javax.ws.rs.core.Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

}
