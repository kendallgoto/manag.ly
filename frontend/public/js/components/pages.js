import { Home } from './home.js';
import { Admin } from './admin.js';
import { ProjectView } from './projectView.js';
import { TeamView } from './teamView.js';

const loadedPages = {
	"#landing": Home,
	"#admin": Admin,
	"#projectView": ProjectView,
	"#teamView": TeamView,
};
function init() {
	for (const [key, Clazz] of Object.entries(loadedPages)) {
		if ($(key).length) {
			new Clazz();
		}
	}
}

export {
	init
};
