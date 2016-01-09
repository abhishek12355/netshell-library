package com.netshell.libraries.utilities.db.enums;

/**
 * @author Abhishek
 *         Created on 12/13/2015.
 */
public interface Initializable<I, O> {
    void initialize(int rowIndex, I row);

    O getId();
}
