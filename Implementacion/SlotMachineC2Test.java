import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Pruebas de unidad del ciclo 2: intercambiar ruedas, fijar/soltar ruedas,
 * girar por pasos y dejar la maquina en una configuracion dada.
 *
 * Las pruebas se ejecutan con la maquina en modo invisible.
 *
 * @author Jose
 */
public class SlotMachineC2Test
{
    private SlotMachine machine;

    @Before
    public void setUp()
    {
        machine = new SlotMachine();
        machine.makeInvisible();
    }

    // ---------- swap ----------

    @Test
    public void swapShouldExchangePositionsOfTwoValidWheels()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");

        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");

        machine.swap(1, 2);

        assertTrue(machine.ok());
        assertArrayEquals(new String[]{"blue", "red"}, machine.configuration());
    }

    @Test
    public void swapShouldFailWithInvalidWheelPosition()
    {
        machine.addWheel(1);
        machine.addWheel(2);

        machine.swap(1, 5);

        assertFalse(machine.ok());
    }

    // ---------- lock / unlock ----------

    @Test
    public void lockShouldPreventLockedWheelFromSpinning()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");
        machine.placeSymbol(1, "red");

        machine.lock(1);
        machine.spin(1);

        assertFalse(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }

    @Test
    public void lockShouldFailWhenWheelIsAlreadyLocked()
    {
        machine.addWheel(1);
        machine.lock(1);

        machine.lock(1);

        assertFalse(machine.ok());
    }

    @Test
    public void lockShouldFailWithInvalidWheelPosition()
    {
        machine.addWheel(1);

        machine.lock(5);

        assertFalse(machine.ok());
    }

    @Test
    public void unlockShouldAllowWheelToSpinAgain()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.lock(1);

        machine.unlock(1);
        machine.spin(1);

        assertTrue(machine.ok());
    }

    @Test
    public void unlockShouldFailWhenWheelIsNotLocked()
    {
        machine.addWheel(1);

        machine.unlock(1);

        assertFalse(machine.ok());
    }

    // ---------- spin(wheel, steps) ----------

    @Test
    public void spinWithStepsShouldAdvanceVisibleSymbolByGivenSteps()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");

        machine.spin(1, 2);

        assertTrue(machine.ok());
        assertEquals("green", machine.configuration()[0]);
    }

    @Test
    public void spinWithStepsShouldFailOnLockedWheel()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.lock(1);

        machine.spin(1, 1);

        assertFalse(machine.ok());
    }

    @Test
    public void spinWithStepsShouldFailWithNegativeSteps()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");

        machine.spin(1, -1);

        assertFalse(machine.ok());
    }

    @Test
    public void spinWithStepsShouldFailWithInvalidWheelPosition()
    {
        machine.addWheel(1);

        machine.spin(3, 1);

        assertFalse(machine.ok());
    }

    // ---------- spin(setSymbols) ----------

    @Test
    public void spinWithConfigurationShouldSetAllWheelsToGivenSymbols()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");

        machine.spin(new String[]{"red", "blue"});

        assertTrue(machine.ok());
        assertArrayEquals(new String[]{"red", "blue"}, machine.configuration());
    }

    @Test
    public void spinWithConfigurationShouldFailWhenLengthDoesNotMatchWheelCount()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");

        machine.spin(new String[]{"red"});

        assertFalse(machine.ok());
    }

    @Test
    public void spinWithConfigurationShouldFailWhenSymbolDoesNotExist()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");

        machine.spin(new String[]{"purple"});

        assertFalse(machine.ok());
        assertNull(machine.configuration()[0]);
    }

    @Test
    public void spinWithConfigurationShouldNotChangeLockedWheels()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");

        machine.placeSymbol(1, "red");
        machine.lock(1);

        machine.spin(new String[]{"red", "blue"});

        assertTrue(machine.ok());
        assertEquals("red", machine.configuration()[0]);
        assertEquals("blue", machine.configuration()[1]);
    }

    // ---------- spin() / spin(wheel) deben respetar ruedas fijas ----------

    @Test
    public void spinAllShouldNotChangeLockedWheelSymbol()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");

        machine.placeSymbol(1, "red");
        machine.lock(1);

        machine.spin();

        assertTrue(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }

    @Test
    public void spinSingleShouldFailWhenWheelIsLocked()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.lock(1);

        machine.spin(1);

        assertFalse(machine.ok());
    }
}