import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 * Simula una maquina tragamonedas con ruedas que muestran simbolos
 * de colores diferentes.
 *
 * @author Jose
 */
public class SlotMachine
{
    private ArrayList<Wheel> wheels;
    private boolean visible;
    private boolean ok;
    private Rectangle jackpotBaner;
    private ArrayList<Circle> jackpotLights;
    private String[] jackpotColors;
    private Random random;


    /**
     * Crea una maquina tragamonedas sin ruedas y visible.
     */
    public SlotMachine()
    {
        wheels = new ArrayList<Wheel>();
        visible = true;
        ok = true;
        
        
        jackpotColors= new String[]{"red","yellow","green","blue"};
        random=new Random();

        jackpotBaner = new Rectangle();
        jackpotBaner.changeSize(40, 150);
        jackpotBaner.changeColor("yellow");
        jackpotBaner.moveHorizontal(-75);
        jackpotBaner.moveVertical(100);
        
        jackpotLights=new ArrayList<Circle>();
        for (int i =0;i<4;i++){
            Circle light=new Circle();
            light.changeColor("red");
            light.changeSize(20);
            light.moveHorizontal(-60+i*40);
            light.moveVertical(80);
            jackpotLights.add(light);
        }
    }

    /**
     * Agrega una rueda en la posicion indicada.
     *
     * @param pos posicion donde insertar la rueda
     */
    public void addWheel(int pos)
    {
        int index = clamp(pos - 1, wheels.size());
        wheels.add(index, new Wheel());

        reposition();
        updateJackpot();
        ok = true;
    }

    /**
     * Elimina una rueda si existe.
     *
     * @param pos posicion de la rueda
     */
    public void delWheel(int pos)
    {
        int index = pos - 1;

        if(index >= 0 && index < wheels.size()) {
            Wheel wheel = wheels.remove(index);
            wheel.makeInvisible();

            reposition();
            updateJackpot();
            ok = true;
        }
        else {
            fail("No existe una rueda en esa posicion");
        }
    }

    /**
     * Intercambia la posicion de dos ruedas.
     *
     * @param wheel1 posicion de la primera rueda
     * @param wheel2 posicion de la segunda rueda
     */
    public void swap(int wheel1, int wheel2)
    {
        int index1 = wheel1 - 1;
        int index2 = wheel2 - 1;

        if(index1 >= 0 && index1 < wheels.size()
            && index2 >= 0 && index2 < wheels.size()) {

            Collections.swap(wheels, index1, index2);

            reposition();
            updateJackpot();
            ok = true;
        }
        else {
            fail("No existen ruedas en esas posiciones");
        }
    }

    /**
     * Fija una rueda para que no gire.
     *
     * @param wheel posicion de la rueda
     */
    public void lock(int wheel)
    {
        int index = wheel - 1;

        if(index >= 0 && index < wheels.size() && !wheels.get(index).isLocked()) {
            wheels.get(index).lock();
            ok = true;
        }
        else {
            fail("No se pudo fijar la rueda " + wheel);
        }
    }

    /**
     * Suelta una rueda previamente fijada.
     *
     * @param wheel posicion de la rueda
     */
    public void unlock(int wheel)
    {
        int index = wheel - 1;

        if(index >= 0 && index < wheels.size() && wheels.get(index).isLocked()) {
            wheels.get(index).unlock();
            ok = true;
        }
        else {
            fail("No se pudo soltar la rueda " + wheel);
        }
    }

    /**
     * Agrega un simbolo a todas las ruedas.
     *
     * @param pos posicion donde insertar el simbolo
     * @param color color del simbolo
     */
    public void addSymbol(int pos, String color)
    {
        for(Wheel wheel : wheels) {
            if(wheel.hasColor(color)) {
                fail("El color " + color + " ya existe");
                return;
            }
        }

        for(Wheel wheel : wheels) {
            wheel.addSymbol(pos, color);
        }

        reposition();
        updateJackpot();
        ok = true;
    }

    /**
     * Elimina un simbolo de todas las ruedas.
     *
     * @param symbol simbolo a eliminar
     */
    public void delSymbol(String symbol)
    {
        boolean found = false;

        for(Wheel wheel : wheels) {
            if(wheel.hasColor(symbol)) {
                found = true;
            }
        }

        if(found) {
            for(Wheel wheel : wheels) {
                wheel.delSymbol(symbol);
            }

            reposition();
            updateJackpot();
            ok = true;
        }
        else {
            fail("No existe el simbolo " + symbol);
        }
    }

    /**
     * Coloca un simbolo como visible en una rueda.
     *
     * @param wheel posicion de la rueda
     * @param symbol simbolo que se desea mostrar
     */
    public void placeSymbol(int wheel, String symbol)
    {
        int index = wheel - 1;

        if(index >= 0 && index < wheels.size()
            && wheels.get(index).placeSymbol(symbol)) {

            reposition();
            updateJackpot();
            ok = true;
        }
        else {
            fail("No se pudo ubicar el simbolo " + symbol);
        }
    }

    /**
     * Gira una rueda.
     *
     * @param wheel posicion de la rueda
     */
    public void spin(int wheel)
    {
        int index = wheel - 1;

        if(index >= 0 && index < wheels.size() && !wheels.get(index).isLocked()) {
            wheels.get(index).spin();

            reposition();
            updateJackpot();
            celebrate();
            ok = true;
        }
        else {
            fail("No existe una rueda en esa posicion o esta fija");
        }
    }

