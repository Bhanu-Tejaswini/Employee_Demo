package com.employee.utility;

import com.employee.exceptions.AppException;

public interface Action<I, O> {

    O execute(I input) throws AppException;

    default <OO> Action<I, OO> then(Action<O, OO> afterAction) {
        return (i) -> afterAction.execute(this.execute(i));
    }



}
