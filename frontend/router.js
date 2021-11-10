const express = require('express');
const router = express.Router();

router.get('/', function (req, res, next) {
	res.render('teaser', { title: "Manag.ly - Coming soon." });
});
/*
router.get('/', function (req, res, next) {
	res.render('landing', { title: "Manag.ly - Find your Project" });
});

router.get('/:proj', function (req, res, next) {
	res.render('project', { title: "Manag.ly - Project View" });
});

router.get('/:proj/team', function (req, res, next) {
	res.render('team', { title: "Manag.ly - Team View" });
});

router.get('/admin', function (req, res, next) {
	res.render('project', { title: "Manag.ly - Project View" });
});
*/

module.exports = router;
