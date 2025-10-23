package praktikum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

/**
 * Класс для тестирования модели булочки (Bun)
 * Использует параметризованное тестирование для проверки различных наборов данных
 */
@RunWith(Parameterized.class)
public class BunTest {

    // Параметры теста, передаваемые через конструктор
    private final String name;
    private final float price;

    /**
     * Конструктор для параметризованного тестирования
     * @param name название булочки
     * @param price цена булочки
     */
    public BunTest(String name, float price) {
        this.name = name;
        this.price = price;
    }

    /**
     * Метод предоставляет тестовые данные для параметризованного тестирования
     * Включает стандартные и граничные случаи
     */
    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][] {
                {"black bun", 100.0f},     // Стандартная булочка
                {"white bun", 200.0f},     // Булочка с другой ценой
                {"red bun", 300.0f},       // Еще один вариант
                {"", 0.0f},                // Граничный случай: пустое название
                {"special bun", 999.99f},  // Булочка с дробной ценой
                {"test bun", -50.0f}       // Граничный случай: отрицательная цена
        };
    }

    /**
     * Тест проверяет корректность получения названия булочки
     * Создает экземпляр Bun и проверяет, что getName() возвращает ожидаемое значение
     */
    @Test
    public void testGetNameReturnsCorrectName() {
        // Arrange: создаем объект булочки с тестовыми параметрами
        Bun bun = new Bun(name, price);

        // Act & Assert: проверяем соответствие имени
        assertEquals("Название булочки должно соответствовать переданному в конструктор",
                name, bun.getName());
    }

    /**
     * Тест проверяет корректность получения цены булочки
     * Использует дельту для сравнения float значений
     */
    @Test
    public void testGetPriceReturnsCorrectPrice() {
        // Arrange: создаем объект булочки
        Bun bun = new Bun(name, price);

        // Act & Assert: проверяем соответствие цены с учетом погрешности float
        assertEquals("Цена булочки должна соответствовать переданной в конструктор",
                price, bun.getPrice(), 0.001);
    }
}