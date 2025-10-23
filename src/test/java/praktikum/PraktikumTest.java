package praktikum;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Класс для тестирования главного класса приложения (Praktikum)
 * Тестирует выполнение main метода и корректность вывода в консоль
 */
public class PraktikumTest {

    /**
     * Тест проверяет корректность вывода main метода в консоль
     * Перехватывает System.out для анализа выводимого текста
     * Проверяет наличие ключевых элементов в выводе
     */
    @Test
    public void testMainMethodOutputContainsExpectedElements() {
        // Arrange: создаем буфер для перехвата вывода и сохраняем оригинальный System.out
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Act: запускаем main метод приложения
            Praktikum.main(new String[]{});

            // Получаем перехваченный вывод
            String output = outputStream.toString();

            // Assert: проверяем наличие ожидаемых элементов в выводе
            assertTrue("Вывод должен содержать название булочки",
                    output.contains("black bun") || output.contains("white bun") || output.contains("red bun"));
            assertTrue("Вывод должен содержать название ингредиента",
                    output.contains("sour cream") || output.contains("cutlet") || output.contains("dinosaur"));
            assertTrue("Вывод должен содержать общую стоимость", output.contains("Price:"));
            assertTrue("Вывод должен содержать формат чека с разделителями",
                    output.contains("(====") && output.contains("===)"));
            assertTrue("Вывод должен содержать конкретную структуру чека",
                    output.contains("(==== black bun ====)"));

        } finally {
            // Восстанавливаем оригинальный System.out
            System.setOut(originalOut);
        }
    }

    /**
     * Тест проверяет, что main метод выполняется без исключений
     * Это smoke-тест для проверки базовой работоспособности приложения
     */
    @Test
    public void testMainMethodExecutesWithoutExceptions() {
        try {
            // Act: запускаем main метод
            Praktikum.main(new String[]{});

        } catch (Exception e) {
            // Assert: если возникло исключение - тест провален
            throw new AssertionError("Метод main не должен выбрасывать исключения", e);
        }
    }

    /**
     * Тест проверяет обработку null аргументов в main методе
     */
    @Test
    public void testMainMethodWithNullArgs() {
        try {
            // Act: запускаем main метод с null аргументами
            Praktikum.main(null);

        } catch (Exception e) {
            // Assert: если возникло исключение - тест провален
            throw new AssertionError("Метод main должен корректно обрабатывать null аргументы", e);
        }
    }

    /**
     * Тест проверяет создание экземпляра класса Praktikum через рефлексию
     * Используется для достижения 100% покрытия кода
     */
    @Test
    public void testPraktikumConstructorViaReflection() throws Exception {
        // Arrange & Act: получаем конструктор и создаем экземпляр через рефлексию
        Constructor<Praktikum> constructor = Praktikum.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Praktikum instance = constructor.newInstance();

        // Assert: проверяем, что объект успешно создан
        assertNotNull("Экземпляр класса Praktikum должен быть создан через рефлексию", instance);
    }
}