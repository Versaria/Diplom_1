package praktikum;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Класс для тестирования базы данных ингредиентов (Database)
 * Проверяет инициализацию данных и корректность работы методов доступа
 */
public class DatabaseTest {

    private Database database;

    /**
     * Метод выполняется перед каждым тестом
     * Инициализирует базу данных для тестирования
     */
    @Before
    public void setUp() {
        database = new Database();
    }

    /**
     * Тест проверяет корректность получения списка доступных булочек
     * Проверяет наличие ожидаемых булочек и базовые свойства списка
     */
    @Test
    public void testAvailableBunsReturnsCorrectList() {
        // Act: получаем список булочек из базы данных
        List<Bun> buns = database.availableBuns();

        // Assert: проверяем базовые свойства списка
        assertNotNull("Список булочек не должен быть null", buns);
        assertFalse("Список булочек не должен быть пустым", buns.isEmpty());
        assertEquals("В базе должно быть 3 булочки", 3, buns.size());

        // Assert: проверяем наличие ожидаемых булочек
        assertTrue("Должна содержать black bun",
                buns.stream().anyMatch(bun -> "black bun".equals(bun.getName())));
        assertTrue("Должна содержать white bun",
                buns.stream().anyMatch(bun -> "white bun".equals(bun.getName())));
        assertTrue("Должна содержать red bun",
                buns.stream().anyMatch(bun -> "red bun".equals(bun.getName())));
    }

    /**
     * Тест проверяет корректность цен булочек в базе данных
     */
    @Test
    public void testAvailableBunsHaveCorrectPrices() {
        // Act: получаем список булочек
        List<Bun> buns = database.availableBuns();

        // Assert: проверяем цены конкретных булочек с проверкой наличия
        Optional<Bun> blackBunOpt = buns.stream().filter(b -> "black bun".equals(b.getName())).findFirst();
        assertTrue("Булочка black bun должна присутствовать в базе", blackBunOpt.isPresent());
        assertEquals("Цена black bun должна быть 100", 100.0f, blackBunOpt.get().getPrice(), 0.001);

        Optional<Bun> whiteBunOpt = buns.stream().filter(b -> "white bun".equals(b.getName())).findFirst();
        assertTrue("Булочка white bun должна присутствовать в базе", whiteBunOpt.isPresent());
        assertEquals("Цена white bun должна быть 200", 200.0f, whiteBunOpt.get().getPrice(), 0.001);

        Optional<Bun> redBunOpt = buns.stream().filter(b -> "red bun".equals(b.getName())).findFirst();
        assertTrue("Булочка red bun должна присутствовать в базе", redBunOpt.isPresent());
        assertEquals("Цена red bun должна быть 300", 300.0f, redBunOpt.get().getPrice(), 0.001);
    }

    /**
     * Тест проверяет корректность получения списка доступных ингредиентов
     * Проверяет наличие ожидаемых ингредиентов
     */
    @Test
    public void testAvailableIngredientsReturnsCorrectList() {
        // Act: получаем список ингредиентов из базы данных
        List<Ingredient> ingredients = database.availableIngredients();

        // Assert: проверяем базовые свойства списка
        assertNotNull("Список ингредиентов не должен быть null", ingredients);
        assertFalse("Список ингредиентов не должен быть пустым", ingredients.isEmpty());
        assertTrue("Список ингредиентов должен содержать как минимум 6 элементов",
                ingredients.size() >= 6);

        // Assert: проверяем наличие конкретных ингредиентов
        assertTrue("Должен содержать hot sauce",
                ingredients.stream().anyMatch(i -> "hot sauce".equals(i.getName())));
        assertTrue("Должен содержать sour cream",
                ingredients.stream().anyMatch(i -> "sour cream".equals(i.getName())));
        assertTrue("Должен содержать chili sauce",
                ingredients.stream().anyMatch(i -> "chili sauce".equals(i.getName())));
    }

    /**
     * Тест проверяет корректность типов и цен ингредиентов в базе данных
     */
    @Test
    public void testAvailableIngredientsHaveCorrectTypesAndPrices() {
        // Act: получаем список ингредиентов
        List<Ingredient> ingredients = database.availableIngredients();

        // Assert: проверяем конкретные ингредиенты с проверкой наличия
        Ingredient hotSauce = ingredients.stream()
                .filter(i -> "hot sauce".equals(i.getName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("hot sauce должен присутствовать в базе"));

        assertEquals("hot sauce должен быть типом SAUCE", IngredientType.SAUCE, hotSauce.getType());
        assertEquals("Цена hot sauce должна быть 100", 100.0f, hotSauce.getPrice(), 0.001);

        Ingredient cutlet = ingredients.stream()
                .filter(i -> "cutlet".equals(i.getName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("cutlet должен присутствовать в базе"));

        assertEquals("cutlet должен быть типом FILLING", IngredientType.FILLING, cutlet.getType());
        assertEquals("Цена cutlet должна быть 100", 100.0f, cutlet.getPrice(), 0.001);
    }

    /**
     * Тест проверяет наличие всех ожидаемых начинок в базе данных
     */
    @Test
    public void testDatabaseContainsExpectedFillings() {
        // Act: получаем список ингредиентов
        List<Ingredient> ingredients = database.availableIngredients();

        // Ищем ожидаемые начинки в списке
        boolean foundCutlet = ingredients.stream()
                .anyMatch(i -> i.getType() == IngredientType.FILLING && "cutlet".equals(i.getName()));
        boolean foundDinosaur = ingredients.stream()
                .anyMatch(i -> i.getType() == IngredientType.FILLING && "dinosaur".equals(i.getName()));
        boolean foundSausage = ingredients.stream()
                .anyMatch(i -> i.getType() == IngredientType.FILLING && "sausage".equals(i.getName()));

        // Assert: проверяем, что все ожидаемые начинки найдены
        assertTrue("База должна содержать начинку 'cutlet'", foundCutlet);
        assertTrue("База должна содержать начинку 'dinosaur'", foundDinosaur);
        assertTrue("База должна содержать начинку 'sausage'", foundSausage);
    }

    /**
     * Тест проверяет корректность инициализации базы данных
     */
    @Test
    public void testConstructorInitializesDataCorrectly() {
        // Assert: проверяем, что база данных корректно инициализирована
        assertNotNull("База данных должна быть инициализирована", database);

        // Act: получаем данные из базы
        List<Bun> buns = database.availableBuns();
        List<Ingredient> ingredients = database.availableIngredients();

        // Assert: проверяем, что данные доступны
        assertNotNull("Список булочек должен быть инициализирован", buns);
        assertNotNull("Список ингредиентов должен быть инициализирован", ingredients);
        assertFalse("Список булочек должен содержать элементы", buns.isEmpty());
        assertFalse("Список ингредиентов должен содержать элементы", ingredients.isEmpty());
    }
}