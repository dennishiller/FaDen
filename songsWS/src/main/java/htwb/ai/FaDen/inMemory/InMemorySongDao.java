package htwb.ai.FaDen.inMemory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import htwb.ai.FaDen.bean.Song;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.util.List;

public class InMemorySongDao {

    // Reads a list of songs from a JSON-file into List<Song>
    @SuppressWarnings("unchecked")
    public List<Song> readJSONToSongs(String filename) throws IOException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(filename);
        ObjectMapper objectMapper = new ObjectMapper();
        return (List<Song>) objectMapper.readValue(in, new TypeReference<List<Song>>() {});
    }

    public InMemorySongs loadSongsInMemory(String filename) throws IOException {
        List<Song> songs = readJSONToSongs(filename);
        return new InMemorySongs(songs);
    }

    public void saveInMemoryToJSON(List<Song> songs, String filename) throws IOException { //TODO was ist wenn File noch nicht existiert?
        PrintWriter writer = new PrintWriter(new File(this.getClass().getClassLoader().getResource(filename).getPath()));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(writer, songs);
    }

    public void writeStoragetoJSON(InMemorySongs database, String filename) throws IOException {
        List<Song> songs = database.getSongs();
        saveInMemoryToJSON(songs, filename);
    }

    public String convertSongToXml(Song song) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Song.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(song, sw);
            return sw.toString();
        } catch (JAXBException e) {
            return e.getMessage();
        }
    }
}
