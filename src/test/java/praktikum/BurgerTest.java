package praktikum;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Класс для тестирования модели бургера (Burger)
 * Использует мокирование для изоляции тестируемого класса и параметризацию
 * Тестирует основные операции с бургером: добавление, удаление, перемещение ингредиентов,
 * расчет цены и формирование чека
 */
@RunWith(Parameterized.class)
public class BurgerTest {

    private Burger burger;
    private AutoCloseable mocks;

    // Моки зависимостей для изоляции тестируемого класса
    @Mock
    private Bun mockBun;

    @Mock
    private Ingredient mockIngredient1;

    @Mock
    private Ingredient mockIngredient2;

    @Mock
    private Ingredient mockIngredient3;

    // Параметры для параметризованного тестирования
    private final String bunName;
    private final float bunPrice;
    private final IngredientType ingredientType;
    private final String ingredientName;
    private final float ingredientPrice;

    /**
     * Конструктор для параметризованного тестирования
     */
    public BurgerTest(String bunName, float bunPrice, IngredientType ingredientType,
                      String ingredientName, float ingredientPrice) {
        this.bunName = bunName;
        this.bunPrice = bunPrice;
        this.ingredientType = ingredientType;
        this.ingredientName = ingredientName;
        this.ingredientPrice = ingredientPrice;
    }

    /**
     * Предоставляет тестовые данные для различных комбинаций бургеров
     */
    @Parameterized.Parameters(name = "Булочка: {0}, Ингредиент: {3}")
    public static Object[][] getTestData() {
        return new Object[][] {
                {"black bun", 100.0f, IngredientType.SAUCE, "hot sauce", 50.0f},
                {"white bun", 200.0f, IngredientType.FILLING, "cutlet", 100.0f},
                {"red bun", 150.0f, IngredientType.SAUCE, "sour cream", 75.0f},
                {"special bun", 250.0f, IngredientType.FILLING, "dinosaur", 150.0f}
        };
    }

    /**
     * Метод инициализации выполняется перед каждым тестом
     * Настраивает моки и создает экземпляр тестируемого класса
     */
    @Before
    public void setUp() {
        // Инициализация моков с сохранением AutoCloseable для правильного закрытия
        mocks = MockitoAnnotations.openMocks(this);
        burger = new Burger();

        // Настройка мока булочки
        when(mockBun.getName()).thenReturn(bunName);
        when(mockBun.getPrice()).thenReturn(bunPrice);

        // Настройка моков ингредиентов
        when(mockIngredient1.getType()).thenReturn(ingredientType);
        when(mockIngredient1.getName()).thenReturn(ingredientName);
        when(mockIngredient1.getPrice()).thenReturn(ingredientPrice);

        when(mockIngredient2.getType()).thenReturn(IngredientType.FILLING);
        when(mockIngredient2.getName()).thenReturn("dinosaur");
        when(mockIngredient2.getPrice()).thenReturn(200.0f);

        when(mockIngredient3.getType()).thenReturn(IngredientType.SAUCE);
        when(mockIngredient3.getName()).thenReturn("chili sauce");
        when(mockIngredient3.getPrice()).thenReturn(150.0f);
    }

    /**
     * Метод очистки выполняется после каждого теста
     * Закрывает ресурсы моков
     */
    @After
    public void tearDown() {
        try {
            if (mocks != null) {
                mocks.close();
            }
        } catch (Exception e) {
            // Логируем исключение, но не проваливаем тест
            System.err.println("Ошибка при закрытии моков: " + e.getMessage());
        }
    }

