import React, { useState, useEffect, useRef } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';
import ReactSpeedometer from "react-d3-speedometer";
import SummaryDialog from '../components/SummaryDialog';
import HUD from '../components/HUD';
import LocationDisplay from '../components/LocationDisplay';

const useStyles = makeStyles(theme => ({
    root: {
        position: 'absolute',
        top: 64,
        bottom: 0,
        left: 0,
        right: 0,
        backgroundColor: 'black',
        color: 'white'
    },
    grid: {
        position: 'absolute',
        zIndex: 100,
        bottom: 0
    },
    gaugeContainer: {
        border: "solid 1px #585858",
        backgroundColor: "black",
        color: 'limegreen',
    },
    gauge: {
        padding: theme.spacing(2),
        height: 200,
        backgroundColor: "black",
        '& canvas': {
            height: 200,
            width: "100%"
        }
    }
}));

export default function TripContainer(props) {
    const classes = useStyles();
    const [temperatureGauge, setTemperatureGauge] = useState({ min: 0, max: 100, units: "Fahrenheit" });
    const [pressureGauge, setPressureGauge] = useState({ min: 90, max: 105, units: "kPa" });
    const [speedGauge, setSpeedGauge] = useState({ min: 20000, max: 40000, units: "km/h" });
    const [currentValues, setCurrentValues] = useState({
        temperature: null,
        pressure: null,
        speed: null,
        location: { x_coordinate: 0, y_coordinate: 0, z_coordinate: 0 },
        index: 0
    });
    const [currentInterval, setCurrentInterval] = useState(30);
    const [data, setData] = useState(props.data);
    const [openSummaryDialog, setOpenSummaryDialog] = useState(false);

    ///Reset the current playing state when it is finished
    useEffect(() => {
        setCurrentValues({
            temperature: null,
            pressure: null,
            speed: null,
            location: { x_coordinate: 0, y_coordinate: 0, z_coordinate: 0 },
            index: 0
        })
    }, [props.playing])

    function closeSummaryDialog() {
        setOpenSummaryDialog(false);
    }

    useInterval(() => {
        if (props.playing && data && data.temperature) {
            if (data.temperature && currentValues.index < 999) {    //We are ready so ley's play the replay
                var newIndex = currentValues.index + 1;
                setCurrentValues({
                    temperature: data.temperature[currentValues.index].temperature.toPrecision(4),
                    pressure: data.pressure[currentValues.index].pressure.toPrecision(4),
                    speed: data.speed[currentValues.index].speed.toPrecision(7),
                    location: data.location[currentValues.index].location,
                    index: newIndex
                });
            } else {    //we are done running so stop playing and show the summary
                setOpenSummaryDialog(true);
                props.stopPlaying();
            }
        }
    }, currentInterval);

    //Set the gauges to a value on playback
    useEffect(() => {
        setData(props.data)
    }, [props.data])


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
        <div className={classes.root}>
            {props.playing &&
                <div className="stars">
                </div>
            }
            {props.playing &&
                <HUD currentWriteCount={props.currentWriteCount} currentReadCount={props.currentReadCount} index={currentValues.index} />
            }
            <Grid container spacing={3} className={classes.grid}>
                <Grid item xs={3}>
                    <div className={classes.gaugeContainer}>
                        <div className={classes.gauge}>
                            <ReactSpeedometer
                                minValue={temperatureGauge.min}
                                maxValue={temperatureGauge.max}
                                value={currentValues.temperature}
                                currentValueText={"Temperature: ${value} " + temperatureGauge.units}
                                segmentColors={['green', 'limegreen', 'yellow', 'orange', 'red']}
                            />

                        </div>
                        Astra Table: spacecraft_temperature_over_time
                    </div>
                </Grid>
                <Grid item xs={3}>
                    <div className={classes.gaugeContainer}>
                        <div className={classes.gauge}>
                            <ReactSpeedometer
                                minValue={speedGauge.min}
                                maxValue={speedGauge.max}
                                value={currentValues.speed}
                                currentValueText={"Speed: ${value} " + speedGauge.units}
                                segmentColors={['green', 'limegreen', 'yellow', 'orange', 'red']}
                                style={{
                                    border: "solid 1px red"
                                }}
                            />
                        </div>
                        Astra Table: spacecraft_speed_over_time
                    </div>
                </Grid>
                <Grid item xs={3}>
                    <div className={classes.gaugeContainer}>
                        <div className={classes.gauge}>
                            <ReactSpeedometer
                                minValue={pressureGauge.min}
                                maxValue={pressureGauge.max}
                                value={currentValues.pressure}
                                currentValueText={"Pressure: ${value} " + pressureGauge.units}
                                segmentColors={['green', 'limegreen', 'yellow', 'orange', 'red']}
                            />
                        </div>
                        Astra Table: spacecraft_pressure_over_time
                    </div>
                </Grid>
                <Grid item xs={3}>
                    <div className={classes.gaugeContainer}>
                        <div className={classes.gauge}>
                            <LocationDisplay location={currentValues.location} />
                        </div>
                        Astra Table: spacecraft_location_over_time
                    </div>
                </Grid>
            </Grid>
            <SummaryDialog open={openSummaryDialog} handleClose={closeSummaryDialog} journeyInformation={props.journeyInformation} />
        </div >
    );
}