const path = require('path')

module.exports = {

  entry: {
    './src/main.ts'
  },
  
  output: {
    path: path.resolve('dist'),
    filename: 'bundle.js',
    publicPath: '/assets/'
  },

  resolve {
    extensions: 
  },

  module: {
    rules: [
      {
        test: /\.js$/,
        loader: 'babel-loader'
      }
    ]
  }
}