    /**
     * Gira una rueda un numero determinado de pasos.
     *
     * @param wheel posicion de la rueda
     * @param steps numero de pasos a girar
     */
    public void spin(int wheel, int steps)
    {
        int index = wheel - 1;

        if(index >= 0 && index < wheels.size()
            && !wheels.get(index).isLocked() && steps >= 0) {

            wheels.get(index).spin(steps);

            reposition();
            updateJackpot();
            celebrate();
            ok = true;
        }
        else {
            fail("No se pudo girar la rueda " + wheel);
        }
    }

    /**
     * Gira todas las ruedas que no esten fijas.
     */
    public void spin()
    {
        for(Wheel wheel : wheels) {
            if(!wheel.isLocked()) {
                wheel.spin();
            }
        }

        reposition();
        updateJackpot();
        celebrate();
        ok = true;
    }

    /**
     * Deja la maquina en la configuracion dada, respetando las ruedas fijas.
     *
     * @param setSymbols simbolo deseado para cada rueda
     */
    public void spin(String[] setSymbols)
    {
        if(setSymbols.length != wheels.size()) {
            fail("La configuracion no coincide con el numero de ruedas");
            return;
        }

        for(int i = 0; i < wheels.size(); i++) {
            if(!wheels.get(i).isLocked() && !wheels.get(i).hasColor(setSymbols[i])) {
                fail("El simbolo " + setSymbols[i] + " no existe en la rueda " + (i + 1));
                return;
            }
        }

        for(int i = 0; i < wheels.size(); i++) {
            if(!wheels.get(i).isLocked()) {
                wheels.get(i).placeSymbol(setSymbols[i]);
            }
        }

        reposition();
        updateJackpot();
        celebrate();
        ok = true;
    }

    /**
     * Retorna los simbolos de la maquina.
     *
     * @return simbolos de la maquina
     */
    public String[] symbols()
    {
        if(wheels.isEmpty()) {
            return new String[0];
        }

        return wheels.get(0).symbols();
    }

    /**
     * Retorna la cantidad de simbolos diferentes visibles.
     *
     * @return cantidad de simbolos diferentes
     */
    public int distinctSymbols()
    {
        ArrayList<String> distinct = new ArrayList<String>();

        for(Wheel wheel : wheels) {
            String symbol = wheel.visibleSymbol();

            if(symbol != null && !distinct.contains(symbol)) {
                distinct.add(symbol);
            }
        }

        return distinct.size();
    }

    /**
     * Retorna la configuracion actual.
     *
     * @return configuracion de las ruedas
     */
    public String[] configuration()
    {
        String[] config = new String[wheels.size()];

        for(int i = 0; i < wheels.size(); i++) {
            config[i] = wheels.get(i).visibleSymbol();
        }

        return config;
    }

    /**
     * Determina si la configuracion es ganadora.
     *
     * @return true si todas las ruedas muestran el mismo simbolo
     */
public boolean isJackpot()
{
    if(wheels.size() < 2) {
        return false;
    }
    for(Wheel wheel : wheels) {
        if(wheel.visibleSymbol() == null) {
            return false;
        }
    }
    return distinctSymbols() == 1;
}
    /**
     * Hace visible la maquina.
     */
    public void makeVisible()
    {
        visible = true;

        reposition();
        updateJackpot();
    }

    /**
     * Hace invisible la maquina.
     */
    public void makeInvisible()
    {
        visible = false;

        for(Wheel wheel : wheels) {
            wheel.makeInvisible();
        }

        hideJackpot();
    }

    /**
     * Termina el simulador.
     */
    public void exit()
    {
        makeInvisible();
    }

    /**
     * Indica si la ultima operacion fue exitosa.
     *
     * @return true si la ultima operacion fue exitosa
     */
    public boolean ok()
    {
        return ok;
    }

    /**
     * Reposiciona las ruedas.
     */
    private void reposition()
    {
        for(int i = 0; i < wheels.size(); i++) {
            if(visible) {
                wheels.get(i).makeVisible(i + 1);
            }
            else {
                wheels.get(i).updateAppearance(i + 1);
            }
        }
    }

    /**
     * Actualiza la apariencia del indicador de jackpot.
     */
    private void updateJackpot()
    {
        if(visible && isJackpot()) {
            showJackpot();
        }
        else {
            hideJackpot();
        }
    }

    /*Muestra el jackpot */
    
    private void showJackpot(){
    
    jackpotBaner.makeVisible();
    
    for(Circle light:jackpotLights){
    
        light.makeVisible();
    }
    }

    /*Esconde el jackpot */
    
    private void hideJackpot(){
        jackpotBaner.makeInvisible();
        
        for(Circle light:jackpotLights){
            light.makeInvisible();
        }
    }

    /*Cuando se gana el jackpot */
    
    private void  celebrate(){
        if(!visible || !isJackpot()){
            return;
        }
        
        for (int i=0;i<6;i++){
            jackpotBaner.changeColor(randomColor());
        
            for(Circle light:jackpotLights){
                light.changeColor(randomColor());
                light.moveVertical(i%2==0 ? -5:5);
            }
        }
        JOptionPane.showMessageDialog(null,"Has ganado");
    }
    
    private String randomColor(){
        return jackpotColors[random.nextInt(jackpotColors.length)];
    }
    

    /**
     * Ajusta un indice a los limites validos.
     *
     * @param index indice
     * @param max limite superior
     * @return indice ajustado
     */
    private int clamp(int index, int max)
    {
        if(index < 0) {
            return 0;
        }

        if(index > max) {
            return max;
        }

        return index;
    }

    /**
     * Marca la ultima operacion como fallida.
     *
     * @param message mensaje que se mostrara al usuario
     */
    private void fail(String message)
    {
        ok = false;

        if(visible) {
            JOptionPane.showMessageDialog(null, message);
        }
    }
}