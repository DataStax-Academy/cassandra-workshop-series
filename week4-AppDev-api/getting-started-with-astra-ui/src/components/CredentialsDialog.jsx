import React, { useState, useEffect } from 'react';
import Button from '@material-ui/core/Button';
import DialogTitle from '@material-ui/core/DialogTitle';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import TextField from '@material-ui/core/TextField';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemSecondaryAction from '@material-ui/core/ListItemSecondaryAction';
import ListItemText from '@material-ui/core/ListItemText';
import IconButton from '@material-ui/core/IconButton';
import { DropzoneArea } from 'material-ui-dropzone'
import DeleteIcon from '@material-ui/icons/Delete';

export default function CredentialsDialog(props) {
    const [userName, setUserName] = useState(null);
    const [password, setPassword] = useState(null);
    const [keyspace, setKeyspace] = useState(null);
    const [files, setFiles] = useState(null);


    useEffect(() => {
        setFiles(null) //Remove any files on open or close
    }, [props.open])

    function removeFile(files) {
        setFiles(null)
    }

    function updateFiles(files) {
        setFiles(files)
    }

    function updateUserName(target) {
        setUserName(target.currentTarget.value);
    }

    function updatePassword(target) {
        setPassword(target.currentTarget.value);
    }

    function updateKeyspace(target) {
        setKeyspace(target.currentTarget.value);
    }

    function testConnection() {
        props.handleTest({ username: userName, password: password, keyspace: keyspace, secureConnectBundle: files[0] });
    }

    function saveConnection() {
        props.handleSave({ username: userName, password: password, keyspace: keyspace, secureConnectBundle: files[0] });
    }

    const saveEnabled = password && userName && keyspace && files && files.length;

    return (
        <div>
            <Dialog open={props.open} aria-labelledby="form-dialog-title">
                <DialogTitle id="form-dialog-title">Connect to your Astra Database</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Please enter the following information to connect to your Astra instance
                    </DialogContentText>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="userName"
                        label="Database User Name"
                        type="text"
                        fullWidth
                        required
                        onChange={updateUserName}
                    />
                    <TextField
                        margin="dense"
                        id="password"
                        label="Database Password"
                        type="password"
                        fullWidth
                        required
                        onChange={updatePassword}
                    />
                    <TextField
                        margin="dense"
                        id="keyspace"
                        label="Keyspace"
                        type="keyspace"
                        fullWidth
                        required
                        onChange={updateKeyspace}
                    />
                    {(!files || !files.length) &&
                        <DialogContentText>
                            Choose your Secure Connect Bundle:*
                            <DropzoneArea
                                acceptedFiles={["application/zip"]}
                                filesLimit={1}
                                dropzoneText="Drag and Drop your Secure Connect Bundle here or click to choose"
                                onChange={updateFiles}
                                showFileNames={true}
                                showPreviews={true}
                                showPreviewsInDropzone={false}
                                showAlerts={false}
                            />
                        </DialogContentText>
                    }
                    {files && files.length &&
                        <List dense>
                            <ListItem>
                                <ListItemText
                                    primary={files[0].name}
                                />
                                <ListItemSecondaryAction>
                                    <IconButton edge="end" aria-label="delete" onClick={removeFile}>
                                        <DeleteIcon />
                                    </IconButton>
                                </ListItemSecondaryAction>
                            </ListItem>
                        </List>
                    }
                </DialogContent>
                <DialogActions>
                    <Button disabled={!saveEnabled} onClick={testConnection} color="primary">
                        Test Connection
                    </Button>
                    <Button disabled={!saveEnabled} variant="contained" onClick={saveConnection} color="primary">
                        Save
                    </Button>
                </DialogActions>
            </Dialog>
        </div>
    )
};