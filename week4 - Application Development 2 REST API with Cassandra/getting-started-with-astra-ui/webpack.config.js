const path = require('path');
const webpack = require('webpack');
const HTMLWebpackPlugin = require('html-webpack-plugin');
const dotenv = require('dotenv');
const CopyWebpackPlugin = require('copy-webpack-plugin');


const dev = process.env.NODE_ENV !== 'production';

const HTMLWebpackPluginConfig = new HTMLWebpackPlugin({
  template: path.join(__dirname, '/src/index.html'),
  filename: 'index.html',
  inject: 'body',
});

const DefinePluginConfig = new webpack.DefinePlugin({
  'process.env.NODE_ENV': JSON.stringify('production'),
});

const env = dotenv.config().parsed;

// reduce it to a nice object, the same as before
const envKeys = Object.keys(env).reduce((prev, next) => {
  prev[`process.env.${next}`] = JSON.stringify(env[next]);
  return prev;
}, {});

module.exports = {
  devServer: {
    host: 'localhost',
    port: '3000',
    hot: true,
    headers: {
      'Access-Control-Allow-Origin': '*',
    },
    historyApiFallback: true,
  },
  entry: ['@babel/polyfill', 'whatwg-fetch', 'react-hot-loader/patch', path.join(__dirname, '/src/index.jsx')],
  module: {
    rules: [
      {
        test: /\.jsx?$/,
        exclude: /node_modules/,
        loaders: ['babel-loader'],
      },
      {
        test: /\.scss$/,
        loader: 'style-loader!css-loader!sass-loader',
      },
      {
        test: /\.(jpe?g|png|gif|svg)$/i,
        loader: 'url-loader',
        options: {
          limit: 10000,
        },
      },
      {
        test: /\.(woff|woff2|eot|ttf|otf)$/,
        loader: "file-loader"
      }
    ],
  },
  resolve: {
    extensions: ['.js', '.jsx'],
  },
  node: {
    fs: "empty"
  },
  output: {
    filename: 'index.js',
    path: path.join(__dirname, '/build'),
  },
  mode: dev ? 'development' : 'production',
  plugins: dev
    ? [HTMLWebpackPluginConfig, new webpack.HotModuleReplacementPlugin(), new webpack.DefinePlugin(envKeys)]
    : [HTMLWebpackPluginConfig, DefinePluginConfig,
      new webpack.DefinePlugin(envKeys),
      new CopyWebpackPlugin([
        // relative path is from src
        { from: './favicon.ico' }, // <- your path to favicon
      ])
    ],
};
