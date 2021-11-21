/**
 * Containing all the possible renderers in the game.
 */
public class RendererFactory {
    /**
     * Translate user input for which renderer (through main args) to the actual renderer.
     *
     * @param input
     * @return Renderer class.
     */
    Renderer buildRenderer(String input) {

        if (input.equals("console")) {
            return new ConsoleRenderer();
        }

        if (input.equals("none")) {
            return new VoidRenderer();
        }
        return null;
    }
}
