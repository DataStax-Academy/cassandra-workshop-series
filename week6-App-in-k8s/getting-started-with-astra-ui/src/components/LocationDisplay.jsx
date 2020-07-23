import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import Typography from '@material-ui/core/Typography';
import { Scatter } from 'react-chartjs-2';

const useStyles = makeStyles(theme => ({
    scatterContainer: {
        float: "left",
        flex: 1,
        '& canvas': {
            height: 100,
            width: "100%"
        }
    },
    grey: {
        borderColor: "#585858"
    }
}));


export default function LocationDisplay(props) {
    const classes = useStyles();
    //Data setup for the Scatter plot
    const locationData = {
        labels: ['Scatter'],
        datasets: [
            {
                fill: false,
                backgroundColor: 'green',
                color: 'green',
                pointBorderColor: 'green',
                pointBackgroundColor: 'green',
                pointBorderWidth: 1,
                pointHoverRadius: 5,
                pointHoverBackgroundColor: 'rgba(75,192,192,1)',
                pointHoverBorderColor: 'rgba(220,220,220,1)',
                pointHoverBorderWidth: 2,
                pointRadius: 7,
                pointHitRadius: 10,
                data: [
                    {
                        x: props.location ? props.location.x_coordinate : 0,
                        y: props.location ? props.location.y_coordinate : 0,
                    }
                ]
            }
        ]
    };

    //Options setup for the scatter plot
    const optionsCustom = {
        responsive: false,
        maintainAspectRation: true,
        tooltips: {
            mode: 'label'
        },
        elements: {
            line: {
                fill: false
            }
        },
        scales: {
            xAxes: [{
                type: 'linear',
                ticks: {
                    min: 10000,
                    max: 15000,
                    display: false
                },
                display: true,
                gridLines: {
                    color: "green",
                }
            }],
            yAxes: [{
                type: 'linear',
                ticks: {
                    min: 10000,
                    max: 15000,
                    display: false
                },
                display: true,
                gridLines: {
                    color: "green",
                }
            }],
        }
    };

    return (
        <div>
            <Typography variant="h8" className={classes.list}>
                Current Spacecraft Location
          </Typography>
            <hr className={classes.grey} />
            <div style={{
                display: "flex",
            }}>
                <div style={{ float: "left" }}>
                    <List dense>
                        <ListItem>
                            <ListItemText
                                primary={"X: " + props.location.x_coordinate + " km"} />
                        </ListItem>
                        <ListItem>
                            <ListItemText
                                primary={"Y: " + props.location.y_coordinate + " km"} />
                        </ListItem>
                        <ListItem>
                            <ListItemText
                                primary={"Z: " + props.location.z_coordinate + " km"} />
                        </ListItem>
                    </List>
                </div>
                <div className={classes.scatterContainer}>
                    <Scatter style={{ display: "block" }} data={locationData} options={optionsCustom} legend={{ display: false }} height={200} />

                </div>
            </div>
        </div>
    )
};