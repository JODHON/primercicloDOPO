import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SlotMachineContest
{
    public int[][] solve(int n)
    {
        Scanner in = new Scanner(System.in);
        List<int[]> actions = new ArrayList<>();

        int k = in.nextInt();
        if(k == 1) {
            return actions.toArray(new int[0][]);
        }

        // ---- Fase 1: separacion (todas las ruedas con simbolos distintos) ----
        for(int wheel = 2; wheel <= n && k > 1; wheel++) {
            int bestStep = 0;
            int bestK = k;

            for(int step = 1; step < n && k > 1; step++) {
                System.out.println(wheel + " " + 1);
                System.out.flush();
                actions.add(new int[]{wheel, 1});
                k = in.nextInt();

                if(k > bestK) {
                    bestK = k;
                    bestStep = step;
                }
            }

            if(k > 1) {
                int back = bestStep - (n - 1);
                if(back != 0) {
                    System.out.println(wheel + " " + back);
                    System.out.flush();
                    actions.add(new int[]{wheel, back});
                    k = in.nextInt();
                }
            }
        }

        // ---- Fase 2: descubrir el orden relativo de las ruedas ----
        int[] delta = new int[n + 1];
        boolean[] resolved = new boolean[n + 1];
        resolved[1] = true;

        for(int r = 1; r < n && k > 1; r++) {
            System.out.println("1 1");
            System.out.flush();
            actions.add(new int[]{1, 1});
            k = in.nextInt();

            boolean found = false;
            for(int w = 2; w <= n && !found && k > 1; w++) {
                if(resolved[w]) {
                    continue;
                }

                System.out.println(w + " -1");
                System.out.flush();
                actions.add(new int[]{w, -1});
                k = in.nextInt();

                if(k == n) {
                    delta[w] = r;
                    resolved[w] = true;
                    found = true;
                }
                else {
                    System.out.println(w + " 1");
                    System.out.flush();
                    actions.add(new int[]{w, 1});
                    k = in.nextInt();
                }
            }
        }

        // ---- Fase 3: alinear todas al mismo simbolo ----
        if(k > 1) {
            for(int w = 2; w <= n && k > 1; w++) {
                int steps = n - delta[w];
                if(steps != 0) {
                    System.out.println(w + " " + steps);
                    System.out.flush();
                    actions.add(new int[]{w, steps});
                    k = in.nextInt();
                }
            }
        }

        return actions.toArray(new int[0][]);
    }

    public void simulate(int n)
    {
        SlotMachine machine = new SlotMachine(n);
        machine.makeVisible();
<<<<<<< HEAD
 
        int k = machine.distinctSymbols();
        boolean jackpotReached = (k == 1);
 
        for(int pass = 0; pass < max_passes && k > 1 && !jackpotReached; pass++) {
            for(int wheel = 2; wheel <= n && k > 1 && !jackpotReached; wheel++) {
 
                int bestStep = 0;
                int bestK = k;
 
                for(int step = 1; step < n && k > 1 && !jackpotReached; step++) {
                    machine.spin(wheel, 1);
                    System.out.println(wheel + " " + 1);
 
=======

        int k = machine.distinctSymbols();
        if(k == 1) {
            System.out.println(k);
            return;
        }

        // ---- Fase 1: separacion ----
        for(int wheel = 2; wheel <= n && k > 1; wheel++) {
            int bestStep = 0;
            int bestK = k;

            for(int step = 1; step < n && k > 1; step++) {
                machine.rotate(wheel, 1);
                System.out.println(wheel + " " + 1);
                k = machine.distinctSymbols();

                if(k > bestK) {
                    bestK = k;
                    bestStep = step;
                }
            }

            if(k > 1) {
                int back = bestStep - (n - 1);
                if(back != 0) {
                    machine.rotate(wheel, back);
                    System.out.println(wheel + " " + back);
>>>>>>> 44e0ce9 (Simulate y solve de slotMachineContest ya funcionan)
                    k = machine.distinctSymbols();
                    if(k == 1) {
                        jackpotReached = true;
                    }
                    else if(k < bestK) {
                        bestK = k;
                        bestStep = step;
                    }
                }
<<<<<<< HEAD
 
                if(!jackpotReached) {
                    int back = -((n - 1) - bestStep);
                    if(back != 0) {
                        machine.spin(wheel, back + n);
                        System.out.println(wheel + " " + back);
 
                        k = machine.distinctSymbols();
                        if(k == 1) {
                            jackpotReached = true;
                        }
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
=======
            }
        }

        // ---- Fase 2: descubrir el orden relativo ----
        int[] delta = new int[n + 1];
        boolean[] resolved = new boolean[n + 1];
        resolved[1] = true;

        for(int r = 1; r < n && k > 1; r++) {
            machine.rotate(1, 1);
            System.out.println("1 1");
            k = machine.distinctSymbols();

            boolean found = false;
            for(int w = 2; w <= n && !found && k > 1; w++) {
                if(resolved[w]) {
                    continue;
                }

                machine.rotate(w, -1);
                System.out.println(w + " -1");
                k = machine.distinctSymbols();

                if(k == n) {
                    delta[w] = r;
                    resolved[w] = true;
                    found = true;
                }
                else {
                    machine.rotate(w, 1);
                    System.out.println(w + " 1");
                    k = machine.distinctSymbols();
                }
            }
        }

        // ---- Fase 3: alinear ----
        if(k > 1) {
            for(int w = 2; w <= n && k > 1; w++) {
                int steps = n - delta[w];
                if(steps != 0) {
                    machine.rotate(w, steps);
                    System.out.println(w + " " + steps);
                    k = machine.distinctSymbols();
                }
            }
        }

        System.out.println(k);
    }
>>>>>>> 44e0ce9 (Simulate y solve de slotMachineContest ya funcionan)
}