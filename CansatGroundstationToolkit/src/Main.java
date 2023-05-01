import com.mjhutchinson.cansatgroundstationtoolkit.homeWindow.HomeWindow;

import java.awt.*;

/**
 * Created by Michael Hutchinson on 28/10/2014 at 08:54.
 *
 * Creates a new instance of the program
 */
public class Main {

    public static void main(String[] args){

        EventQueue.invokeLater(HomeWindow::new);

    }

}
