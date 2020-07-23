import React from 'react';
import { BrowserRouter } from 'react-router-dom';
import Routes from './Routes';
import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';
import createTypography from '@material-ui/core/styles/createTypography';
import createPalette from '@material-ui/core/styles/createPalette';

const palette = createPalette({
  type: 'dark',
});
const theme = createMuiTheme({
  typography: createTypography(palette, {
    fontFamily: "ProximaNova"
  })
});

const App = () => (
  <MuiThemeProvider theme={theme}>
    <BrowserRouter>
      <main className="container">
        <Routes />
      </main>
    </BrowserRouter>
  </MuiThemeProvider>
);

export default App;
