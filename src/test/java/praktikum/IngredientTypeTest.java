package praktikum;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Класс для тестирования перечисления типов ингредиентов (IngredientType)
 * Тестирует базовые операции enum: values(), valueOf(), toString()
 */
public class IngredientTypeTest {

    /**
     * Тест проверяет корректность значений перечисления IngredientType
     * Проверяет количество элементов и их порядок
     */
    @Test
    public void testIngredientTypeValues() {
        // Act: получаем все значения перечисления
        IngredientType[] values = IngredientType.values();

        // Assert: проверяем количество и порядок элементов
        assertEquals("Перечисление должно содержать 2 типа ингредиентов", 2, values.length);
        assertEquals("Первый элемент должен быть SAUCE", IngredientType.SAUCE, values[0]);
        assertEquals("Второй элемент должен быть FILLING", IngredientType.FILLING, values[1]);
    }

    /**
     * Тест проверяет корректность преобразования строк в значения enum
     */
    @Test
    public void testIngredientTypeValueOf() {
        // Act: преобразуем строки в значения enum
        IngredientType sauce = IngredientType.valueOf("SAUCE");
        IngredientType filling = IngredientType.valueOf("FILLING");

        // Assert: проверяем корректность преобразования
        assertEquals("SAUCE должен корректно преобразовываться из строки", IngredientType.SAUCE, sauce);
        assertEquals("FILLING должен корректно преобразовываться из строки", IngredientType.FILLING, filling);
    }

    /**
     * Тест проверяет обработку невалидного имени при преобразовании строки в enum
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIngredientTypeValueOfWithInvalidName() {
        // Act & Assert: попытка преобразования невалидного имени должна вызвать исключение
        IngredientType.valueOf("INVALID_TYPE");
    }

    /**
     * Тест проверяет строковое представление значений enum
     */
    @Test
    public void testIngredientTypeToString() {
        // Act & Assert: проверяем строковое представление
        assertEquals("SAUCE.toString() должен возвращать 'SAUCE'", "SAUCE", IngredientType.SAUCE.toString());
        assertEquals("FILLING.toString() должен возвращать 'FILLING'", "FILLING", IngredientType.FILLING.toString());
    }

    /**
     * Тест проверяет порядковые номера значений enum
     */
    @Test
    public void testIngredientTypeOrdinal() {
        // Act & Assert: проверяем ordinal значения
        assertEquals("SAUCE должен иметь ordinal 0", 0, IngredientType.SAUCE.ordinal());
        assertEquals("FILLING должен иметь ordinal 1", 1, IngredientType.FILLING.ordinal());
    }
}
