package praktikum;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Непараметризованные тесты для класса Burger
 * Тестирует граничные случаи, исключительные ситуации и базовую функциональность
 */
public class BurgerTest {

    private Burger burger;
    private AutoCloseable mocks;

    @Mock
    private Bun mockBun;

    @Mock
    private Ingredient firstIngredient;

    @Mock
    private Ingredient secondIngredient;

    @Before
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        burger = new Burger();

        // Настройка стандартных тестовых данных
        when(mockBun.getName()).thenReturn("black bun");
        when(mockBun.getPrice()).thenReturn(100.0f);

        when(firstIngredient.getType()).thenReturn(IngredientType.SAUCE);
        when(firstIngredient.getName()).thenReturn("hot sauce");
        when(firstIngredient.getPrice()).thenReturn(50.0f);

        when(secondIngredient.getType()).thenReturn(IngredientType.FILLING);
        when(secondIngredient.getName()).thenReturn("cutlet");
        when(secondIngredient.getPrice()).thenReturn(100.0f);
    }

    @After
    public void tearDown() throws Exception {
        mocks.close();
    }

    /**
     * Тест проверяет что после установки булочки цена становится неотрицательной
     * Это базовое требование - бургер не может иметь отрицательную стоимость
     */
    @Test
    public void testSetBunsMakesPriceNonNegative() {
        // Act: устанавливаем булочку
        burger.setBuns(mockBun);

        // Assert: проверяем что цена >= 0
        float price = burger.getPrice();
        assertTrue("Цена должна быть неотрицательной после установки булочки", price >= 0);
    }

    /**
     * Тест проверяет корректность уменьшения цены при удалении ингредиента
     * Удаление ингредиента должно уменьшать стоимость на его цену
     */
    @Test
    public void testRemoveIngredientDecreasesPrice() {
        // Arrange: создаем бургер с двумя ингредиентами
        burger.setBuns(mockBun);
        burger.addIngredient(firstIngredient);  // +50
        burger.addIngredient(secondIngredient); // +100
        float priceBefore = burger.getPrice();  // 100*2 + 50 + 100 = 350

        // Act: удаляем первый ингредиент (стоимостью 50)
        burger.removeIngredient(0);

        // Assert: проверяем уменьшение цены на 50
        float priceAfter = burger.getPrice();
        assertEquals("Цена должна уменьшиться на стоимость удаленного ингредиента",
                priceBefore - 50.0f, priceAfter, 0.001);
    }

    /**
     * Тест проверяет обработку невалидного индекса при удалении
     * Попытка удаления по несуществующему индексу должна вызывать исключение
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveIngredientWithInvalidIndexThrowsException() {
        // Act & Assert: попытка удаления по невалидному индексу
        burger.removeIngredient(999);
    }

    /**
     * Тест проверяет удаление из пустого списка ингредиентов
     * Попытка удаления при отсутствии ингредиентов должна вызывать исключение
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveIngredientFromEmptyListThrowsException() {
        // Arrange: устанавливаем только булочку (ингредиентов нет)
        burger.setBuns(mockBun);

        // Act & Assert: попытка удаления из пустого списка
        burger.removeIngredient(0);
    }

    /**
     * Тест проверяет что перемещение ингредиента изменяет порядок в чеке
     * Изменение порядка ингредиентов должно отражаться в финальном чеке
     */
    @Test
    public void testMoveIngredientChangesOrder() {
        // Arrange: создаем бургер с двумя ингредиентами
        burger.setBuns(mockBun);
        burger.addIngredient(firstIngredient);  // "hot sauce"
        burger.addIngredient(secondIngredient); // "cutlet"
        String receiptBefore = burger.getReceipt();

        // Act: перемещаем ингредиенты
        burger.moveIngredient(0, 1);

        // Assert: проверяем что чек изменился
        String receiptAfter = burger.getReceipt();
        assertNotEquals("Чек должен измениться после перемещения ингредиента",
                receiptBefore, receiptAfter);
    }

    /**
     * Тест проверяет что перемещение на тот же индекс не изменяет чек
     * Операция перемещения элемента на его текущую позицию должна быть идемпотентной
     */
    @Test
    public void testMoveIngredientToSameIndexKeepsReceiptSame() {
        // Arrange: создаем бургер с ингредиентом
        burger.setBuns(mockBun);
        burger.addIngredient(firstIngredient);
        String receiptBefore = burger.getReceipt();

        // Act: перемещаем ингредиент на ту же позицию
        burger.moveIngredient(0, 0);

        // Assert: проверяем что чек не изменился
        String receiptAfter = burger.getReceipt();
        assertEquals("При перемещении на тот же индекс чек не должен меняться",
                receiptBefore, receiptAfter);
    }

    /**
     * Тест проверяет обработку невалидного индекса при перемещении
     * Попытка перемещения на несуществующую позицию должна вызывать исключение
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testMoveIngredientWithInvalidIndexThrowsException() {
        // Act & Assert: попытка перемещения на невалидный индекс
        burger.moveIngredient(0, 999);
    }

    /**
     * Тест проверяет перемещение в пустом списке ингредиентов
     * Попытка перемещения при отсутствии ингредиентов должна вызывать исключение
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testMoveIngredientInEmptyListThrowsException() {
        // Arrange: устанавливаем только булочку
        burger.setBuns(mockBun);

        // Act & Assert: попытка перемещения в пустом списке
        burger.moveIngredient(0, 0);
    }

    /**
     * Тест проверяет что получение цены без установки булочки вызывает исключение
     * Это соответствует текущей реализации класса Burger
     */
    @Test(expected = NullPointerException.class)
    public void testGetPriceWithNoBunThrowsNullPointerException() {
        // Act & Assert: попытка получить цену без булочки
        burger.getPrice();
    }

    /**
     * Тест проверяет что получение чека без установки булочки вызывает исключение
     * Чек не может быть сформирован без информации о булочке
     */
    @Test(expected = NullPointerException.class)
    public void testGetReceiptWithNoBunThrowsNullPointerException() {
        // Act & Assert: попытка получить чек без булочки
        burger.getReceipt();
    }

    /**
     * Тест проверяет наличие информации о цене в чеке
     * Чек должен содержать строку с общей стоимостью бургера
     */
    @Test
    public void testGetReceiptContainsPrice() {
        // Arrange: устанавливаем булочку
        burger.setBuns(mockBun);

        // Act: получаем чек
        String receipt = burger.getReceipt();

        // Assert: проверяем наличие цены
        assertTrue("Чек должен содержать общую стоимость", receipt.contains("Price:"));
    }

    /**
     * Тест проверяет наличие названия булочки в чеке без ингредиентов
     * Даже без ингредиентов чек должен содержать информацию о булочке
     */
    @Test
    public void testGetReceiptWithNoIngredientsContainsBunName() {
        // Arrange: устанавливаем только булочку
        burger.setBuns(mockBun);

        // Act: получаем чек
        String receipt = burger.getReceipt();

        // Assert: проверяем наличие названия булочки
        assertTrue("Чек должен содержать название булочки", receipt.contains("black bun"));
    }

    /**
     * Тест проверяет наличие разделителей в чеке
     * Чек должен иметь правильное форматирование с разделителями
     */
    @Test
    public void testGetReceiptWithNoIngredientsContainsSeparators() {
        // Arrange: устанавливаем только булочку
        burger.setBuns(mockBun);

        // Act: получаем чек
        String receipt = burger.getReceipt();

        // Assert: проверяем наличие разделителей
        assertTrue("Чек должен содержать разделители",
                receipt.contains("(====") && receipt.contains("===)"));
    }

    /**
     * Тест проверяет что после удаления ингредиента чек содержит оставшийся ингредиент
     * Удаление не должно влиять на отображение других ингредиентов
     */
    @Test
    public void testReceiptContainsRemainingIngredientAfterRemoval() {
        // Arrange: создаем бургер с двумя ингредиентами
        burger.setBuns(mockBun);
        burger.addIngredient(firstIngredient);
        burger.addIngredient(secondIngredient);

        // Act: удаляем первый ингредиент
        burger.removeIngredient(0);

        // Assert: проверяем что второй ингредиент остался в чеке
        String receipt = burger.getReceipt();
        assertTrue("Чек должен содержать оставшийся ингредиент", receipt.contains("cutlet"));
    }

    /**
     * Тест проверяет что удаленный ингредиент отсутствует в чеке
     * После удаления ингредиент не должен отображаться в финальном чеке
     */
    @Test
    public void testReceiptDoesNotContainRemovedIngredient() {
        // Arrange: создаем бургер с двумя ингредиентами
        burger.setBuns(mockBun);
        burger.addIngredient(firstIngredient);
        burger.addIngredient(secondIngredient);

        // Act: удаляем первый ингредиент
        burger.removeIngredient(0);

        // Assert: проверяем что удаленный ингредиент отсутствует
        String receipt = burger.getReceipt();
        assertFalse("Чек не должен содержать удаленный ингредиент", receipt.contains("hot sauce"));
    }

    /**
     * Тест проверяет корректность расчета цены при большом количестве ингредиентов
     * Проверяем производительность и правильность расчетов для edge case
     */
    @Test
    public void testBurgerWithManyIngredientsPriceCalculation() {
        // Arrange: устанавливаем булочку
        burger.setBuns(mockBun);

        // Act: добавляем 100 одинаковых ингредиентов
        int ingredientCount = 100;
        for (int i = 0; i < ingredientCount; i++) {
            burger.addIngredient(firstIngredient);
        }

        // Assert: проверяем корректность расчета
        float price = burger.getPrice();
        assertEquals("Цена должна корректно рассчитываться для многих ингредиентов",
                (100.0f * 2) + (50.0f * ingredientCount), price, 0.001);
    }
}