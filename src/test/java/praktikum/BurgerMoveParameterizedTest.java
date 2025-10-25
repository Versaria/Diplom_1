package praktikum;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Параметризованные тесты для операции перемещения ингредиентов
 * Проверяем различные комбинации перемещения без изменения общей стоимости
 */
@RunWith(Parameterized.class)
public class BurgerMoveParameterizedTest {

    private Burger burger;
    private AutoCloseable mocks;

    @Mock
    private Bun mockBun;

    @Mock
    private Ingredient firstIngredient;

    @Mock
    private Ingredient secondIngredient;

    @Mock
    private Ingredient thirdIngredient;

    // Параметры для тестирования перемещения: исходный и целевой индексы
    private final int fromIndex;
    private final int toIndex;

    public BurgerMoveParameterizedTest(int fromIndex, int toIndex) {
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
    }

    /**
     * Тестовые данные: различные комбинации перемещения ингредиентов
     * Проверяем перемещение между всеми возможными позициями
     */
    @Parameterized.Parameters
    public static Object[][] getMoveCombinations() {
        return new Object[][] {
                {0, 1}, // Первый -> Второй
                {1, 0}, // Второй -> Первый
                {0, 2}, // Первый -> Третий
                {2, 0}, // Третий -> Первый
                {1, 2}, // Второй -> Третий
                {2, 1}  // Третий -> Второй
        };
    }

    @Before
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        burger = new Burger();

        // Настройка стандартной булочки
        when(mockBun.getName()).thenReturn("black bun");
        when(mockBun.getPrice()).thenReturn(100.0f);

        // Настройка трех различных ингредиентов
        when(firstIngredient.getType()).thenReturn(IngredientType.SAUCE);
        when(firstIngredient.getName()).thenReturn("hot sauce");
        when(firstIngredient.getPrice()).thenReturn(50.0f);

        when(secondIngredient.getType()).thenReturn(IngredientType.FILLING);
        when(secondIngredient.getName()).thenReturn("cutlet");
        when(secondIngredient.getPrice()).thenReturn(100.0f);

        when(thirdIngredient.getType()).thenReturn(IngredientType.SAUCE);
        when(thirdIngredient.getName()).thenReturn("chili sauce");
        when(thirdIngredient.getPrice()).thenReturn(150.0f);

        // Создаем бургер с тремя ингредиентами
        burger.setBuns(mockBun);
        burger.addIngredient(firstIngredient);
        burger.addIngredient(secondIngredient);
        burger.addIngredient(thirdIngredient);
    }

    @After
    public void tearDown() throws Exception {
        mocks.close();
    }

    /**
     * Тест проверяет что перемещение ингредиентов не влияет на общую стоимость
     * Независимо от порядка ингредиентов, цена бургера должна оставаться неизменной
     * Это важно для обеспечения консистентности расчетов
     */
    @Test
    public void testMoveIngredientPriceRemainsSame() {
        // Arrange: запоминаем исходную цену
        float originalPrice = burger.getPrice();

        // Act: перемещаем ингредиент согласно параметрам теста
        burger.moveIngredient(fromIndex, toIndex);

        // Assert: проверяем что цена не изменилась
        assertEquals("Цена не должна меняться при перемещении ингредиентов",
                originalPrice, burger.getPrice(), 0.001);
    }
}