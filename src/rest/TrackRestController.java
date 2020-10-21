package rest;

import domain.objects.Track;
import rest.dto.GetTrackListResponse;
import service.IDTOBuilderService;
import service.IDTOToDomainService;
import service.ITokenService;
import service.ITrackService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/")
public class TrackRestController {

    @Inject
    private ITokenService tokenService;

    @Inject private ITrackService trackService;

    @Inject private IDTOBuilderService dtoBuilderService;

    @Inject private IDTOToDomainService dtoToDomainService;

    @GET
    @Path("tracks")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTracks(@QueryParam("token") String token, @QueryParam("forPlaylist") int forPlaylist){
        if(!tokenService.validateToken(token)) { return Response.status(Response.Status.FORBIDDEN).build(); }
        final List<Track> trackList = trackService.getAllTracks(forPlaylist);
        final GetTrackListResponse dto = dtoBuilderService.buildGetAllTracksResponseDTO(trackList);
        return Response.status(Response.Status.OK).entity(dto).build();
    }

    @GET
    @Path("playlists/{id}/tracks")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTracksByPlayList(@QueryParam("token") String token, @PathParam("id") int playListID){
        if(!tokenService.validateToken(token)) { return Response.status(Response.Status.FORBIDDEN).build(); }
        List<Track> trackList = trackService.getAllTracksByPlayList(playListID);
        GetTrackListResponse dto = dtoBuilderService.buildGetAllTracksResponseDTO(trackList);
        return Response.status(Response.Status.OK).entity(dto).build();
    }

    @DELETE
    @Path("playlists/{playListID}/tracks/{trackID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeTrackFromPlayList(@QueryParam("token") String token,
                                            @PathParam("playListID") int playListID,
                                            @PathParam("trackID") int trackID)
    {
        if(!tokenService.validateToken(token)) { return Response.status(Response.Status.FORBIDDEN).build(); }
        trackService.removeTrackFromPlayList(playListID, trackID);
        List<Track> trackList = trackService.getAllTracksByPlayList(playListID);
        GetTrackListResponse dto = dtoBuilderService.buildGetAllTracksResponseDTO(trackList);
        return Response.status(Response.Status.OK).entity(dto).build();
    }

    @POST
    @Path("playlists/{id}/tracks")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTrackToPlayList(@QueryParam("token") String token,
                                       @PathParam("id") int playListID,
                                       rest.dto.Track track)
    {
        if(!tokenService.validateToken(token)) { return Response.status(Response.Status.FORBIDDEN).build(); }
        trackService.addTrackToPlayList(playListID, dtoToDomainService.convertTrackDTOToTrack(track));
        trackService.updateOfflineAvailable(dtoToDomainService.convertTrackDTOToTrack(track));
        List<Track> trackList = trackService.getAllTracksByPlayList(playListID);
        GetTrackListResponse dto = dtoBuilderService.buildGetAllTracksResponseDTO(trackList);
        return Response.status(Response.Status.OK).entity(dto).build();
    }
}
