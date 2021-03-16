module.exports = {
  entry: {
    're-resizable': './main.js'
  },
  output: {
    filename: '[name].inc.js'
  },
  externals: {
    'react': 'React',
    'react-dom': 'ReactDOM',
  }
};
