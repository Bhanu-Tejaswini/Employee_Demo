package com.arraigntech.utility;

import com.arraigntech.Exception.AppException;

public interface Action<I, O> {

    O execute(I input) throws AppException;

    default <OO> Action<I, OO> then(Action<O, OO> afterAction) {
        return (i) -> afterAction.execute(this.execute(i));
    }



}
