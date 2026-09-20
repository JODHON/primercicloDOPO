import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class SlotMachineContestTest
{
    private InputStream originalIn;

    @Before
    public void setUp()
    {
        originalIn = System.in;
    }

    @After
    public void tearDown()
    {
        System.setIn(originalIn);
    }

    private void feedInput(String canned)
    {
        System.setIn(new ByteArrayInputStream(canned.getBytes()));
    }

    @Test
    public void solveShouldTerminateOnSampleInteractionOne()
    {
        feedInput("4\n3\n3\n3\n2\n1\n");

        int[][] actions = new SlotMachineContest().solve(5);

        assertArrayEquals(new int[][]{
            {2, 1}, {2, 1}, {2, 1}, {2, 1}, {3, 1}
        }, actions);
    }

    @Test
    public void solveShouldTerminateOnSampleInteractionTwo()
    {
        feedInput("3\n2\n2\n1\n");

        int[][] actions = new SlotMachineContest().solve(3);

        assertArrayEquals(new int[][]{
            {2, 1}, {2, 1}, {2, -1}
        }, actions);
    }
}