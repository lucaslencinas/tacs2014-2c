package com.tacs.truequeLibre.endpoints;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;
import com.tacs.truequeLibre.setup.Setup;
import com.tacs.truequeLibre.domain.ListaDeUsuarios;
import com.tacs.truequeLibre.domain.Usuario;

@Path("/amigos")
public class Amigos {

	/**
	 * Listar Usuarios.
	 * @return: Un JSON que representa la lista de usuarios que tiene como amigos la persona que se logueo
	 */
	/*En el @Context HttpHeader hh estan los parametros que se envian, como headers, cookies y esas cosas.
	 *Desde javascript tengo que guardar previamente la cookie en la sesion, por ahora lo hago con:
	 *document.cookie = 'key=' + value;
	 *
	 *Lo empiezo a probar aca porque es la primer llamada que se hace desde javascript hasta el servidor, supongo que tendria que ser
	 *en otra pero despues los cambiamos
	 *
	 * */
    @GET 
    @Produces("application/json")
    public Response index(@Context HttpHeaders header) {
      // NEGRADA FIXME
      if(Setup.isSet == false){
      	Setup.isSet = true;
      	Setup.setup();
      }
      
      guardarItemDePrueba();
      
      
    	
    	System.out.println(header.toString());
      Usuario user = Setup.facebook.getLoggedUser(header);
      if(user == null)
      return Response.status(500).build();
      
      System.out.println("me pidieron los amigos de " + user.getNombre());
      ListaDeUsuarios amigos = Setup.facebook.getAmigos(user, header);
    	String usuariosJson = new Gson().toJson(amigos);
    	
      return Response.ok(usuariosJson,MediaType.APPLICATION_JSON).build();
    }
    
     private void guardarItemDePrueba() {
    	 /**
    	  * Kind es una tabla
				*	Entity es una fila
				*	Key es una pK
				*	Property es un campo
    	  * **/
       Key itemKey = KeyFactory.createKey("Item", Setup.item1.getId());
       Entity item = new Entity("Item", itemKey);
       item.setProperty("id", Setup.item1.getId());
       item.setProperty("title", Setup.item1.getTitulo());
       item.setProperty("description", Setup.item1.getDescripcion());
       DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
       datastore.put(item);
    	 
    	 
    	 
		
	}

		/**
      * FIXME (Lucas)--> Este lo modifique, ahora se le pasa un String id, ver si hay error
      *  Listar un determinado usuario.
      * @param id: El id de un usuario que quiero ver
      * @return: un usuario con ese id que se pasa por parametro
      */
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response show(@PathParam("id") String id){
    	
    	Usuario unUsuario= Setup.usuarios.findById(id);	
    	Gson unGson = new Gson();
    	String unUsuarioJson = unGson.toJson(unUsuario);
    	return Response.ok(unUsuarioJson, MediaType.APPLICATION_JSON).build();
    }
    
    /**
     * FIXME(Lucas) --> Este lo modifique, ahora se le pasa un String id, ver si hay error
    **/
    @GET
    @Path("/{id}/items")
    @Produces("application/json")
    public Response showItems(@PathParam("id") String id){
    	Usuario unUsuario= Setup.usuarios.findById(id);	
    	Gson unGson = new Gson();
    	String itemsDeUsuarioJson = unGson.toJson(unUsuario.getItems());	//No se si va a andar
    	return Response.ok(itemsDeUsuarioJson, MediaType.APPLICATION_JSON).build();
    }


}
