package praktikum;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Параметризованные тесты для класса Burger
 * Тестирует основные операции с различными комбинациями булочек и ингредиентов
 */
@RunWith(Parameterized.class)
public class BurgerParameterizedTest {

    private Burger burger;
    private AutoCloseable mocks;

    @Mock
    private Bun mockBun;

    @Mock
    private Ingredient mockIngredient;

    // Параметры для тестирования различных комбинаций
    private final String bunName;
    private final float bunPrice;
    private final IngredientType ingredientType;
    private final String ingredientName;
    private final float ingredientPrice;

    public BurgerParameterizedTest(String bunName, float bunPrice, IngredientType ingredientType,
                                   String ingredientName, float ingredientPrice) {
        this.bunName = bunName;
        this.bunPrice = bunPrice;
        this.ingredientType = ingredientType;
        this.ingredientName = ingredientName;
        this.ingredientPrice = ingredientPrice;
    }

    /**
     * Тестовые данные: различные комбинации булочек и ингредиентов
     * Проверяем работу с разными названиями, ценами и типами ингредиентов
     */
    @Parameterized.Parameters(name = "Булочка: {0}, Ингредиент: {3}")
    public static Object[][] getTestData() {
        return new Object[][] {
                {"black bun", 100.0f, IngredientType.SAUCE, "hot sauce", 50.0f},
                {"white bun", 200.0f, IngredientType.FILLING, "cutlet", 100.0f},
                {"red bun", 150.0f, IngredientType.SAUCE, "sour cream", 75.0f}
        };
    }

    @Before
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        burger = new Burger();

        // Настройка мока булочки с переданными параметрами
        when(mockBun.getName()).thenReturn(bunName);
        when(mockBun.getPrice()).thenReturn(bunPrice);

        // Настройка мока ингредиента с переданными параметрами
        when(mockIngredient.getType()).thenReturn(ingredientType);
        when(mockIngredient.getName()).thenReturn(ingredientName);
        when(mockIngredient.getPrice()).thenReturn(ingredientPrice);
    }

    @After
    public void tearDown() throws Exception {
        mocks.close();
    }

    /**
     * Тест проверяет базовый расчет стоимости бургера
     * Цена бургера с булочкой должна быть равна удвоенной цене булочки
     * Это основа для всех последующих расчетов
     */
    @Test
    public void testSetBunsPriceEqualsBunPriceMultipliedByTwo() {
        // Arrange: устанавливаем булочку
        burger.setBuns(mockBun);

        // Act: получаем цену
        float actualPrice = burger.getPrice();

        // Assert: проверяем что цена = цена булочки * 2
        assertEquals("Цена бургера с булочкой должна быть равна цене булочки * 2",
                bunPrice * 2, actualPrice, 0.001);
    }

    /**
     * Тест проверяет корректность добавления стоимости ингредиента
     * При добавлении ингредиента цена должна увеличиться на его стоимость
     * Проверяем математическую корректность расчета
     */
    @Test
    public void testAddIngredientIncreasesPrice() {
        // Arrange: устанавливаем булочку и запоминаем начальную цену
        burger.setBuns(mockBun);
        float priceBefore = burger.getPrice();

        // Act: добавляем ингредиент
        burger.addIngredient(mockIngredient);

        // Assert: проверяем увеличение цены
        float priceAfter = burger.getPrice();
        assertEquals("Цена должна увеличиться на стоимость ингредиента",
                priceBefore + ingredientPrice, priceAfter, 0.001);
    }

    /**
     * Тест проверяет отражение добавленного ингредиента в чеке
     * Добавленный ингредиент должен отображаться в финальном чеке
     * Это важно для корректного формирования заказа
     */
    @Test
    public void testAddIngredientAppearsInReceipt() {
        // Arrange: устанавливаем булочку
        burger.setBuns(mockBun);

        // Act: добавляем ингредиент и получаем чек
        burger.addIngredient(mockIngredient);
        String receipt = burger.getReceipt();

        // Assert: проверяем наличие ингредиента в чеке
        assertTrue("Чек должен содержать название добавленного ингредиента",
                receipt.contains(ingredientName));
    }

    /**
     * Тест проверяет комплексный расчет стоимости
     * Цена бургера = (цена булочки * 2) + цена ингредиента
     * Проверяем правильность формулы расчета
     */
    @Test
    public void testGetPriceWithBunAndIngredient() {
        // Arrange: создаем бургер с булочкой и ингредиентом
        burger.setBuns(mockBun);
        burger.addIngredient(mockIngredient);

        // Calculate: рассчитываем ожидаемую цену
        float expectedPrice = (bunPrice * 2) + ingredientPrice;

        // Act: получаем фактическую цену
        float actualPrice = burger.getPrice();

        // Assert: сравниваем ожидаемую и фактическую цены
        assertEquals("Расчет стоимости бургера должен быть корректным",
                expectedPrice, actualPrice, 0.001);
    }

    /**
     * Тест проверяет отображение названия булочки в чеке
     * Название булочки должно присутствовать в верхней и нижней части чека
     * Это важно для идентификации бургера
     */
    @Test
    public void testGetReceiptContainsBunName() {
        // Arrange: создаем бургер с булочкой и ингредиентом
        burger.setBuns(mockBun);
        burger.addIngredient(mockIngredient);

        // Act: получаем чек
        String receipt = burger.getReceipt();

        // Assert: проверяем наличие названия булочки
        assertTrue("Чек должен содержать название булочки", receipt.contains(bunName));
    }
}