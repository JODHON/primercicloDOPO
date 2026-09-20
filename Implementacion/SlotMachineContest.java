import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SlotMachineContest
{
    private static final int max_actions = 10000;
    private static final int max_passes = 3;

    /**
     * Resuelve el problema interactivo: lee k de System.in y escribe
     * las acciones (i j) en System.out hasta que k == 1.
     */
public int[][] solve(int n)
{
    Scanner in = new Scanner(System.in);
    List<int[]> actions = new ArrayList<>();

    int k = in.nextInt();
    boolean jackpotReached = (k == 1);

    for(int pass = 0; pass < max_passes && k > 1 && !jackpotReached; pass++) {
        for(int wheel = 2; wheel <= n && k > 1 && !jackpotReached; wheel++) {

            int bestStep = 0;
            int bestK = k;

            for(int step = 1; step < n && k > 1 && !jackpotReached
                    && actions.size() < max_actions; step++) {

                System.out.println(wheel + " " + 1);
                System.out.flush();
                actions.add(new int[]{wheel, 1});

                k = in.nextInt();
                if(k == 1) {
                    jackpotReached = true;
                }
                else if(k < bestK) {
                    bestK = k;
                    bestStep = step;
                }
            }

            if(!jackpotReached) {
                int back = -((n - 1) - bestStep);
                if(back != 0 && actions.size() < max_actions) {
                    System.out.println(wheel + " " + back);
                    System.out.flush();
                    actions.add(new int[]{wheel, back});

                    k = in.nextInt();
                    if(k == 1) {
                        jackpotReached = true;
                    }
                }
            }
        }
    }

    return actions.toArray(new int[0][]);
}

    /**
     * Simulacion local usando una SlotMachine propia 
     * al final el valor de k alcanzado (1 = exito).
     */
    public void simulate(int n)
{
    SlotMachine machine = new SlotMachine(n);
    machine.makeVisible();

    int wheelCount = machine.wheelCount();
    int k = machine.distinctSymbols();
    boolean jackpotReached = (k == 1);

    for(int pass = 0; pass < max_passes && k > 1 && !jackpotReached; pass++) {
        for(int wheel = 2; wheel <= wheelCount && k > 1 && !jackpotReached; wheel++) {

            int bestStep = 0;
            int bestK = k;

            for(int step = 1; step < wheelCount && k > 1 && !jackpotReached; step++) {
                machine.rotate(wheel, 1);
                System.out.println(wheel + " " + 1);

                k = machine.distinctSymbols();
                if(k < bestK) {
                    bestK = k;
                    bestStep = step;
                }
            }

            if(!jackpotReached) {
                int back = -((wheelCount - 1) - bestStep);
                if(back != 0) {
                    machine.rotate(wheel, back);
                    System.out.println(wheel + " " + back);
                    k = machine.distinctSymbols();
                }

                if(k == 1) {
                    jackpotReached = true;
                }
            }
        }
    }

    System.out.println(k);
}

/**
 * Lee la entrada y ejecuta la solucion
 */
    
    public static void main(String[] args)
{
    Scanner in = new Scanner(System.in);
    int n = in.nextInt();
    new SlotMachineContest().solve(n);
}
}