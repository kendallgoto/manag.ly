const createError = require('http-errors');
const express = require('express');
const path = require('path');
const cookieParser = require('cookie-parser');
const logger = require('morgan');
const chokidar = require('chokidar');
const { rollup } = require("rollup");
const { terser } = require("rollup-plugin-terser");
const debug = require('debug')('manag.ly');
const sass = require('sass');
const fs = require('fs');

require('dotenv').config();

function buildCSS() {
	debug("Rendering scss to managly.css " + sass.info);
	const coreCSS = sass.renderSync({
		includePaths: [path.join(__dirname, 'public', 'css', '_partials')],
		file: path.join(__dirname, 'public', 'css', 'managly.scss'),
		indentedSyntax: false,
		sourceMap: true,
		outFile: path.join(__dirname, 'public', 'managly.css'),
		outputStyle: 'compressed'
	});
	fs.writeFileSync(path.join(__dirname, 'public', 'managly.min.css'), coreCSS.css);
	fs.writeFileSync(path.join(__dirname, 'public', 'managly.min.css.map'), coreCSS.map);
	debug("SASS rendered in " + coreCSS.stats.duration + "ms");
}
buildCSS();

async function buildJS() {
	debug("Rendering js to managly.js");
	const startTime = new Date().getTime();
	const bundle = await rollup({
		input: "./public/js/managly.js",
		plugins: [terser({
			warnings: true
		})]
	});
	const { output } = await bundle.write({
		file: "./public/managly.min.js",
		format: "iife",
		sourcemap: "./public/managly.min.js.map"
	});
	await bundle.close();
	debug("JS uglified in " + (new Date().getTime() - startTime) + "ms");
}
buildJS();
if (process.env.NODE_ENV != 'production') {
	chokidar.watch(path.join(__dirname, 'public', 'css')).on('change', buildCSS);
	chokidar.watch(path.join(__dirname, 'public', 'js')).on('change', buildJS);
}

const app = express();
const port = process.env.PORT;

const indexRouter = require('./router');

app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());

app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use(function (req, res, next) {
	next(createError(404));
});

app.use(function (err, req, res, next) {
	// set locals, only providing error in development
	res.locals.message = err.message;
	res.locals.error = process.env.NODE_ENV === 'development' ? err : {};

	// render the error page
	res.sendStatus(err.status || 500);
	console.log(res.locals.message, res.locals.error);
});

app.listen(port, () => {
	console.log(`Manag.ly running at http://localhost:${port}`);
});
