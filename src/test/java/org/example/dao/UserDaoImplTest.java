package org.example.dao;

import org.example.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.hibernate.cfg.Configuration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class UserDaoImplTest {

    @Container
    private static final PostgreSQLContainer postgres =
            new PostgreSQLContainer("postgres:17")
                    .withDatabaseName("user_service")
                    .withUsername("test")
                    .withPassword("test");

    private static SessionFactory sessionFactory;
    private UserDaoImpl userDao;

    @BeforeAll
    static void setUp() {
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        configuration.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", postgres.getUsername());
        configuration.setProperty("hibernate.connection.password", postgres.getPassword());
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.addAnnotatedClass(User.class);
        sessionFactory = configuration.buildSessionFactory();
    }

    @BeforeEach
    void init() {
        userDao = new UserDaoImpl(sessionFactory);
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createMutationQuery("delete from User")
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @AfterAll
    static void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    // =====================
    // save()
    // =====================

    @Test
    @DisplayName("Пользователь создан")
    public void save_ShouldSaveUser() {
        User user = new User("Denis", "denis@com", 26);
        userDao.save(user);
        assertNotNull(user.getId());

        try (Session session = sessionFactory.openSession()) {
            User savedUser = session.find(User.class, user.getId());

            assertNotNull(savedUser);
            assertEquals("Denis", savedUser.getName());
            assertEquals("denis@com", savedUser.getEmail());
            assertEquals(26, savedUser.getAge());
        }
    }

    // =====================
    // findById()
    // =====================

    @Test
    @DisplayName("Пользователь найден")
    public void findById_ShouldReturnUser() {
        User user = new User("Denis", "denis@com", 26);
        userDao.save(user);

        User foundUser = userDao.findById(user.getId());

        assertNotNull(foundUser);
        assertEquals("Denis", foundUser.getName());
        assertEquals("denis@com", foundUser.getEmail());
        assertEquals(26, foundUser.getAge());
    }

    @Test
    @DisplayName("Пользователь не найден")
    public void findById_ShouldReturnNull_WhenUserNotFound(){
        User foundUser = userDao.findById(999L);
        assertNull(foundUser);
    }

    // =====================
    // findAll()
    // =====================

    @Test
    @DisplayName("список пользователей")
    public void findAll_ShouldReturnUsers() {
        User user1 = new User("Denis", "denis@com", 26);
        User user2 = new User("Ivan", "ivan@com", 30);
        userDao.save(user1);
        userDao.save(user2);

        List<User> users = userDao.findAll();

        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("Denis",users.get(0).getName());
        assertEquals("Ivan",users.get(1).getName());
    }

    @Test
    @DisplayName("список пользователей пуст")
    public void findAll_ShouldReturnEmptyList_WhenNoUsersExist(){
        List<User> users = userDao.findAll();
        assertTrue(users.isEmpty());
    }

    // =====================
    // update()
    // =====================

    @Test
    @DisplayName("пользователь обновлен")
    public void update_ShouldUpdateUser() {
        User user = new User("Denis", "denis@com", 26);
        userDao.save(user);

        user.setName("Ivan");
        user.setEmail("ivan@com");
        user.setAge(30);

        userDao.update(user);

        try (Session session = sessionFactory.openSession()) {
            User updatedUser = session.find(User.class, user.getId());

            assertNotNull(updatedUser);
            assertEquals("Ivan", updatedUser.getName());
            assertEquals("ivan@com", updatedUser.getEmail());
            assertEquals(30, updatedUser.getAge());
        }
    }

    // =====================
    // delete()
    // =====================

    @Test
    @DisplayName("пользователь удален")
    public void delete_ShouldDeleteUser() {
        User user = new User("Denis", "denis@com", 26);
        userDao.save(user);

        userDao.delete(user.getId());
        try(Session session = sessionFactory.openSession()){
            User deletedUser = session.find(User.class, user.getId());
            assertNull(deletedUser);
        }
    }
}
