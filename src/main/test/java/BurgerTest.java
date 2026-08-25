import praktikum.Bun;
import praktikum.Burger;
import praktikum.Ingredient;
import praktikum.IngredientType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BurgerTest {

    @Mock
    private Bun bun;

    @Mock
    private Ingredient sauce;

    @Mock
    private Ingredient filling;

    @Test
    public void TestSetBunsSetsBun() {
        Burger burger = new Burger();
        burger.setBuns(bun);

        assertSame(bun, burger.bun);
    }

    @Test
    public void TestAddIngredientAddsToList() {
        Burger burger = new Burger();
        burger.addIngredient(sauce);

        assertEquals(1, burger.ingredients.size());
        assertSame(sauce, burger.ingredients.get(0));
    }

    @Test
    public void TestRemoveIngredientRemovesFromList() {
        Burger burger = new Burger();
        burger.addIngredient(sauce);
        burger.addIngredient(filling);
        burger.removeIngredient(0);

        assertEquals(1, burger.ingredients.size());
        assertSame(filling, burger.ingredients.get(0));
    }

    @Test
    public void TestRemoveIngredientWithWrongIndexThrowsException() {
        Burger burger = new Burger();
        try {
            burger.removeIngredient(0);
            fail("Ожидалось исключение");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Index 0 out of bounds for length 0", e.getMessage());
        }
    }

    @Test
    public void TestMoveIngredientChangesOrder() {
        Burger burger = new Burger();
        burger.addIngredient(sauce);
        burger.addIngredient(filling);
        burger.moveIngredient(0, 1);

        assertSame(filling, burger.ingredients.get(0));
        assertSame(sauce, burger.ingredients.get(1));
    }

    @Test
    public void TestGetPriceWithoutIngredients() {
        when(bun.getPrice()).thenReturn(100F);
        Burger burger = new Burger();
        burger.setBuns(bun);

        assertEquals(200F, burger.getPrice(), 0);
    }

    @Test
    public void TestGetPriceWithIngredients() {
        when(bun.getPrice()).thenReturn(100F);
        when(sauce.getPrice()).thenReturn(50F);
        when(filling.getPrice()).thenReturn(150F);
        Burger burger = new Burger();
        burger.setBuns(bun);
        burger.addIngredient(sauce);
        burger.addIngredient(filling);

        assertEquals(400F, burger.getPrice(), 0);
    }

    @Test
    public void TestGetPriceCallsBunGetPriceOnce() {
        when(bun.getPrice()).thenReturn(100F);
        Burger burger = new Burger();
        burger.setBuns(bun);
        burger.getPrice(); // вызываем, но не проверяем результат

        verify(bun, times(1)).getPrice();
    }

    @Test
    public void TestGetPriceCallsIngredientGetPriceOnce() {
        when(bun.getPrice()).thenReturn(100F);
        when(sauce.getPrice()).thenReturn(50F);
        Burger burger = new Burger();
        burger.setBuns(bun);
        burger.addIngredient(sauce);
        burger.getPrice();

        verify(sauce, times(1)).getPrice();
    }

    @Test
    public void TestGetReceiptWithIngredients() {
        when(bun.getName()).thenReturn("black bun");
        when(bun.getPrice()).thenReturn(100F);
        when(sauce.getName()).thenReturn("hot sauce");
        when(sauce.getType()).thenReturn(IngredientType.SAUCE);
        when(sauce.getPrice()).thenReturn(100F);
        Burger burger = new Burger();
        burger.setBuns(bun);
        burger.addIngredient(sauce);

        String expectedReceipt = String.format("(==== %s ====)%n", "black bun")
                + String.format("= %s %s =%n", "sauce", "hot sauce")
                + String.format("(==== %s ====)%n", "black bun")
                + String.format("%nPrice: %f%n", 300F);

        assertEquals(expectedReceipt, burger.getReceipt());
    }

    @Test
    public void TestGetReceiptWithoutIngredients() {
        when(bun.getName()).thenReturn("white bun");
        when(bun.getPrice()).thenReturn(200F);
        Burger burger = new Burger();
        burger.setBuns(bun);

        String expectedReceipt = String.format("(==== %s ====)%n", "white bun")
                + String.format("(==== %s ====)%n", "white bun")
                + String.format("%nPrice: %f%n", 400F);

        assertEquals(expectedReceipt, burger.getReceipt());
    }
}
