package com.example

import android.os.Build
import androidx.annotation.RequiresApi
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.example.StreamPlayExtractor.invoke2embed
import com.example.StreamPlayExtractor.invoke4khdhub
import com.example.StreamPlayExtractor.invokeAllMovieland
import com.example.StreamPlayExtractor.invokeAnimes
import com.example.StreamPlayExtractor.invokeBollyflix
import com.example.StreamPlayExtractor.invokeCinemaOS
import com.example.StreamPlayExtractor.invokeDahmerMovies
import com.example.StreamPlayExtractor.invokeDotmovies
import com.example.StreamPlayExtractor.invokeDramadrip
import com.example.StreamPlayExtractor.invokeEmovies
import com.example.StreamPlayExtractor.invokeExtramovies
import com.example.StreamPlayExtractor.invokeFilm1k
import com.example.StreamPlayExtractor.invokeHdmovie2
import com.example.StreamPlayExtractor.invokeKimcartoon
import com.example.StreamPlayExtractor.invokeKisskh
import com.example.StreamPlayExtractor.invokeKisskhAsia
import com.example.StreamPlayExtractor.invokeMappleTv
import com.example.StreamPlayExtractor.invokeMoflix
import com.example.StreamPlayExtractor.invokeMovieBox
import com.example.StreamPlayExtractor.invokeMoviehubAPI
import com.example.StreamPlayExtractor.invokeMoviesdrive
import com.example.StreamPlayExtractor.invokeMoviesmod
import com.example.StreamPlayExtractor.invokeMultiEmbed
import com.example.StreamPlayExtractor.invokeMultimovies
import com.example.StreamPlayExtractor.invokeNepu
import com.example.StreamPlayExtractor.invokeNinetv
import com.example.StreamPlayExtractor.invokeNuvioStreams
import com.example.StreamPlayExtractor.invokePlaydesi
import com.example.StreamPlayExtractor.invokePlayer4U
import com.example.StreamPlayExtractor.invokePrimeSrc
import com.example.StreamPlayExtractor.invokeRidomovies
import com.example.StreamPlayExtractor.invokeRiveStream
import com.example.StreamPlayExtractor.invokeRogmovies
import com.example.StreamPlayExtractor.invokeShowflix
import com.example.StreamPlayExtractor.invokeSoapy
import com.example.StreamPlayExtractor.invokeSuperstream
import com.example.StreamPlayExtractor.invokeToonstream
import com.example.StreamPlayExtractor.invokeTopMovies
import com.example.StreamPlayExtractor.invokeUhdmovies
import com.example.StreamPlayExtractor.invokeVegamovies
import com.example.StreamPlayExtractor.invokeVidFast
import com.example.StreamPlayExtractor.invokeVidPlus
import com.example.StreamPlayExtractor.invokeVidSrcXyz
import com.example.StreamPlayExtractor.invokeVideasy
import com.example.StreamPlayExtractor.invokeVidlink
import com.example.StreamPlayExtractor.invokeVidnest
import com.example.StreamPlayExtractor.invokeVidsrccc
import com.example.StreamPlayExtractor.invokeVidzee
import com.example.StreamPlayExtractor.invokeWatch32APIHQ
import com.example.StreamPlayExtractor.invokeWatchsomuch
import com.example.StreamPlayExtractor.invokeXDmovies
import com.example.StreamPlayExtractor.invokeXPrimeAPI
import com.example.StreamPlayExtractor.invokeYflix
import com.example.StreamPlayExtractor.invokeZoechip
import com.example.StreamPlayExtractor.invokeZshow
import com.example.StreamPlayExtractor.invokehdhub4u
import com.example.StreamPlayExtractor.invokemorph
import com.example.StreamPlayExtractor.invokemp4hydra
import com.example.StreamPlayExtractor.invokevidrock

data class Provider(
    val id: String,
    val name: String,
    val invoke: suspend (
        res: StreamPlay.LinkData,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit,
        token: String,
        dahmerMoviesAPI: String
    ) -> Unit
)


