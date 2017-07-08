'use strict';

let path = require('path');
let ExtractTextPlugin = require('extract-text-webpack-plugin');

module.exports = [
    {
        test: /\.pug$/,
        loaders: ['apply-loader', 'pug-loader']
    },
    {
        test: /\.html$/,
        use: 'raw-loader'
    },
    {
        test: /\.scss$/,
        include: path.resolve(process.cwd(), 'src', 'app'),
        loaders: ['to-string-loader', 'css-loader', 'sass-loader']
    },
    {
        test: /\.sass$/,
        include: path.resolve(process.cwd(), 'src', 'app'),
        loaders: ['to-string-loader', 'css-loader', 'sass-loader']
    },
    {
        test: /\.css$/,
        exclude: path.resolve(process.cwd(), 'src', 'app'),
        loader: ExtractTextPlugin.extract({
            fallback: 'style-loader',
            use: 'css-loader'
        })
    }
];
