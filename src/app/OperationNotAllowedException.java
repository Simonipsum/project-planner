/**
 * Main responsible: Emil Skov Andersen
 */

package app;
// Courtesy of Huba
public class OperationNotAllowedException extends Exception {
    public OperationNotAllowedException(String errorMessage) {
        super(errorMessage);
    }
}
