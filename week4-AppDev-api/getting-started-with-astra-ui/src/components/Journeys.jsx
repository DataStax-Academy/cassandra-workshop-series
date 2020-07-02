import React from 'react';
import MuiTreeView from 'material-ui-treeview';
import Typography from '@material-ui/core/Typography';

export default function Journeys(props) {

    const [open, setOpen] = React.useState(false);

    const handleClick = () => {
        setOpen(!open);
    };

    function getJourneysForSpacecraft(spacecraft_name) {
        var items = [];
        var journeys = props.spacecraft.filter(s => s.spacecraft_name === spacecraft_name);
        journeys.map((value, index) => {
            var date = new Date(value.start);
            items.push({ id: spacecraft_name + "|" + date.getTime(), value: date.toLocaleDateString(), data: value });
        })
        return items;
    }

    function getJourneyReadings(node) {
        props.fetchJourney(node.data.spacecraft_name, node.data.journey_id)
        props.onClose();
    }

    var names = [...new Set(props.spacecraft.map(item => item.spacecraft_name))];
    var tree = [];
    names.forEach(n => {
        var nodes = getJourneysForSpacecraft(n);
        tree.push({ value: n, nodes: nodes })
    })
    return (
        <div>
            <Typography variant="h6" >Replay Journey</Typography>
            <MuiTreeView tree={tree}
                onLeafClick={getJourneyReadings}
                expansionPanelSummaryProps={{ style: { minHeight: 20, height: 25 } }}
                expansionPanelDetailsProps={{ style: { marginLeft: 15 } }}
            />
        </div>

    );
}