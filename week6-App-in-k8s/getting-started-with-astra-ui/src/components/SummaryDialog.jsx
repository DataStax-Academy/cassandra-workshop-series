import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import DialogTitle from '@material-ui/core/DialogTitle';
import Dialog from '@material-ui/core/Dialog';
import DialogContent from '@material-ui/core/DialogContent';
import { DialogContentText } from '@material-ui/core';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import IconButton from '@material-ui/core/IconButton';
import CloseIcon from '@material-ui/icons/Close';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';

const useStyles = makeStyles(theme => ({
    root: {
        width: '100%',
        maxWidth: 600,
        backgroundColor: theme.palette.background.paper,
    },
    nested: {
        paddingLeft: theme.spacing(2),
        width: '100%',
        marginTop: 2,
        marginBottom: 2,
        '& li': {
            marginTop: 2,
            marginBottom: 2,
            listStyle: "none"
        }
    },
    nopadding: {
        paddingTop: 0,
        paddingBottom: 0
    },
    closeButton: {
        position: 'absolute',
        right: theme.spacing(1),
        top: theme.spacing(1),
        color: theme.palette.grey[500],
    },
}));

export default function SummaryDialog(props) {
    const classes = useStyles();

    return (
        <div>
            <Dialog open={props.open} onClose={props.handleClose} aria-labelledby="form-dialog-title">
                <DialogTitle id="form-dialog-title">
                    <Typography variant="h6">Journey Summary</Typography>
                    {props.handleClose ? (
                        <IconButton aria-label="close" className={classes.closeButton} onClick={props.handleClose}>
                            <CloseIcon />
                        </IconButton>
                    ) : null}</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        <List dense
                            disablePadding
                            component="nav"
                            className={classes.root}
                        >
                            <ListItem className={classes.nopadding}>
                                <ListItemText primary={<span>Spacecraft Name: <strong>{props.journeyInformation.spacecraft_name}</strong></span>} />
                            </ListItem>
                            <ListItem className={classes.nopadding}>
                                <ListItemText primary={<span>Journey ID: <strong>{props.journeyInformation.journey_id}</strong></span>} />
                            </ListItem>
                            <ListItem className={classes.nopadding}>
                                <ListItemText primary={<span>Read Duration: <strong>{(props.journeyInformation.read_time / 1000).toFixed(2)}</strong> seconds</span>} />
                            </ListItem>
                            <ListItem className={classes.nopadding}>
                                <ListItemText primary={<span>Write Duration: <strong>{(props.journeyInformation.write_time / 1000).toFixed(2)}</strong> seconds</span>} />
                            </ListItem>
                            <ListItem className={classes.nopadding}>
                                <ListItemText primary="Database Tables Accessed"

                                    secondary={
                                        <ul className={classes.nested}>
                                            <li>spacecraft_journey_catalog: <strong>1</strong> row written &amp; read</li>
                                            <li>spacecraft_speed_over_time: <strong>1000</strong> rows written &amp; read</li>
                                            <li>spacecraft_location_over_time: <strong>1000</strong> rows written &amp; read</li>
                                            <li>spacecraft_temperature_over_time: <strong>1000</strong> rows written &amp; read</li>
                                            <li>spacecraft_pressure_over_time: <strong>1000</strong> rows written &amp; read</li>
                                        </ul>
                                    } />
                            </ListItem>
                            <ListItem className={classes.nopadding}>
                                <ListItemText primary="To Learn More"
                                    secondary={
                                        <ul className={classes.nested}>
                                            <li>To <strong>learn</strong> from the experts, start your <a href="www.datastax.com">DataStax Academy learning path</a>.</li>
                                            <li>To <strong>see</strong> the code for this example, go to the <a href="www.datastax.com">source code</a>.</li>
                                            <li>To <strong>read</strong> about Astra details and tooling, go to the <a href="www.datastax.com">documentation</a>.</li>
                                        </ul>
                                    } />
                            </ListItem>
                        </List>
                        <div style={{ display: "flex" }}>
                            <Button variant="contained" onClick={props.handleClose} color="primary" style={{ marginLeft: "auto", marginRight: "auto" }}>
                                Close
                            </Button>
                        </div>
                    </DialogContentText>
                </DialogContent>
            </Dialog>
        </div >
    )
};