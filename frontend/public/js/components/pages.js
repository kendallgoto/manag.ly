import { Home } from './home.js';
import { Admin } from './admin.js';

const loadedPages = {
	"#landing": Home,
	"#admin": Admin,
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