    /**
     * Тест проверяет корректность установки булочки для бургера
     * Проверяет, что цена бургера становится неотрицательной после установки булочки
     */
    @Test
    public void testSetBunsCorrectlySetsBun() {
        // Act: устанавливаем булочку
        burger.setBuns(mockBun);

        // Assert: проверяем, что цена стала неотрицательной
        float price = burger.getPrice();
        assertTrue("Цена должна быть неотрицательной после установки булочки", price >= 0);

        // Дополнительная проверка: цена должна быть равна удвоенной стоимости булочки
        assertEquals("Цена бургера с булочкой должна быть " + (bunPrice * 2),
                bunPrice * 2, price, 0.001);
    }

    /**
     * Тест проверяет, что добавление ингредиента увеличивает стоимость бургера
     */
    @Test
    public void testAddIngredientIncreasesPrice() {
        // Arrange: устанавливаем булочку и запоминаем начальную цену
        burger.setBuns(mockBun);
        float priceBefore = burger.getPrice();

        // Act: добавляем ингредиент
        burger.addIngredient(mockIngredient1);

        // Assert: проверяем увеличение цены
        float priceAfter = burger.getPrice();
        assertEquals("Цена должна увеличиться на стоимость ингредиента (" + ingredientPrice + ")",
                priceBefore + ingredientPrice, priceAfter, 0.001);
    }

    /**
     * Тест проверяет, что добавленный ингредиент отображается в чеке
     */
    @Test
    public void testAddIngredientAppearsInReceipt() {
        // Arrange: устанавливаем булочку
        burger.setBuns(mockBun);

        // Act: добавляем ингредиент и получаем чек
        burger.addIngredient(mockIngredient1);
        String receipt = burger.getReceipt();

        // Assert: проверяем наличие ингредиента в чеке
        assertTrue("Чек должен содержать название добавленного ингредиента: " + ingredientName,
                receipt.contains(ingredientName));
        assertTrue("Чек должен содержать тип ингредиента: " + ingredientType.toString().toLowerCase(),
                receipt.contains(ingredientType.toString().toLowerCase()));
    }

    /**
     * Тест проверяет, что удаление ингредиента уменьшает стоимость бургера
     */
    @Test
    public void testRemoveIngredientDecreasesPrice() {
        // Arrange: создаем бургер с двумя ингредиентами
        burger.setBuns(mockBun);
        burger.addIngredient(mockIngredient1);
        burger.addIngredient(mockIngredient2);

        // Запоминаем цену до удаления
        float priceBefore = burger.getPrice();

        // Act: удаляем ингредиент
        burger.removeIngredient(0);

        // Assert: проверяем уменьшение цены
        float priceAfter = burger.getPrice();
        assertEquals("Цена должна уменьшиться на стоимость удаленного ингредиента (" + ingredientPrice + ")",
                priceBefore - ingredientPrice, priceAfter, 0.001);
    }

    /**
     * Тест проверяет обработку невалидного индекса при удалении ингредиента
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveIngredientWithInvalidIndexThrowsException() {
        // Act & Assert: попытка удаления по несуществующему индексу должен вызвать исключение
        burger.removeIngredient(999);
    }

    /**
     * Тест проверяет удаление ингредиента из пустого списка
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveIngredientFromEmptyListThrowsException() {
        // Arrange: бургер без ингредиентов
        burger.setBuns(mockBun);

        // Act & Assert: попытка удаления из пустого списка должна вызвать исключение
        burger.removeIngredient(0);
    }

    /**
     * Тест проверяет корректность перемещения ингредиента в списке
     * Проверяет изменение порядка ингредиентов через анализ позиций в чеке
     */
    @Test
    public void testMoveIngredientChangesOrder() {
        // Arrange: создаем бургер с тремя ингредиентами
        burger.setBuns(mockBun);
        burger.addIngredient(mockIngredient1); // ingredientName
        burger.addIngredient(mockIngredient2); // "dinosaur"
        burger.addIngredient(mockIngredient3); // "chili sauce"

        // Получаем исходный порядок ингредиентов из чека
        String receiptBefore = burger.getReceipt();
        List<String> ingredientsBefore = extractIngredientLines(receiptBefore);

        // Act: перемещаем первый ингредиент в конец
        burger.moveIngredient(0, 2);

        // Assert: проверяем новый порядок через получение чека
        String receiptAfter = burger.getReceipt();
        List<String> ingredientsAfter = extractIngredientLines(receiptAfter);

        // Проверяем, что порядок изменился
        assertEquals("Количество строк с ингредиентами должно остаться прежним",
                ingredientsBefore.size(), ingredientsAfter.size());

        // После перемещения первый ингредиент должен стать последним
        assertEquals("Первый ингредиент должен переместиться в конец",
                ingredientsBefore.get(0), ingredientsAfter.get(2));
        assertEquals("Второй ингредиент должен стать первым",
                ingredientsBefore.get(1), ingredientsAfter.get(0));
        assertEquals("Третий ингредиент должен стать вторым",
                ingredientsBefore.get(2), ingredientsAfter.get(1));
    }

