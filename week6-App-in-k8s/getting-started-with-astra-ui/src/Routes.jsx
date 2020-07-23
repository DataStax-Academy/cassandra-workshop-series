import React from 'react';
import { Route, Switch } from 'react-router-dom';
import { HomeContainer } from './containers';

const Routes = () => (
  <Switch>
    <Route exact path="/" component={HomeContainer} />
  </Switch>
);

export default Routes;