@RequiresApi(Build.VERSION_CODES.O)
fun buildProviders(): List<Provider> {
    return listOf(
        Provider("uhdmovies", "UHD Movies (Multi)") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokeUhdmovies(res.title, res.year, res.season, res.episode, callback, subtitleCallback)
        },
        Provider("anime", "All Anime Sources") { res, subtitleCallback, callback, _, _ ->
            if (res.isAnime) invokeAnimes(res.title, res.jpTitle, res.date, res.airedDate, res.season, res.episode, subtitleCallback, callback, res.isDub)
        },
        Provider("player4u", "Player4U") { res, _, callback, _, _ ->
            if (!res.isAnime) invokePlayer4U(res.title, res.season, res.episode, res.year, callback)
        },
        Provider("vidsrccc", "Vidsrccc") { res, _, callback, _, _ ->
            if (!res.isAnime) invokeVidsrccc(res.id, res.season, res.episode, callback)
        },
        Provider("topmovies", "Top Movies") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokeTopMovies(res.imdbId, res.year, res.season, res.episode, subtitleCallback, callback)
        },
        Provider("moviesmod", "MoviesMod") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokeMoviesmod(res.imdbId, res.year, res.season, res.episode, subtitleCallback, callback)
        },
        Provider("bollyflix", "Bollyflix") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokeBollyflix(res.imdbId, res.season, res.episode, subtitleCallback, callback)
        },
        Provider("watchsomuch", "WatchSoMuch") { res, subtitleCallback, _, _, _ ->
            if (!res.isAnime) invokeWatchsomuch(res.imdbId, res.season, res.episode, subtitleCallback)
        },
        Provider("ninetv", "NineTV") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokeNinetv(res.id, res.season, res.episode, subtitleCallback, callback)
        },
        Provider("ridomovies", "RidoMovies") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokeRidomovies(res.id, res.imdbId, res.season, res.episode, subtitleCallback, callback)
        },
        Provider("moviehubapi", "MovieHub API") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokeMoviehubAPI(res.id, res.season, res.episode, subtitleCallback, callback)
        },
        Provider("allmovieland", "AllMovieland") { res, _, callback, _, _ ->
            if (!res.isAnime) invokeAllMovieland(res.imdbId, res.season, res.episode, callback)
        },
        Provider("multiembed", "MultiEmbed") { res, _, callback, _, _ ->
            if (!res.isAnime) invokeMultiEmbed(res.imdbId, res.season, res.episode, callback)
        },
        Provider("emovies", "EMovies") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokeEmovies(res.title, res.year, res.season, res.episode, subtitleCallback, callback)
        },
        Provider("vegamovies", "VegaMovies (Multi)") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokeVegamovies(res.title, res.year, res.season, res.episode, res.imdbId, subtitleCallback, callback)
        },
        Provider("extramovies", "ExtraMovies") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokeExtramovies(res.imdbId, res.season, res.episode, subtitleCallback, callback)
        },
        Provider("multimovies", "MultiMovies (Multi)") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokeMultimovies(res.title, res.season, res.episode, subtitleCallback, callback)
        },
        Provider("2embed", "2Embed") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invoke2embed(res.imdbId, res.season, res.episode, subtitleCallback, callback)
        },
        Provider("zshow", "ZShow") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokeZshow(res.title, res.year, res.season, res.episode, subtitleCallback, callback)
        },
        Provider("showflix", "ShowFlix (South Indian)") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokeShowflix(res.title, res.year, res.season, res.episode, subtitleCallback, callback)
        },
        Provider("moflix", "Moflix (Multi)") { res, _, callback, _, _ ->
            if (!res.isAnime) invokeMoflix(res.id, res.season, res.episode, callback)
        },
        Provider("zoechip", "ZoeChip") { res, _, callback, _, _ ->
            if (!res.isAnime) invokeZoechip(res.title, res.year, res.season, res.episode, callback)
        },
        Provider("nepu", "Nepu") { res, _, callback, _, _ ->
            if (!res.isAnime) invokeNepu(res.title, res.airedYear ?: res.year, res.season, res.episode, callback)
        },
        Provider("playdesi", "PlayDesi") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokePlaydesi(res.title, res.season, res.episode, subtitleCallback, callback)
        },
        Provider("moviesdrive", "MoviesDrive (Multi)") { res, subtitleCallback, callback, _, _ ->
            invokeMoviesdrive(res.title, res.season, res.episode, res.year, res.imdbId, subtitleCallback, callback)
        },
        Provider("watch32APIHQ", "Watch32 API HQ (English)") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokeWatch32APIHQ(res.title, res.season, res.episode,
                subtitleCallback, callback)
        },
        Provider("primesrc", "PrimeSrc") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokePrimeSrc(res.imdbId, res.season, res.episode, subtitleCallback, callback)
        },
        Provider("film1k", "Film1k") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokeFilm1k(res.title, res.season, res.year, subtitleCallback, callback)
        },
        Provider("superstream", "SuperStream") { res, _, callback, token, _ ->
            if (!res.isAnime && res.imdbId != null) invokeSuperstream(token, res.imdbId, res.season, res.episode, callback)
        },
        Provider("vidsrcxyz", "VidSrcXyz (English)") { res, _, callback, _, _ ->
            if (!res.isAnime) invokeVidSrcXyz(res.imdbId, res.season, res.episode, callback)
        },
        Provider("xprimeapi", "XPrime API") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokeXPrimeAPI(res.title, res.year, res.imdbId, res.id, res.season, res.episode, subtitleCallback, callback)
        },
        Provider("vidzeeapi", "Vidzee API") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokeVidzee(res.id, res.season, res.episode, subtitleCallback, callback)
        },
        Provider("4khdhub", "4kHdhub (Multi)") { res, subtitleCallback, callback, _, _ ->
            invoke4khdhub(res.title, res.year, res.season, res.episode, subtitleCallback, callback)
        },
        Provider("hdhub4u", "Hdhub4u (Multi)") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokehdhub4u(res.imdbId, res.title, res.year, res.season, res.episode, subtitleCallback, callback)
        },
        Provider("hdmovie2", "Hdmovie2") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokeHdmovie2(res.title, res.year,
                res.episode, subtitleCallback, callback)
        },
        Provider("dramadrip", "Dramadrip (Asian Drama)") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime)invokeDramadrip(res.imdbId, res.season, res.episode, subtitleCallback, callback)
        },
        Provider("rivestream", "RiveStream") { res, _, callback, _, _ ->
            if (!res.isAnime) invokeRiveStream(res.id, res.season, res.episode, callback)
        },
        Provider("moviebox", "MovieBox (Multi)") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokeMovieBox(res.title, res.season, res.episode, subtitleCallback, callback)
        },
        Provider("morph", "Morph") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokemorph(res.title, res.year, res.season, res.episode, subtitleCallback, callback)
        },
        Provider("vidrock", "Vidrock") { res, _, callback, _, _ ->
            if (!res.isAnime) invokevidrock(res.id, res.season, res.episode, callback)
        },
        Provider("soapy", "Soapy") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokeSoapy(res.id, res.season, res.episode, subtitleCallback, callback)
        },
        Provider("vidlink", "Vidlink") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokeVidlink(res.id, res.season, res.episode, subtitleCallback, callback)
        },
        Provider("mappletv", "MappleTV") { res, _, callback, _, _ ->
            if (!res.isAnime) invokeMappleTv(res.id, res.season, res.episode, callback)
        },
        Provider("vidnest", "Vidnest") { res, _, callback, _, _ ->
            if (!res.isAnime) invokeVidnest(res.id, res.season, res.episode, callback)
        },
        Provider("dotmovies", "DotMovies") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokeDotmovies(res.imdbId, res.title, res.year, res.season, res.episode, subtitleCallback, callback)
        },
        Provider("rogmovies", "RogMovies") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokeRogmovies(res.imdbId, res.title, res.year, res.season, res.episode, subtitleCallback, callback)
        },
        Provider("kisskh", "KissKH (Asian Drama)") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokeKisskh(res.title, res.season, res.episode, res.lastSeason, subtitleCallback, callback)
        },
        Provider("cinemaos", "CinemaOS") { res, _, callback, _, _ ->
            invokeCinemaOS(res.imdbId, res.id, res.title, res.season, res.episode, res.year, callback)
        },
        Provider("dahmermovies", "DahmerMovies") { res, _, callback, _, dahmerMoviesAPI ->
            if (!res.isAnime) invokeDahmerMovies(dahmerMoviesAPI, res.title, res.year, res.season, res.episode, callback)
        },
        Provider("KisskhAsia", "KissKhAsia (Asian Drama)") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokeKisskhAsia(res.id, res.season, res.episode, subtitleCallback, callback)
        },
        Provider("mp4hydra", "MP4Hydra") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokemp4hydra(res.title, res.year,res.season, res.episode, subtitleCallback, callback)
        },
        Provider("vidfast", "VidFast") { res, _, callback, _, _ ->
            if (!res.isAnime) invokeVidFast(res.id, res.season,res.episode, callback)
        },
        Provider("vidplus", "VidPlus") { res, _, callback, _, _ ->
            if (!res.isAnime) invokeVidPlus(res.id, res.season,res.episode,  callback)
        },
        Provider("toonstream", "Toonstream (Hindi Anime)") { res, subtitleCallback, callback, _, _ ->
            if (res.isAnime || res.isCartoon) invokeToonstream(res.title, res.season, res.episode, subtitleCallback, callback)
        },
        Provider("NuvioStreams", "NuvioStreams") { res, _, callback, _, _ ->
            invokeNuvioStreams(res.imdbId, res.season,res.episode,  callback)
        },
        Provider("VidEasy", "VidEasy") { res, subtitleCallback, callback, _, _ ->
            invokeVideasy(res.id, res.imdbId, res.title, res.year, res.season,res.episode,  callback, subtitleCallback)
        },
        Provider("XDMovies", "XDMovies") { res, subtitleCallback, callback, _, _ ->
            invokeXDmovies(res.title,res.id, res.season, res.episode,  callback, subtitleCallback)
        },
        Provider("KimCartoon", "KimCartoon") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokeKimcartoon(res.title, res.season, res.episode, subtitleCallback, callback)
        },
        Provider("YFlix", "YFlix") { res, subtitleCallback, callback, _, _ ->
            if (!res.isAnime) invokeYflix(res.title, res.season, res.episode, subtitleCallback, callback)
        },
    )
}
