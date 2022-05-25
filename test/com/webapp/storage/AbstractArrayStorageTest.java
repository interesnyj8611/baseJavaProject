package com.webapp.storage;

import com.webapp.exception.ExistStorageException;
import com.webapp.exception.NotExistStorageException;
import com.webapp.exception.StorageException;
import com.webapp.model.Resume;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public abstract class  AbstractArrayStorageTest {
    Storage storage;
    private static final String UUID_1 = "uuid1";
    private static final Resume RESUME_1 = new Resume(UUID_1);
    private static final String UUID_2 = "uuid2";
    private static final Resume RESUME_2 = new Resume(UUID_2);
    private static final String UUID_3 = "uuid3";
    private static final Resume RESUME_3 = new Resume(UUID_3);

    private static final String UUID_4 = "uuid4";
    private static final Resume RESUME_4 = new Resume(UUID_4);

    protected AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    public void  setUp() {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    void size() {
        assertSize(3);
    }

    @Test
    void clear() {
        storage.clear();
        assertSize(0);
    }

    @Test
    void get() {
        assertGet(RESUME_1);
        assertGet(RESUME_2);
        assertGet(RESUME_3);
        assertGet(RESUME_4);
    }


    @Test
    void getAll() {
        Resume[] array = storage.getAll();
        Assertions.assertEquals(3, array.length);
        Assertions.assertEquals(RESUME_1, array[0]);
        Assertions.assertEquals(RESUME_2, array[1]);
        Assertions.assertEquals(RESUME_3, array[2]);
    }

    @Test
    void delete() {
        storage.delete(UUID_1);
        assertSize(2);
        storage.get(UUID_1);
    }

    @Test
    void save() {
        storage.save(RESUME_4);
        assertSize(4);
        assertGet(RESUME_4);
    }

    @Test
    void update() {
        Resume newResume = new Resume(UUID_1);
        storage.update(newResume);
        Assertions.assertTrue(newResume == storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    void updateNotExist() {

    }

    @Test(expected = ExistStorageException.class)
    void saveExist() {
        storage.save(RESUME_1);
    }

    @Test(expected = StorageException.class)
    void saveOverflow() {
        try {
            for (int i = 4; i <= AbstractArrayStorage.STORAGE_LIMIT; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException e) {
            Assertions.fail();
        }
        storage.save(new Resume());
    }

    @Test(expected = NotExistStorageException.class)
    void deleteNotExist() {
        storage.delete("dummy");
    }
    @Test(expected = NotExistStorageException.class)
     void getNotExist() {
        storage.get("dummy");
    }

    private void assertGet(Resume r) {
        Assertions.assertEquals(r, storage.get(r.getUuid()));
    }
    private void assertSize(int size) {
        Assertions.assertEquals(size, storage.size());
    }
}