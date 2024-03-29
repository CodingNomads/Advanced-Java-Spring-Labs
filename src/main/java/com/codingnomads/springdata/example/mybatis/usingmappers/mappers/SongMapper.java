/* CodingNomads (C)2024 */
package com.codingnomads.springdata.example.mybatis.usingmappers.mappers;

import com.codingnomads.springdata.example.mybatis.usingmappers.models.Artist;
import com.codingnomads.springdata.example.mybatis.usingmappers.models.Song;
import java.util.List;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

@Mapper
public interface SongMapper {

    @Insert("INSERT INTO mybatis.songs " + "(name, artist_id, album_name, song_length) "
            + "VALUES (#{name}, #{artist.id}, #{albumName}, #{songLength});")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void insertNewSong(Song song);

    @Select("SELECT * " + "FROM mybatis.songs " + "WHERE id = #{param1};")
    @Results(
            id = "songResultMap",
            value = {
                @Result(property = "albumName", column = "album_name"),
                @Result(property = "songLength", column = "song_length"),
                @Result(
                        // property to map to
                        property = "artist",
                        column = "artist_id",
                        javaType = Artist.class,
                        one =
                                @One(
                                        select =
                                                "com.codingnomads.springdata.example.mybatis.usingmappers.mappers.ArtistMapper.getArtistById",
                                        fetchType = FetchType.LAZY))
            })
    Song getSongById(Long id);

    @Select("SELECT * " + "FROM mybatis.songs " + "WHERE name = #{param1};")
    @ResultMap("songResultMap")
    List<Song> getSongsByName(String name);

    @Select("SELECT * " + "FROM mybatis.songs " + "WHERE artist_id = #{param1} AND album_name = #{param2};")
    @ResultMap("songResultMap")
    List<Song> getSongsByAlbumAndArtist(Long artistId, String albumName);

    @Select("SELECT * " + "FROM mybatis.songs " + "WHERE artist_id = #{param1};")
    @ResultMap("songResultMap")
    List<Song> getSongsByArtistId(Long artistId);

    @Update("UPDATE mybatis.songs "
            + "SET name = #{name}, artist_id = #{artist.id}, album_name = #{albumName}, song_length = #{songLength} "
            + "WHERE id = #{id};")
    void updateSong(Song song);

    @Delete("DELETE FROM mybatis.songs WHERE id = #{param1};")
    void deleteSongById(Long songId);

    @Delete("DELETE FROM mybatis.songs " + "WHERE artist_id = #{artistId} AND album_name = #{albumName};")
    void deleteSongsByAlbumAndArtist(Long artistId, String albumName);
}
