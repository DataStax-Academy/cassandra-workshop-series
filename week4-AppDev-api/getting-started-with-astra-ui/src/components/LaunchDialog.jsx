import React, { useState, useEffect, useRef } from 'react';
import DialogTitle from '@material-ui/core/DialogTitle';
import Dialog from '@material-ui/core/Dialog';
import DialogContent from '@material-ui/core/DialogContent';
import { DialogContentText } from '@material-ui/core';

export default function LaunchDialog(props) {
    const [index, setIndex] = useState(-1);
    const messages = [
        "T-5 - Com check Houston",
        "T-4 - We read you 5 by 5",
        "T-3 - Main Engine Start",
        "T-2",
        "T-1 - Liftoff"
    ]

    useInterval(() => {     //Cycles through the messages
        if (props.open) {
            setIndex(index + 1)
            if (index + 1 === messages.length) {
                setIndex(0);
                props.handleClose()
            }
        }
    }, 1000);

    function useInterval(callback, delay) {
        const savedCallback = useRef();

        // Remember the latest function.
        useEffect(() => {
            savedCallback.current = callback;
        }, [callback]);

        // Set up the interval.
        useEffect(() => {
            function tick() {
                savedCallback.current();
            }
            if (delay !== null) {
                let id = setInterval(tick, delay);
                return () => clearInterval(id);
            }
        }, [delay]);
    }

    return (
        <div>
            <Dialog open={props.open} aria-labelledby="form-dialog-title">
                <DialogTitle id="form-dialog-title">Preparing for your Launch</DialogTitle>
                <DialogContent>
                    <DialogContentText align={"center"} >
                        {messages[index]}
                    </DialogContentText>
                </DialogContent>
            </Dialog>
        </div>
    )
};