package praktikum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

/**
 * Класс для тестирования модели ингредиента (Ingredient)
 * Тестирует различные комбинации типов, названий и цен ингредиентов
 * Включает тесты на граничные случаи
 */
@RunWith(Parameterized.class)
public class IngredientTest {

    // Параметры теста
    private final IngredientType type;
    private final String name;
    private final float price;

    /**
     * Конструктор для параметризованного тестирования ингредиентов
     * @param type тип ингредиента (SAUCE или FILLING)
     * @param name название ингредиента
     * @param price цена ингредиента
     */
    public IngredientTest(IngredientType type, String name, float price) {
        this.type = type;
        this.name = name;
        this.price = price;
    }

    /**
     * Предоставляет тестовые данные для ингредиентов
     * Включает соусы, начинки и граничные случаи
     */
    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][] {
                // Соусы
                {IngredientType.SAUCE, "hot sauce", 100.0f},
                {IngredientType.SAUCE, "sour cream", 200.0f},
                {IngredientType.SAUCE, "chili sauce", 300.0f},
                // Начинки
                {IngredientType.FILLING, "cutlet", 100.0f},
                {IngredientType.FILLING, "dinosaur", 200.0f},
                {IngredientType.FILLING, "sausage", 300.0f},
                // Граничные случаи
                {IngredientType.SAUCE, "", 0.0f},
                {IngredientType.FILLING, "test", -50.0f}
        };
    }

    /**
     * Тест проверяет корректность получения цены ингредиента
     */
    @Test
    public void testGetPriceReturnsCorrectPrice() {
        // Arrange: создаем ингредиент с тестовыми параметрами
        Ingredient ingredient = new Ingredient(type, name, price);

        // Act & Assert: проверяем цену
        assertEquals("Цена ингредиента должна соответствовать переданной в конструктор",
                price, ingredient.getPrice(), 0.001);
    }

    /**
     * Тест проверяет корректность получения названия ингредиента
     */
    @Test
    public void testGetNameReturnsCorrectName() {
        // Arrange: создаем ингредиент
        Ingredient ingredient = new Ingredient(type, name, price);

        // Act & Assert: проверяем название
        assertEquals("Название ингредиента должно соответствовать переданному в конструктор",
                name, ingredient.getName());
    }

    /**
     * Тест проверяет корректность получения типа ингредиента
     */
    @Test
    public void testGetTypeReturnsCorrectType() {
        // Arrange: создаем ингредиент
        Ingredient ingredient = new Ingredient(type, name, price);

        // Act & Assert: проверяем тип
        assertEquals("Тип ингредиента должен соответствовать переданному в конструктор",
                type, ingredient.getType());
    }
}