    /**
     * Извлекает строки с ингредиентами из чека
     */
    private List<String> extractIngredientLines(String receipt) {
        return Arrays.stream(receipt.split("\n"))
                .filter(line -> line.startsWith("= ") && line.endsWith(" ="))
                .collect(Collectors.toList());
    }

    /**
     * Тест проверяет перемещение ингредиента на тот же индекс
     */
    @Test
    public void testMoveIngredientToSameIndex() {
        // Arrange: создаем бургер с ингредиентами
        burger.setBuns(mockBun);
        burger.addIngredient(mockIngredient1);
        burger.addIngredient(mockIngredient2);

        // Запоминаем исходный чек
        String receiptBefore = burger.getReceipt();

        // Act: перемещаем ингредиент на тот же индекс
        burger.moveIngredient(0, 0);

        // Assert: чек не должен измениться
        String receiptAfter = burger.getReceipt();
        assertEquals("При перемещении на тот же индекс чек не должен меняться",
                receiptBefore, receiptAfter);
    }

    /**
     * Тест проверяет обработку невалидного индекса при перемещении ингредиента
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testMoveIngredientWithInvalidIndexThrowsException() {
        // Act & Assert: попытка перемещения по несуществующему индексу должна вызвать исключение
        burger.moveIngredient(0, 999);
    }

    /**
     * Тест проверяет перемещение ингредиента в пустом списке
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testMoveIngredientInEmptyListThrowsException() {
        // Arrange: бургер без ингредиентов
        burger.setBuns(mockBun);

        // Act & Assert: попытка перемещения в пустом списке должна вызвать исключение
        burger.moveIngredient(0, 0);
    }

    /**
     * Тест проверяет корректность расчета общей стоимости бургера с булочкой и ингредиентами
     */
    @Test
    public void testGetPriceWithBunAndIngredients() {
        // Arrange: устанавливаем булочку и добавляем ингредиенты
        burger.setBuns(mockBun);
        burger.addIngredient(mockIngredient1);
        burger.addIngredient(mockIngredient2);

        // Calculate expected price: (булочка * 2) + ингредиент1 + ингредиент2
        float expectedPrice = (bunPrice * 2) + ingredientPrice + 200.0f;

        // Act: получаем фактическую цену
        float actualPrice = burger.getPrice();

        // Assert: проверяем расчет цены
        assertEquals("Расчет стоимости бургера должен быть корректным. Ожидалось: " + expectedPrice + ", получено: " + actualPrice,
                expectedPrice, actualPrice, 0.001);
    }

    /**
     * Тест проверяет расчет стоимости бургера только с булочкой
     */
    @Test
    public void testGetPriceWithOnlyBun() {
        // Arrange: устанавливаем только булочку
        burger.setBuns(mockBun);

        // Calculate expected price: цена булочки * 2
        float expectedPrice = bunPrice * 2;

        // Act: получаем фактическую цену
        float actualPrice = burger.getPrice();

        // Assert: проверяем, что цена равна удвоенной стоимости булочки
        assertEquals("Стоимость бургера только с булочкой должна быть равна цене булочки * 2 (" + expectedPrice + ")",
                expectedPrice, actualPrice, 0.001);
    }

