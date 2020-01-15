package htwb.ai.FaDen.dao;

import htwb.ai.FaDen.bean.Song;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SongDaoTest {

    private static final String PERSISTENCE_UNIT_NAME = "songDB-TEST-PU";
    private static EntityManagerFactory emf;
    private SongDao songDao;

    @BeforeAll
    public static void initEMF() {
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    @BeforeEach
    public void setUpDao() {
        songDao = new SongDao(emf);
    }

    @Test
    void initStandardUseCase() {
        SongDao dao = new SongDao(emf);
        assertNotNull(dao);
    }
}