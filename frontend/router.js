const express = require('express');
const router = express.Router();


router.get('/', function (req, res, next) {
	res.render('landing', { title: "Manag.ly - Find your Project" });
});

router.get('/:proj', function (req, res, next) {
	console.log(req);
	res.render('project', { title: "Manag.ly - Project View", active: "project", slug: `/${req.params.proj}` });
});

router.get('/:proj/team', function (req, res, next) {
	res.render('team', { title: "Manag.ly - Team View", active: "team", slug: `/${req.params.proj}` });
});

router.get('/admin', function (req, res, next) {
	res.render('admin', { title: "Manag.ly - Admin View" });
});

module.exports = router;
