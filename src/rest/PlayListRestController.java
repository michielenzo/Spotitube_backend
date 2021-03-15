package rest;

import domain.objects.PlayList;
import rest.dto.AllPlayListsResponse;
import rest.dto.ChangePlayListNameRequest;
import rest.dto.CreatePlayListRequest;
import service.IDTOBuilderService;
import service.IDTOToDomainService;
import service.IPlayListService;
import service.ITokenService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/")
public class PlayListRestController {

    @Inject private ITokenService tokenService;

    @Inject private IPlayListService playListService;

    @Inject private IDTOBuilderService dtoBuilderService;

    @Inject private IDTOToDomainService dtoToDomainService;

    @POST
    @Path("playlists")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPlayList(@QueryParam("token") String token, CreatePlayListRequest playListDTO){
        if(!tokenService.validateToken(token)) { return Response.status(Response.Status.FORBIDDEN).build(); }
        playListService.createPlayList(playListDTO, token);
        final List<PlayList> allPlayLists = playListService.getAllPlayLists(token);
        final AllPlayListsResponse dto = dtoBuilderService.buildAllPlayListsResponseDTO(allPlayLists, token);
        return Response.status(Response.Status.OK).entity(dto).build();
    }

    @GET
    @Path("playlists")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPlaylists(@QueryParam("token") String token){
        if(!tokenService.validateToken(token)) { return Response.status(Response.Status.FORBIDDEN).build(); }
        final List<PlayList> allPlayLists = playListService.getAllPlayLists(token);
        final AllPlayListsResponse dto = dtoBuilderService.buildAllPlayListsResponseDTO(allPlayLists, token);
        return Response.status(Response.Status.OK).entity(dto).build();
    }

    @PUT
    @Path("playlists/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changePlayListName(@QueryParam("token") String token,
                                       @PathParam("id") int id,
                                       ChangePlayListNameRequest playListDTO)
    {
        if(!tokenService.validateToken(token)) { return Response.status(Response.Status.FORBIDDEN).build(); }
        playListService.changeName(dtoToDomainService.convertPlayListDTOToPlayList(playListDTO));
        final List<PlayList> allPlayLists = playListService.getAllPlayLists(token);
        final AllPlayListsResponse dto = dtoBuilderService.buildAllPlayListsResponseDTO(allPlayLists, token);
        return Response.status(Response.Status.OK).entity(dto).build();
    }

    @DELETE
    @Path("playlists/{id}")
    public Response deletePlayList(@QueryParam("token") String token, @PathParam("id") int id){
        if(!tokenService.validateToken(token)) { return Response.status(Response.Status.FORBIDDEN).build(); }
        playListService.deletePlayList(id);
        final List<PlayList> allPlayLists = playListService.getAllPlayLists(token);
        final AllPlayListsResponse dto = dtoBuilderService.buildAllPlayListsResponseDTO(allPlayLists, token);
        return Response.status(Response.Status.OK).entity(dto).build();
    }
}
