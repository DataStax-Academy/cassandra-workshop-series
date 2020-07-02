import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import Typography from '@material-ui/core/Typography';
import TransferWithinAStationIcon from '@material-ui/icons/TransferWithinAStation';
import CheckBoxOutlineBlankIcon from '@material-ui/icons/CheckBoxOutlineBlank';
import CheckBoxIcon from '@material-ui/icons/CheckBox';

const useStyles = makeStyles(theme => ({
    tableContainer: {
        minHeight: '100vh'
    },
    table: {
        zIndex: 100,
        backgroundColor: "black",
        border: "solid 1px #585858",
        width: 650,
        '& th': {
            color: 'limegreen',
            backgroundColor: "black",
            fontWeight: "bold",
            borderBottomColor: 'limegreen',
            paddingTop: 0,
            paddingBottom: 0
        },
        '& td': {
            color: 'limegreen',
            backgroundColor: "black",
            borderBottomColor: 'limegreen',
            paddingTop: 0,
            paddingBottom: 0
        }
    },
    list: {
        listStyle: "none",
        backgroundColor: "black",
        color: "limegreen"
    }
}));


export default function AddJourneyDialog(props) {
    const classes = useStyles();
    return (
        <Grid
            container
            spacing={0}
            direction="column"
            alignItems="center"
            justify="center"
            className={classes.tableContainer}      >

            <Grid item xs={2}>
                <Typography variant="h6" className={classes.list}>
                    Journey Checklist
          </Typography>
                <div className={classes.list}>
                    <List dense>
                        <ListItem>
                            <ListItemIcon>
                                {props.currentWriteCount === 1000 &&
                                    <CheckBoxIcon style={{ fill: "limegreen" }} />}
                                {props.currentWriteCount > 0 && props.currentWriteCount < 1000 &&
                                    <TransferWithinAStationIcon style={{ fill: "limegreen" }} />}
                                {props.currentWriteCount === 0 &&
                                    <CheckBoxOutlineBlankIcon style={{ fill: "limegreen" }} />}
                            </ListItemIcon>
                            <ListItemText
                                primary="Writing 1000 Rows to Astra" />
                        </ListItem>
                        <ListItem>
                            <ListItemIcon>
                                {props.currentReadCount === 1000 &&
                                    <CheckBoxIcon style={{ fill: "limegreen" }} />}
                                {props.currentReadCount > 0 && props.currentReadCount < 1000 &&
                                    <TransferWithinAStationIcon style={{ fill: "limegreen" }} />}
                                {props.currentReadCount === 0 &&
                                    <CheckBoxOutlineBlankIcon style={{ fill: "limegreen" }} />}
                            </ListItemIcon>
                            <ListItemText
                                primary="Reading 1000 Rows from Astra" />
                        </ListItem>
                        <ListItem>
                            <ListItemIcon>
                                {props.index === 1000 &&
                                    <CheckBoxIcon style={{ fill: "limegreen" }} />}
                                {props.index > 0 && props.index < 1000 &&
                                    <TransferWithinAStationIcon style={{ fill: "limegreen" }} />}
                                {props.index === 0 &&
                                    <CheckBoxOutlineBlankIcon style={{ fill: "limegreen" }} />}
                            </ListItemIcon>
                            <ListItemText
                                primary="Replaying Rows" />
                        </ListItem>
                    </List>
                </div>
            </Grid>
            <Grid item xs={4}>
                <Table className={classes.table} aria-label="simple table"
                    dense
                >
                    <TableHead>
                        <TableRow>
                            <TableCell>Astra Table</TableCell>
                            <TableCell align="right">Rows Written</TableCell>
                            <TableCell align="right">Rows Read</TableCell>
                            <TableCell align="right">Current Row Displayed</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        <TableRow key="spacecraft_temperature_over_time">
                            <TableCell component="th" scope="row">
                                spacecraft_temperature_over_time
                                    </TableCell>
                            <TableCell align="right">{props.currentWriteCount}</TableCell>
                            <TableCell align="right">{props.currentReadCount}</TableCell>
                            <TableCell align="right">{props.index}</TableCell>
                        </TableRow>
                        <TableRow key="spacecraft_speed_over_time">
                            <TableCell component="th" scope="row">
                                spacecraft_speed_over_time
                                    </TableCell>
                            <TableCell align="right">{props.currentWriteCount}</TableCell>
                            <TableCell align="right">{props.currentReadCount}</TableCell>
                            <TableCell align="right">{props.index}</TableCell>
                        </TableRow>
                        <TableRow key="spacecraft_pressure_over_time">
                            <TableCell component="th" scope="row">
                                spacecraft_pressure_over_time
                                    </TableCell>
                            <TableCell align="right">{props.currentWriteCount}</TableCell>
                            <TableCell align="right">{props.currentReadCount}</TableCell>
                            <TableCell align="right">{props.index}</TableCell>
                        </TableRow>
                        <TableRow key="spacecraft_location_over_time">
                            <TableCell component="th" scope="row">
                                spacecraft_location_over_time
                                    </TableCell>
                            <TableCell align="right">{props.currentWriteCount}</TableCell>
                            <TableCell align="right">{props.currentReadCount}</TableCell>
                            <TableCell align="right">{props.index}</TableCell>
                        </TableRow>
                    </TableBody>
                </Table>
            </Grid>
        </Grid>
    )
};