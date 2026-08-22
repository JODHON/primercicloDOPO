import java.util.ArrayList;
import java.util.Random;

/**
 * Representa una rueda de la maquina tragamonedas.
 *
 * @author Jose
 */
public class Wheel
{
    private ArrayList<String> symbols;
    private int visibleIndex;
    private boolean visible;
    private Circle symbol;
    private int xPos;

    private static final int SPACING = 60;
    private static final int MARGIN_X = 50;

    private static Random random = new Random();

    /**
     * Crea una rueda vacia.
     */
    public Wheel()
    {
        symbols = new ArrayList<String>();
        visibleIndex = 0;
        visible = false;
        symbol = new Circle();
        xPos = 0;
    }

    /**
     * Agrega un simbolo en una posicion.
     *
     * @param pos posicion del simbolo
     * @param color color del simbolo
     */
    public void addSymbol(int pos, String color)
    {
        int index = pos - 1;

        if(index < 0) {
            index = 0;
        }

        if(index > symbols.size()) {
            index = symbols.size();
        }

        symbols.add(index, color);
    }

    /**
     * Elimina un simbolo.
     *
     * @param color color del simbolo
     */
    public void delSymbol(String color)
    {
        int idx = symbols.indexOf(color);

        if(idx != -1) {
            symbols.remove(idx);

            if(symbols.isEmpty()) {
                visibleIndex = 0;
            }
            else if(visibleIndex >= symbols.size()) {
                visibleIndex = symbols.size() - 1;
            }
        }
    }

    /**
     * Gira la rueda y selecciona un simbolo aleatorio.
     */
    public void spin()
    {
        if(!symbols.isEmpty()) {
            visibleIndex = random.nextInt(symbols.size());
        }
    }

    /**
     * Coloca un simbolo como visible.
     *
     * @param color color del simbolo
     * @return true si el simbolo existe
     */
    public boolean placeSymbol(String color)
    {
        int idx = symbols.indexOf(color);

        if(idx != -1) {
            visibleIndex = idx;
            return true;
        }

        return false;
    }

    /**
     * Retorna el simbolo actualmente visible.
     *
     * @return simbolo visible
     */
    public String visibleSymbol()
    {
        if(symbols.isEmpty()) {
            return null;
        }

        return symbols.get(visibleIndex);
    }

    /**
     * Retorna todos los simbolos.
     *
     * @return simbolos
     */
    public String[] symbols()
    {
        return symbols.toArray(new String[0]);
    }

    /**
     * Comprueba si existe un color.
     *
     * @param color color a buscar
     * @return true si existe
     */
    public boolean hasColor(String color)
    {
        return symbols.contains(color);
    }

    /**
     * Actualiza la posicion y color del simbolo.
     *
     * @param wheelPos posicion de la rueda
     */
    public void updateAppearance(int wheelPos)
    {
        if(!symbols.isEmpty()) {
            int targetX = MARGIN_X + (wheelPos - 1) * SPACING;

            symbol.changeColor(symbols.get(visibleIndex));
            symbol.moveHorizontal(targetX - xPos);

            xPos = targetX;
        }
    }

    /**
     * Hace visible la rueda.
     *
     * @param wheelPos posicion de la rueda
     */
    public void makeVisible(int wheelPos)
    {
        visible = true;

        updateAppearance(wheelPos);
        symbol.makeVisible();
    }

    /**
     * Hace invisible la rueda.
     */
    public void makeInvisible()
    {
        visible = false;
        symbol.makeInvisible();
    }
}