    /**
     * Тест проверяет, что вызов getPrice() без установки булочки вызывает NullPointerException
     * Это соответствует текущей реализации класса Burger
     */
    @Test(expected = NullPointerException.class)
    public void testGetPriceWithNoBunThrowsNullPointerException() {
        // Act & Assert: попытка получить цену без установки булочки должна вызвать NullPointerException
        burger.getPrice();
    }

    /**
     * Тест проверяет корректность формирования чека бургера
     * Проверяет наличие всех обязательных элементов в чеке
     */
    @Test
    public void testGetReceiptContainsAllRequiredElements() {
        // Arrange: создаем бургер с булочкой и одним ингредиентом
        burger.setBuns(mockBun);
        burger.addIngredient(mockIngredient1);

        // Act: получаем чек
        String receipt = burger.getReceipt();

        // Assert: проверяем содержание чека
        assertNotNull("Чек не должен быть null", receipt);
        assertTrue("Чек должен содержать название булочки: " + bunName, receipt.contains(bunName));
        assertTrue("Чек должен содержать название ингредиента: " + ingredientName, receipt.contains(ingredientName));
        assertTrue("Чек должен содержать тип ингредиента: " + ingredientType.toString().toLowerCase(),
                receipt.contains(ingredientType.toString().toLowerCase()));
        assertTrue("Чек должен содержать общую стоимость", receipt.contains("Price:"));
        assertTrue("Чек должен содержать разделители",
                receipt.contains("(====") && receipt.contains("===)"));

        // Проверяем структуру чека
        String[] lines = receipt.split("\n");
        assertTrue("Чек должен содержать как минимум 4 строки", lines.length >= 4);
        assertTrue("Первая строка должна содержать булочку", lines[0].contains(bunName));
        assertTrue("Одна из строк должна содержать ингредиент",
                Arrays.stream(lines).anyMatch(line -> line.contains(ingredientName)));
    }

    /**
     * Тест проверяет формирование чека для бургера без ингредиентов
     */
    @Test
    public void testGetReceiptWithNoIngredients() {
        // Arrange: устанавливаем только булочку
        burger.setBuns(mockBun);

        // Act: получаем чек
        String receipt = burger.getReceipt();

        // Assert: проверяем базовое содержание чека
        assertNotNull("Чек не должен быть null даже без ингредиентов", receipt);
        assertTrue("Чек должен содержать название булочки: " + bunName, receipt.contains(bunName));
        assertTrue("Чек должен содержать общую стоимость", receipt.contains("Price:"));

        // Проверяем, что нет строк с ингредиентами (только булочка и цена)
        String[] lines = receipt.split("\n");
        // Чек без ингредиентов содержит: верхняя булочка, нижняя булочка, пустая строка, цена = 4 строки
        assertEquals("Чек без ингредиентов должен содержать 4 строки", 4, lines.length);
    }

    /**
     * Тест проверяет, что вызов getReceipt() без установки булочки вызывает NullPointerException
     * Это соответствует текущей реализации класса Burger
     */
    @Test(expected = NullPointerException.class)
    public void testGetReceiptWithNoBunThrowsNullPointerException() {
        // Act & Assert: попытка получить чек без установки булочки должна вызвать NullPointerException
        burger.getReceipt();
    }

