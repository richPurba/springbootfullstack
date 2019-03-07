var path = require('path');

module.exports = {
	entry: './src/main/js/app.js', // an entry point of to begin building its internal dependency graph
	devtool: 'sourcemaps',
	cache: true,
	mode: 'development',
	output: {
		path: __dirname,
		filename: './src/main/resources/static/built/bundle.js' //where to emit the bundles it creates and how to name them
	},
	module: {
		rules: [
			{
				test: path.join(__dirname, '.'),//which file should be transformed in the require()/import statement! Transform to what? WHy transform?
				exclude: /(node_modules)/,//excluding node_modules
				use: [{// which loader should be used to do the transforming
					loader: 'babel-loader',
					options: {
						presets: ["@babel/preset-env", "@babel/preset-react"]
					}
				}]
			}
		]
	}
};