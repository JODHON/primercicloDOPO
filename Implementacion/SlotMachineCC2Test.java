import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Pruebas de unidad del ciclo 2: agregar/eliminar ruedas y simbolos,
 * y deteccion de jackpot.
 *
 * Las pruebas se ejecutan con la maquina en modo invisible.
 *
 * @author Andres
 */
public class SlotMachineCC2Test
{
    private SlotMachine machine;

    @Before
    public void setUp()
    {
        machine = new SlotMachine();
        machine.makeInvisible();
    }

    // ---------- isJackpot ----------

    @Test
    public void accordingCGIsJackpotShouldBeFalseWithOnlyOneWheelEvenIfSymbolIsSet()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.placeSymbol(1, "red");

        assertFalse(machine.isJackpot());
    }

    // ---------- addSymbol ----------

    @Test
    public void accordingCGAddSymbolShouldFailWhenColorAlreadyExists()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");

        machine.addSymbol(1, "red");

        assertFalse(machine.ok());
    }
}