    /**
     * Тест проверяет последовательность операций с бургером
     * Проверяет согласованность состояния после нескольких операций
     */
    @Test
    public void testMultipleOperationsSequence() {
        // Arrange: создаем бургер и добавляем ингредиенты с уникальными именами
        burger.setBuns(mockBun);

        // Используем mockIngredient1 и mockIngredient3 с гарантированно разными именами
        burger.addIngredient(mockIngredient1); // ingredientName (например, "hot sauce", "cutlet", etc.)
        burger.addIngredient(mockIngredient3); // "chili sauce" (гарантированно отличается от ingredientName)

        // Запоминаем цену после добавления
        float priceAfterAdd = burger.getPrice();

        // Act: удаляем первый ингредиент (mockIngredient1)
        burger.removeIngredient(0);
        float priceAfterRemove = burger.getPrice();

        // Assert: проверяем изменение цены
        assertTrue("Цена после удаления (" + priceAfterRemove + ") должна быть меньше цены после добавления (" + priceAfterAdd + ")",
                priceAfterRemove < priceAfterAdd);

        // Проверяем содержание чека
        String receipt = burger.getReceipt();

        // Извлекаем только строки с ингредиентами для точной проверки
        List<String> ingredientLines = extractIngredientLines(receipt);

        // Должен остаться только один ингредиент - "chili sauce" (mockIngredient3)
        assertEquals("В чеке должен остаться только один ингредиент", 1, ingredientLines.size());
        assertTrue("Оставшийся ингредиент должен быть 'chili sauce'",
                ingredientLines.get(0).contains("chili sauce"));

        // Проверяем что удаленный ингредиент отсутствует
        boolean containsRemovedIngredient = ingredientLines.stream()
                .anyMatch(line -> line.contains(ingredientName));
        assertFalse("Чек не должен содержать удаленный ингредиент: " + ingredientName,
                containsRemovedIngredient);
    }

    /**
     * Тест производительности для большого количества ингредиентов
     */
    @Test
    public void testBurgerWithManyIngredientsPerformance() {
        // Arrange: устанавливаем булочку
        burger.setBuns(mockBun);

        // Act: добавляем много ингредиентов
        int ingredientCount = 100;
        for (int i = 0; i < ingredientCount; i++) {
            burger.addIngredient(mockIngredient1);
        }

        // Assert: проверяем что операции выполняются за разумное время
        long startTime = System.currentTimeMillis();
        float price = burger.getPrice();
        // Используем receipt для проверки, что чек формируется корректно
        String receipt = burger.getReceipt();
        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;
        assertTrue("Операции с большим количеством ингредиентов должны выполняться быстро (< 1000ms)", duration < 1000);
        assertEquals("Цена должна корректно рассчитываться для многих ингредиентов",
                (bunPrice * 2) + (ingredientPrice * ingredientCount), price, 0.001);

        // Дополнительная проверка: чек должен содержать все ингредиенты
        assertNotNull("Чек не должен быть null", receipt);
        assertTrue("Чек должен содержать название булочки", receipt.contains(bunName));
    }

    /**
     * Тест проверяет различные комбинации перемещения ингредиентов
     */
    @Test
    public void testMoveIngredientVariousCombinations() {
        // Arrange: создаем бургер с тремя ингредиентами
        burger.setBuns(mockBun);
        burger.addIngredient(mockIngredient1);
        burger.addIngredient(mockIngredient2);
        burger.addIngredient(mockIngredient3);

        // Запоминаем исходную цену
        float originalPrice = burger.getPrice();

        // Тестируем различные комбинации перемещения
        int[][] moveCombinations = {
                {0, 1}, {1, 0}, {0, 2}, {2, 0}, {1, 2}, {2, 1}
        };

        for (int[] combination : moveCombinations) {
            int fromIndex = combination[0];
            int toIndex = combination[1];

            // Act: перемещаем ингредиент
            burger.moveIngredient(fromIndex, toIndex);

            // Assert: проверяем что цена не изменилась (только порядок)
            assertEquals("Цена не должна меняться при перемещении ингредиентов с " + fromIndex + " на " + toIndex,
                    originalPrice, burger.getPrice(), 0.001);

            // Возвращаем на место для следующего теста
            burger.moveIngredient(toIndex, fromIndex);
        }
    }
}