/**
 * 
 */
package com.sokoban.Jersey;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sokoban.db.DBHandler;
import com.sokoban.db.SokobanSolution;

/**
 * @author שדמה
 * The service for GET/POST methods
 */
@Path("solutions")
public class SolutionService {
	
	private DBHandler dbHandler = new DBHandler();
	
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("{name}")
    public String getSolution(@PathParam("name") String name) {
        return dbHandler.getSolution(name);
    }
    
    /**
     * Method handling HTTP POST requests. The returned object will be sent
     * to the client as "APPLICATION_FORM_URLENCODED" media type.
     *
     * @return String that will be returned as a APPLICATION_FORM_URLENCODED response.
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void addSolution(@FormParam("name") String name,@FormParam("solution") String solution) {
        SokobanSolution sol = new SokobanSolution(name, solution);
    	dbHandler.addSolution(sol);
    }
}
